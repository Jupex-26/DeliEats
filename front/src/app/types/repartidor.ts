import { ClienteOutputDto } from './cliente';

export interface RepartidorOutputDto {
    id: number;
    clienteId: number;
    cliente?: ClienteOutputDto; // Objeto anidado solicitado
    nombreCliente: string;
    emailCliente: string;
    telefonoCliente: string;
    direccionCliente: string;
    fotoCliente?: string;
    disponible: boolean;
    aprobado: boolean;
}

export interface RepartidorInputDto {
    clienteId: number;
    disponible: boolean;
}