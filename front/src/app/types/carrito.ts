import { DetalleCarritoOutputDto } from './detalle-carrito';
import { EstadoCarrito } from './estado-carrito';

export interface Carrito {
    id?: number;
    estado: EstadoCarrito;
    clienteId?: number;
}

export interface CarritoInputDto {
    clienteId: number;
    detalles: any[];
}

export interface CarritoOutputDto {
    id: number;
    clienteId: number;
    nombreCliente: string;
    detalles: any[];
    precioTotal: number;
}
