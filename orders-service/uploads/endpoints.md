# Listado de Endpoints - DeliEats Orders Service

A continuación se detallan todos los endpoints declarados en los controladores del proyecto, junto con los JSON de entrada requeridos para los métodos POST y PUT.

## AuthController (`/auth`)
- `POST /auth/login` - Iniciar sesión
  ```json
  {
    "email": "usuario@ejemplo.com",
    "password": "password123"
  }
  ```

## AperturaController (`/api/aperturas`)
- `POST /api/aperturas` - Crear apertura
  ```json
  {
    "dia": "LUNES",
    "horaApertura": "2023-10-27T08:00:00",
    "horaCierre": "2023-10-27T22:00:00",
    "empresaId": 1
  }
  ```
- `GET /api/aperturas` - Listar aperturas (paginado)
- `GET /api/aperturas/{id}` - Obtener apertura por ID
- `PUT /api/aperturas/{id}` - Actualizar apertura
  ```json
  {
    "dia": "MARTES",
    "horaApertura": "2023-10-28T09:00:00",
    "horaCierre": "2023-10-28T23:00:00",
    "empresaId": 1
  }
  ```
- `DELETE /api/aperturas/{id}` - Eliminar apertura

## CarritoController (`/api/carritos`)
- `POST /api/carritos` - Crear carrito
  ```json
  {
    "clienteId": 1,
    "productoId": 10,
    "cantidad": 2
  }
  ```
- `GET /api/carritos` - Listar carritos (paginado)
- `GET /api/carritos/{id}` - Obtener carrito por ID
- `GET /api/carritos/usuario/{usuarioId}` - Obtener carrito por ID de usuario
- `DELETE /api/carritos/{id}` - Eliminar carrito
- `POST /api/carritos/{id}/limpiar` - Vaciar carrito
- `PUT /api/carritos/{id}/productos/{productoId}?cantidad={cantidad}` - Actualizar cantidad de producto (No requiere body, usa Query Params)
- `GET /api/carritos/{id}/total` - Calcular total del carrito
- `DELETE /api/carritos/{id}/productos/{productoId}` - Eliminar producto del carrito

## ClienteController (`/api/clientes`)
- `POST /api/clientes` - Crear cliente
  ```json
  {
    "nombre": "Juan Pérez",
    "email": "juan@ejemplo.com",
    "password": "password123",
    "telefono": 123456789,
    "direccion": "Calle Falsa 123",
    "foto": "url_foto.jpg",
    "rolId": 2,
    "fechaNacimiento": "1990-01-01T00:00:00"
  }
  ```
- `GET /api/clientes` - Listar clientes (paginado)
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `PUT /api/clientes/{id}` - Actualizar cliente
  ```json
  {
    "nombre": "Juan Pérez Modificado",
    "email": "juan_mod@ejemplo.com",
    "password": "newpassword123",
    "telefono": 987654321,
    "direccion": "Calle Verdadera 456",
    "foto": "url_foto_nueva.jpg",
    "rolId": 2,
    "fechaNacimiento": "1990-01-01T00:00:00"
  }
  ```
- `DELETE /api/clientes/{id}` - Eliminar cliente

## DetalleCarritoController (`/api/detalles-carrito`)
- `POST /api/detalles-carrito` - Crear detalle de carrito
  ```json
  {
    "id": 1,
    "carritoId": 1,
    "productoId": 10,
    "cantidad": 3
  }
  ```
- `GET /api/detalles-carrito` - Listar detalles de carrito (paginado)
- `GET /api/detalles-carrito/{id}` - Obtener detalle por ID
- `GET /api/detalles-carrito/carrito/{carritoId}` - Obtener detalles por ID de carrito
- `PUT /api/detalles-carrito/{id}` - Actualizar detalle
  ```json
  {
    "id": 1,
    "carritoId": 1,
    "productoId": 10,
    "cantidad": 5
  }
  ```
- `DELETE /api/detalles-carrito/{id}` - Eliminar detalle

## DetallePedidoController (`/api/detalles-pedido`)
- `POST /api/detalles-pedido` - Crear detalle de pedido
  ```json
  {
    "pedidoId": 1,
    "productoId": 15,
    "cantidad": 2,
    "precioUnitario": 12.50
  }
  ```
- `GET /api/detalles-pedido` - Listar detalles de pedido (paginado)
- `GET /api/detalles-pedido/{id}` - Obtener detalle por ID
- `GET /api/detalles-pedido/pedido/{pedidoId}` - Obtener detalles por ID de pedido
- `PUT /api/detalles-pedido/{id}` - Actualizar detalle
  ```json
  {
    "pedidoId": 1,
    "productoId": 15,
    "cantidad": 3,
    "precioUnitario": 12.50
  }
  ```
- `DELETE /api/detalles-pedido/{id}` - Eliminar detalle

## EmpresaController (`/api/empresas`)
- `POST /api/empresas` - Crear empresa
  ```json
  {
    "nombre": "Restaurante Bella Italia",
    "email": "contacto@bellaitalia.com",
    "password": "password123",
    "telefono": 912345678,
    "direccion": "Avenida Roma 10",
    "foto": "logo_empresa.png",
    "rolId": 3,
    "descripcion": "El mejor restaurante italiano de la ciudad",
    "correoContacto": "reservas@bellaitalia.com",
    "telefonoContacto": "912345678",
    "tipoCocina": "Italiana"
  }
  ```
- `GET /api/empresas` - Listar empresas (paginado)
- `GET /api/empresas/{id}` - Obtener empresa por ID
- `PUT /api/empresas/{id}` - Actualizar empresa
  ```json
  {
    "nombre": "Restaurante Bella Italia",
    "email": "contacto@bellaitalia.com",
    "password": "password123",
    "telefono": 912345678,
    "direccion": "Avenida Roma 10",
    "foto": "logo_empresa.png",
    "rolId": 3,
    "descripcion": "Descripción actualizada",
    "correoContacto": "reservas_nuevas@bellaitalia.com",
    "telefonoContacto": "912345678",
    "tipoCocina": "Italiana"
  }
  ```
- `DELETE /api/empresas/{id}` - Eliminar empresa

## EstadoController (`/api/estados`)
- `POST /api/estados` - Crear estado
  ```json
  {
    "nombre": "EN_PREPARACION"
  }
  ```
- `GET /api/estados` - Listar estados (paginado)
- `GET /api/estados/{id}` - Obtener estado por ID
- `PUT /api/estados/{id}` - Actualizar estado
  ```json
  {
    "nombre": "EN_REPARTO"
  }
  ```
- `DELETE /api/estados/{id}` - Eliminar estado

## MensajeController (`/api/mensajes`)
- `POST /api/mensajes` - Crear mensaje
  ```json
  {
    "contenido": "Hola, tu pedido está en camino",
    "receptorId": 5
  }
  ```
- `GET /api/mensajes` - Listar mensajes (paginado)
- `GET /api/mensajes/{id}` - Obtener mensaje por ID
- `DELETE /api/mensajes/{id}` - Eliminar mensaje
- `PATCH /api/mensajes/{id}/leer` - Marcar mensaje como leído

## PedidoController (`/api/pedidos`)
- `POST /api/pedidos` - Crear pedido
  ```json
  {
    "clienteId": 1,
    "detalles": [
      {
        "pedidoId": null,
        "productoId": 10,
        "cantidad": 2,
        "precioUnitario": 15.00
      },
      {
        "pedidoId": null,
        "productoId": 12,
        "cantidad": 1,
        "precioUnitario": 5.50
      }
    ]
  }
  ```
- `GET /api/pedidos` - Listar pedidos (paginado)
- `GET /api/pedidos/{id}` - Obtener pedido por ID
- `GET /api/pedidos/cliente/{clienteId}` - Obtener pedidos de un cliente
- `PUT /api/pedidos/{id}` - Actualizar pedido
  ```json
  {
    "clienteId": 1,
    "detalles": [
      {
        "pedidoId": 1,
        "productoId": 10,
        "cantidad": 3,
        "precioUnitario": 15.00
      }
    ]
  }
  ```
- `PATCH /api/pedidos/{id}/estado/{estadoId}` - Actualizar estado del pedido (No usa body para los campos modificados sino path variables)
- `DELETE /api/pedidos/{id}` - Eliminar pedido

## ProductoController (`/api/productos`)
- `POST /api/productos` - Crear producto
  ```json
  {
    "nombre": "Pizza Margarita",
    "foto": "pizza.jpg",
    "descripcion": "Deliciosa pizza con queso y tomate",
    "precio": 12.99,
    "cantidad": 50,
    "empresaId": 1
  }
  ```
- `GET /api/productos` - Listar productos (paginado)
- `GET /api/productos/{id}` - Obtener producto por ID
- `GET /api/productos/categoria/{categoriaId}` - Obtener productos por ID de categoría
- `PUT /api/productos/{id}` - Actualizar producto
  ```json
  {
    "nombre": "Pizza Margarita Extra Queso",
    "foto": "pizza_extra.jpg",
    "descripcion": "Ahora con más queso",
    "precio": 14.99,
    "cantidad": 45,
    "empresaId": 1
  }
  ```
- `PATCH /api/productos/{id}/stock?cantidad={cantidad}` - Actualizar stock del producto (Usa Query Params)
- `DELETE /api/productos/{id}` - Eliminar producto

## RepartidorController (`/api/repartidores`)
- `POST /api/repartidores` - Crear repartidor
  ```json
  {
    "nombre": "Carlos Repartidor",
    "email": "carlos.repartidor@ejemplo.com",
    "password": "password123",
    "telefono": 654987321,
    "direccion": "Calle Motos 8",
    "foto": "carlos.png",
    "rolId": 4,
    "disponible": true
  }
  ```
- `GET /api/repartidores` - Listar repartidores (paginado)
- `GET /api/repartidores/{id}` - Obtener repartidor por ID
- `GET /api/repartidores/disponibles` - Obtener repartidores disponibles
- `PUT /api/repartidores/{id}` - Actualizar repartidor
  ```json
  {
    "nombre": "Carlos Repartidor",
    "email": "carlos.repartidor@ejemplo.com",
    "password": "password123",
    "telefono": 654987321,
    "direccion": "Calle Motos 8",
    "foto": "carlos.png",
    "rolId": 4,
    "disponible": false
  }
  ```
- `PATCH /api/repartidores/{id}/disponibilidad?disponible={disponible}` - Actualizar disponibilidad (Usa Query Params)
- `DELETE /api/repartidores/{id}` - Eliminar repartidor

## RolController (`/api/roles`)
- `POST /api/roles` - Crear rol
  ```json
  {
    "nombre": "ROLE_CLIENTE"
  }
  ```
- `GET /api/roles` - Listar roles (paginado)
- `GET /api/roles/{id}` - Obtener rol por ID
- `PUT /api/roles/{id}` - Actualizar rol
  ```json
  {
    "nombre": "ROLE_ADMINISTRADOR"
  }
  ```
- `DELETE /api/roles/{id}` - Eliminar rol

## UserController (`/api/users`)
- `POST /api/users` - Crear usuario
  ```json
  {
    "nombre": "Usuario Generico",
    "email": "user@ejemplo.com",
    "password": "password123",
    "telefono": 111222333,
    "direccion": "Avenida Siempre Viva 742",
    "foto": "perfil.jpg",
    "rolId": 1
  }
  ```
- `GET /api/users` - Listar usuarios (paginado)
- `GET /api/users/{id}` - Obtener usuario por ID
- `GET /api/users/email/{email}` - Obtener usuario por email
- `PUT /api/users/{id}` - Actualizar usuario
  ```json
  {
    "nombre": "Usuario Actualizado",
    "email": "user_nuevo@ejemplo.com",
    "password": "newpassword123",
    "telefono": 111222333,
    "direccion": "Avenida Siempre Viva 742",
    "foto": "perfil.jpg",
    "rolId": 1
  }
  ```
- `PATCH /api/users/{id}/password` - Actualizar contraseña (Body con texto plano o String con la nueva contraseña)
- `DELETE /api/users/{id}` - Eliminar usuario
