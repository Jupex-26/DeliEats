# Documentación de APIs - DeliEats

Este documento detalla todas las APIs internas (Backend Spring Boot) y externas utilizadas en el proyecto DeliEats.

## 1. APIs Internas (REST Backend)
La URL base para estas APIs es: `http://localhost:8080/api`

### Autenticación (`/auth`)
- `POST /auth/login`: Iniciar sesión y obtener token/datos de usuario.
- `POST /auth/register`: Registro de nuevos usuarios.

### Clientes (`/clientes`)
- `GET /clientes`: Listar clientes (con paginación).
- `GET /clientes/{id}`: Obtener detalles de un cliente.
- `PUT /clientes/{id}`: Actualizar perfil de cliente.
- `POST /clientes/{id}/solicitar-repartidor`: Enviar solicitud para trabajar como repartidor.

### Empresas / Restaurantes (`/empresas`)
- `GET /empresas`: Listar restaurantes.
- `GET /empresas/{id}`: Detalles de un restaurante.
- `PUT /empresas/{id}`: Actualizar perfil de empresa.
- `POST /empresas`: Registrar nueva empresa.

### Pedidos (`/pedidos`)
- `GET /pedidos`: Listar pedidos (filtrados por rol).
- `GET /pedidos/{id}`: Detalle de un pedido específico.
- `POST /pedidos`: Crear un nuevo pedido (Checkout).
- `PUT /pedidos/{id}`: Actualizar datos de un pedido (asignar repartidor).
- `PATCH /pedidos/{id}/estado`: Cambiar el estado de un pedido (ej. Preparando, En camino, Entregado).
- `GET /pedidos/usuario/{id}`: Listar historial de pedidos de un usuario.

### Productos (`/productos`)
- `GET /productos`: Listar todos los productos.
- `GET /productos/empresa/{id}`: Listar productos de un restaurante específico.
- `POST /productos`: Crear nuevo producto.
- `PUT /productos/{id}`: Editar producto.
- `DELETE /productos/{id}`: Eliminar producto.

### Carrito (`/carritos`)
- `GET /carritos/usuario/{id}`: Obtener el carrito activo de un usuario.
- `POST /carritos`: Crear nuevo carrito.
- `PUT /carritos/{id}/productos/{prodId}?cantidad={n}`: Añadir/Actualizar cantidad de un producto.
- `DELETE /carritos/{id}/productos/{prodId}`: Quitar producto del carrito.
- `POST /carritos/{id}/limpiar`: Vaciar carrito.

### Mensajería / Chat (`/mensajes`)
- `GET /mensajes/chat`: Obtener historial de chat entre dos usuarios (parámetros `emisorId` y `receptorId`).
- `PATCH /mensajes/{id}/leer`: Marcar mensaje como leído.

### Repartidores (`/repartidores`)
- `GET /repartidores/{id}`: Detalles de repartidor.
- `PATCH /repartidores/{id}/disponibilidad?disponible={bool}`: Cambiar estado de disponibilidad.
- `GET /repartidores/disponibles`: Listar repartidores libres.

---

## 2. Comunicación en Tiempo Real (WebSockets)
Utiliza STOMP sobre SockJS.
- **Endpoint de Conexión**: `http://localhost:8080/api/ws-chat`

### Canales (Topics)
- `/topic/mensajes/{usuarioId}`: Recibir mensajes de chat privados.
- `/topic/location/{clienteId}`: Recibir ubicación del repartidor en tiempo real (para el cliente).
- `/app/chat/{destinoId}`: Enviar mensaje a un usuario.
- `/app/location`: Enviar ubicación actual (desde el repartidor).

---

## 3. APIs Externas

### OpenStreetMap (Nominatim)
Utilizada para geocodificación de direcciones.
- **URL**: `https://nominatim.openstreetmap.org/search`
- **Uso**: Validar direcciones físicas y obtener coordenadas (Lat/Lon).

### Leaflet (Map Tiles)
Utilizada para renderizar el mapa interactivo.
- **Tiles**: `https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png`
- **Assets**: `https://unpkg.com/leaflet@1.9.4/dist/images/` (Marcadores y sombras).

---

## 4. Almacenamiento de Archivos (Uploads)
- **URL**: `http://localhost:8080/api/uploads/{filename}`
- **Uso**: Acceso a fotos de perfil, logotipos de empresas y fotos de productos.
