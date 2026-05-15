import { DetallePedidoInputDto, DetallePedidoOutputDto } from './detalle-pedido';

export interface Pedido {
    id?: number;
    precio: number;
    fechaCompra: string;
    clienteId?: number;
    repartidorId?: number;
    estadoId?: number;
}

export interface PedidoInputDto {
    clienteId: number;
    empresaId: number;
    idRepartidor?: number;
    repartidorId?: number;
    detalles: DetallePedidoInputDto[];
}

export interface PedidoOutputDto {
    id: number;
    fechaCompra: string;
    precioTotal: number;
    clienteId: number;
    nombreCliente: string;
    empresaId: number;
    nombreEmpresa: string;
    direccionEntrega?: string;
    estadoNombre: string;
    repartidorId?: number;
    nombreRepartidor?: string;
    detalles: DetallePedidoOutputDto[];
}