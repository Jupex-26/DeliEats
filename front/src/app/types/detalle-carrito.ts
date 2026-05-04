export interface DetalleCarrito {
    id?: number;
    carritoId?: number;
    productoId?: number;
}

export interface DetalleCarritoInputDto {
    id: number;
    carritoId: number;
    productoId: number;
    cantidad?: number;
}

export interface DetalleCarritoOutputDto {
    id: number;
    productoId: number;
    nombreProducto: string;
    fotoProducto?: string;
    precioUnitario: number;
    cantidad: number;
    subtotal: number;
}
