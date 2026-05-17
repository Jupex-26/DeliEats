# DeliEats

DeliEats es una plataforma moderna y completa de reparto de comida a domicilio (Food Delivery). Conecta a **clientes**, **restaurantes (empresas)** y **repartidores** en un ecosistema interactivo que opera con flujos de datos en tiempo real para mensajería y seguimiento GPS.

La plataforma permite a los usuarios registrarse, explorar menús de restaurantes locales, realizar pedidos, descargar facturas en formato PDF, y comunicarse directamente en tiempo real mediante un chat bidireccional y geolocalización GPS en vivo sobre un mapa interactivo (Leaflet).

---

## Arquitectura y Tecnologías

El proyecto está estructurado con una arquitectura robusta de microservicios, lista para producción y completamente orquestada en **Kubernetes**.

*   **Frontend:** Aplicación SPA desarrollada en **Angular 21** integrado con **Ionic 8 (Standalone)**.
*   **Estilos:** **Tailwind CSS v3 (versión 3.4.1)** para un diseño ágil, responsivo y altamente personalizable, complementado con PostCSS y Autoprefixer.
*   **Backend:** API REST y servicios asíncronos creados con **Java 21** y **Spring Boot 4 (versión parent 4.0.5)**.
*   **Base de Datos:** **MySQL** para la persistencia transaccional y relacional de usuarios, pedidos, menús y chats.
*   **Mensajería Asíncrona & Tiempo Real (Event-Driven):** **Apache Kafka & Zookeeper** para desacoplar el procesamiento interno de eventos y orquestar el flujo de mensajes de chat y geolocalización.
*   **Protocolo de Comunicación Bidireccional:** **WebSockets + STOMP / SockJS** para empujar las notificaciones, chats y coordenadas GPS instantáneamente a los navegadores sin necesidad de polling.
*   **Infraestructura:** Manifiestos de **Kubernetes (K8s)** que incluyen Deployments, Services, ConfigMaps, Ingress Controllers, y PersistentVolumeClaims (PVC) para almacenamiento persistente de imágenes compartidas.

---

## Guía de Despliegue en Local (Minikube)

Sigue estos pasos detallados para compilar y levantar toda la infraestructura del ecosistema DeliEats en tu máquina local.

### 1. Requisitos Previos
*   Tener instalado **Docker Desktop**.
*   Tener instalado **Minikube**.
*   Tener instalado la herramienta de terminal de Kubernetes `kubectl`.

### 2. Preparar el Entorno de Minikube
Inicia tu clúster de Kubernetes local y habilita el controlador de enrutamiento web **Ingress**:
```bash
minikube start
minikube addons enable ingress
```

### 3. Construir las Imágenes Docker
Para que el clúster local de Kubernetes acceda a tus imágenes locales sin necesidad de subirlas a un registro público (como Docker Hub), apunta tu terminal al demonio interno de Minikube y construye las imágenes:

```bash
# Conectar tu terminal al Docker de Minikube
eval $(minikube docker-env)

# Compilar la imagen del Backend (v2)
docker build -t delieats-back:v2 -f ./orders-service/Dockerfile ./orders-service

# Compilar la imagen del Frontend (v5 - Versión Estable)
docker build -t delieats-front:v6 -f ./front/Dockerfile ./front
```

### 4. Desplegar los Recursos en Kubernetes
Una vez cargadas las imágenes estables, aplica todos los archivos de configuración YAML en el orden correspondiente:

```bash
# Aplicar todos los manifiestos de K8s (Servicios, Volumenes, MySQL, Kafka, Ingress y Deployments)
kubectl apply -f .
```

Verifica el estado del arranque del clúster. Puede tardar un par de minutos en lo que MySQL y Kafka realizan su inicio inicial y superan los healthchecks:
```bash
kubectl get pods
```

### 5. Configurar el Acceso Local (DNS)
El Ingress redirige el tráfico basándose en el dominio `www.delieats.com`. Debes mapear este dominio a tu máquina local.

Edita tu archivo de hosts (`/etc/hosts` en Mac/Linux o `C:\Windows\System32\drivers\etc\hosts` en Windows) agregando la siguiente línea al final:
```text
127.0.0.1 www.delieats.com
```

### 6. Iniciar el Túnel de Red
Crea un puente de red para que el clúster redirija las peticiones locales del dominio Ingress a tu puerto de red:
```bash
# Mantén esta terminal abierta
minikube tunnel
```

**¡Perfecto!** Ahora abre tu navegador web favorito y dirígete a: **[http://www.delieats.com](http://www.delieats.com)**

---

## Estabilización y Mejoras de Tiempo Real Recientes

Durante las fases de desarrollo recientes, se han aplicado optimizaciones críticas al sistema de comunicación en tiempo real para garantizar un flujo impecable:

1.  **Garantía de Sincronización WebSockets (1 Réplica de Backend):** 
    Debido a que Spring Boot utiliza un broker STOMP en memoria (`SimpleBroker`) para despachar mensajes entrantes de Kafka hacia el cliente suscrito, desplegar múltiples réplicas (3 pods) del backend hacía que los mensajes que Kafka enviaba a la réplica "A" no se entregaran si el navegador del cliente estaba conectado físicamente por WebSocket a la réplica "B". Hemos escalado el despliegue del backend a **1 réplica** (`replicas: 1` en `delieats-back-deployment.yaml`) para unificar la memoria del broker STOMP, asegurando comunicación 100% confiable y sin pérdidas.
    *(Nota: Si en producción necesitas escalar el backend horizontalmente, se requerirá cambiar `SimpleBroker` por un relay externo como RabbitMQ o Redis).*

2.  **Prevención de Suscripciones Duplicadas (Frontend):**
    Añadimos **guardias de idempotencia** en `WebSocketService` y `TrackingService`. Ahora, si la aplicación ya tiene una suscripción STOMP activa para un topic (por ejemplo, seguimiento de ubicación de un pedido), ignorará intentos de suscripción redundantes, impidiendo la saturación del tráfico de red y fugas de memoria.

3.  **Remoción de Race Conditions en Chat (`@defer` corregidos):**
    Tanto en la vista del Cliente (`detalle-pedido-cliente.component.html`) como en la del Repartidor (`perfil.component.html`), el componente `<app-chat-modal>` estaba envuelto en un bloque `@defer (on immediate)`. Esto causaba una carga diferida asíncrona que retrasaba la ejecución de la suscripción al flujo de mensajes de WebSocket en milisegundos tras hacer clic. Si un mensaje llegaba justo al instante de abrir el chat, el componente no estaba listo para escucharlo y el mensaje se perdía. Se han removido los bloques `@defer` haciendo el renderizado del modal de chat completamente síncrono.

4.  **Optimización de Polling de Pedido:**
    Para proteger el rendimiento del servidor y el ciclo de vida del frontend, aumentamos el polling manual de estado del pedido de **1 segundo a 10 segundos**. Al confiar la actualización del mapa y el chat enteramente a los WebSockets asíncronos en tiempo real, el polling de fondo solo actúa como un respaldo lejano, eliminando recargas de UI innecesarias en Angular.

---

## Notas Técnicas y de Almacenamiento

*   **Persistencia de Subidas (Fotos):** Las imágenes de avatares de usuarios y fotos de comida del menú de restaurantes se almacenan en un `PersistentVolumeClaim` (PVC). Esto previene que se borren al reiniciar o recrear los contenedores.
*   **Límites de Carga en Nginx:** Configurado con directivas de cliente para aceptar subidas de archivos multipartes de hasta **50MB**.
