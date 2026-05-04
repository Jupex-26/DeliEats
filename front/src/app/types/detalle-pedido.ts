export interface DetallePedido {
    id?: number;
    precio: number;
    cantidad: number;
    pedidoId?: number;
    productoId?: number;
}

export interface DetallePedidoInputDto {
    pedidoId: number;
    productoId: number;
    cantidad: number;
    precioUnitario: number;
}

export interface DetallePedidoOutputDto {
    id: number;
    productoId: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitario: number;
    subtotal: number;
}
