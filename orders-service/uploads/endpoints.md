# Listado de Endpoints - DeliEats Orders Service

A continuación se detallan todos los endpoints declarados en los controladores del proyecto.

## AperturaController (`/api/aperturas`)
- `POST /api/aperturas` - Crear apertura
- `GET /api/aperturas` - Listar aperturas (paginado)
- `GET /api/aperturas/{id}` - Obtener apertura por ID
- `PUT /api/aperturas/{id}` - Actualizar apertura
- `DELETE /api/aperturas/{id}` - Eliminar apertura

## CarritoController (`/api/carritos`)
- `POST /api/carritos` - Crear carrito
- `GET /api/carritos` - Listar carritos (paginado)
- `GET /api/carritos/{id}` - Obtener carrito por ID
- `GET /api/carritos/usuario/{usuarioId}` - Obtener carrito por ID de usuario
- `DELETE /api/carritos/{id}` - Eliminar carrito
- `POST /api/carritos/{id}/limpiar` - Vaciar carrito
- `PUT /api/carritos/{id}/productos/{productoId}?cantidad={cantidad}` - Actualizar cantidad de producto
- `GET /api/carritos/{id}/total` - Calcular total del carrito
- `DELETE /api/carritos/{id}/productos/{productoId}` - Eliminar producto del carrito

## ClienteController (`/api/clientes`)
- `POST /api/clientes` - Crear cliente
- `GET /api/clientes` - Listar clientes (paginado)
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

## DetalleCarritoController (`/api/detalles-carrito`)
- `POST /api/detalles-carrito` - Crear detalle de carrito
- `GET /api/detalles-carrito` - Listar detalles de carrito (paginado)
- `GET /api/detalles-carrito/{id}` - Obtener detalle por ID
- `GET /api/detalles-carrito/carrito/{carritoId}` - Obtener detalles por ID de carrito
- `PUT /api/detalles-carrito/{id}` - Actualizar detalle
- `DELETE /api/detalles-carrito/{id}` - Eliminar detalle

## DetallePedidoController (`/api/detalles-pedido`)
- `POST /api/detalles-pedido` - Crear detalle de pedido
- `GET /api/detalles-pedido` - Listar detalles de pedido (paginado)
- `GET /api/detalles-pedido/{id}` - Obtener detalle por ID
- `GET /api/detalles-pedido/pedido/{pedidoId}` - Obtener detalles por ID de pedido
- `PUT /api/detalles-pedido/{id}` - Actualizar detalle
- `DELETE /api/detalles-pedido/{id}` - Eliminar detalle

## EmpresaController (`/api/empresas`)
- `POST /api/empresas` - Crear empresa
- `GET /api/empresas` - Listar empresas (paginado)
- `GET /api/empresas/{id}` - Obtener empresa por ID
- `PUT /api/empresas/{id}` - Actualizar empresa
- `DELETE /api/empresas/{id}` - Eliminar empresa

## EstadoController (`/api/estados`)
- `POST /api/estados` - Crear estado
- `GET /api/estados` - Listar estados (paginado)
- `GET /api/estados/{id}` - Obtener estado por ID
- `PUT /api/estados/{id}` - Actualizar estado
- `DELETE /api/estados/{id}` - Eliminar estado

## MensajeController (`/api/mensajes`)
- `POST /api/mensajes` - Crear mensaje
- `GET /api/mensajes` - Listar mensajes (paginado)
- `GET /api/mensajes/{id}` - Obtener mensaje por ID
- `DELETE /api/mensajes/{id}` - Eliminar mensaje
- `PATCH /api/mensajes/{id}/leer` - Marcar mensaje como leído

## PedidoController (`/api/pedidos`)
- `POST /api/pedidos` - Crear pedido
- `GET /api/pedidos` - Listar pedidos (paginado)
- `GET /api/pedidos/{id}` - Obtener pedido por ID
- `GET /api/pedidos/cliente/{clienteId}` - Obtener pedidos de un cliente
- `PUT /api/pedidos/{id}` - Actualizar pedido
- `PATCH /api/pedidos/{id}/estado/{estadoId}` - Actualizar estado del pedido
- `DELETE /api/pedidos/{id}` - Eliminar pedido

## ProductoController (`/api/productos`)
- `POST /api/productos` - Crear producto
- `GET /api/productos` - Listar productos (paginado)
- `GET /api/productos/{id}` - Obtener producto por ID
- `GET /api/productos/categoria/{categoriaId}` - Obtener productos por ID de categoría
- `PUT /api/productos/{id}` - Actualizar producto
- `PATCH /api/productos/{id}/stock?cantidad={cantidad}` - Actualizar stock del producto
- `DELETE /api/productos/{id}` - Eliminar producto

## RepartidorController (`/api/repartidores`)
- `POST /api/repartidores` - Crear repartidor
- `GET /api/repartidores` - Listar repartidores (paginado)
- `GET /api/repartidores/{id}` - Obtener repartidor por ID
- `GET /api/repartidores/disponibles` - Obtener repartidores disponibles
- `PUT /api/repartidores/{id}` - Actualizar repartidor
- `PATCH /api/repartidores/{id}/disponibilidad?disponible={disponible}` - Actualizar disponibilidad
- `DELETE /api/repartidores/{id}` - Eliminar repartidor

## RolController (`/api/roles`)
- `POST /api/roles` - Crear rol
- `GET /api/roles` - Listar roles (paginado)
- `GET /api/roles/{id}` - Obtener rol por ID
- `PUT /api/roles/{id}` - Actualizar rol
- `DELETE /api/roles/{id}` - Eliminar rol

## UserController (`/api/users`)
- `POST /api/users` - Crear usuario
- `GET /api/users` - Listar usuarios (paginado)
- `GET /api/users/{id}` - Obtener usuario por ID
- `GET /api/users/email/{email}` - Obtener usuario por email
- `PUT /api/users/{id}` - Actualizar usuario
- `PATCH /api/users/{id}/password` - Actualizar contraseña
- `DELETE /api/users/{id}` - Eliminar usuario