import { DetalleCarritoOutputDto } from './detalle-carrito';
import { EstadoCarrito } from './estado-carrito';

export interface Carrito {
    id?: number;
    estado: EstadoCarrito;
    clienteId?: number;
}

export interface CarritoInputDto {
    clienteId: number;
    productoId: number;
    cantidad: number;
}

export interface CarritoOutputDto {
    id: number;
    clienteId: number;
    nombreCliente: string;
    estado: string;
    detalles: DetalleCarritoOutputDto[];
    precioTotal: number;
}
