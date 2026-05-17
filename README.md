# 🍔 DeliEats

DeliEats es una plataforma completa de reparto de comida a domicilio (Food Delivery). Conecta a clientes, restaurantes (empresas) y repartidores en un entorno interactivo y en tiempo real. 

La plataforma permite a los usuarios explorar menús, realizar pedidos, realizar seguimiento en tiempo real y comunicarse directamente con los repartidores mediante un chat integrado.

## 🏗 Arquitectura y Tecnologías

El proyecto está diseñado bajo una arquitectura de microservicios y está preparado para ser desplegado en **Kubernetes**. 

* **Frontend:** Angular + Ionic (servido mediante un proxy Nginx).
* **Backend:** Java + Spring Boot.
* **Base de Datos:** MySQL.
* **Mensajería Asíncrona (Tiempo Real):** Apache Kafka & Zookeeper (utilizado para el chat en vivo y la actualización de ubicaciones de los repartidores).
* **Infraestructura:** Kubernetes (ConfigMap, PVCs para almacenamiento de imágenes persistente, Ingress Controller).

---

## 🚀 Guía de Despliegue en Local (Minikube)

Sigue estos pasos para levantar toda la infraestructura y la aplicación en tu entorno local utilizando Minikube.

### 1. Requisitos Previos
* Tener instalado [Docker](https://www.docker.com/).
* Tener instalado [Minikube](https://minikube.sigs.k8s.io/docs/start/).
* Tener instalado `kubectl`.

### 2. Preparar el Entorno de Minikube
Inicia tu clúster local de Kubernetes y habilita el controlador de Ingress (necesario para el enrutamiento web):
```bash
minikube start
minikube addons enable ingress
```

### 3. Construir las Imágenes Docker
Para que Kubernetes tenga acceso a las imágenes locales sin necesidad de subirlas a Docker Hub, conectaremos nuestra terminal al demonio Docker de Minikube y compilaremos las imágenes directamente allí:

```bash
# Apuntar el demonio Docker al de Minikube
eval $(minikube docker-env)

# Construir imagen del Backend
docker build -t delieats-back:v2 -f ./orders-service/Dockerfile ./orders-service

# Construir imagen del Frontend
docker build -t delieats-front:v2 -f ./front/Dockerfile ./front
```

### 4. Desplegar los Recursos en Kubernetes
Una vez construidas las imágenes, aplicamos todos los archivos de configuración YAML que definen nuestra infraestructura (Almacenamiento, ConfigMaps, Servicios, Despliegues e Ingress):

```bash
# Aplicar todos los manifiestos YAML en el directorio raíz
kubectl apply -f .
```

Verifica que todos los pods se están ejecutando correctamente (puede tardar un par de minutos mientras inicializa MySQL y Kafka):
```bash
kubectl get pods
```

### 5. Configurar el Acceso Local (Hosts)
El Ingress está configurado para responder al dominio `www.delieats.com`. Para acceder desde tu navegador local, debes mapear este dominio a tu dirección local.

Edita tu archivo de hosts (`/etc/hosts` en Mac/Linux o `C:\Windows\System32\drivers\etc\hosts` en Windows) y añade la siguiente línea:
```text
127.0.0.1 www.delieats.com
```

### 6. Exponer la Red y Acceder a la App
Finalmente, crea un túnel de Minikube para que el tráfico de tu ordenador llegue al Ingress de Kubernetes:
```bash
# Mantén esta terminal abierta
minikube tunnel
```

🌐 **¡Listo!** Abre tu navegador y accede a: [http://www.delieats.com](http://www.delieats.com)

---

## 🛠 Notas de Mantenimiento

* **Almacenamiento de Imágenes:** Las fotos subidas a la plataforma se almacenan en un `PersistentVolumeClaim` (PVC). Esto garantiza que las imágenes sobrevivan a reinicios de los pods y estén disponibles para todas las réplicas del backend simultáneamente.
* **Límites de Subida:** El sistema (Nginx + Spring Boot) está configurado para aceptar subidas de archivos (como fotos de productos o avatares) de hasta **50MB**.
