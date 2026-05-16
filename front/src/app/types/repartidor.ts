import { ClienteOutputDto } from './cliente';

export interface RepartidorOutputDto {
    id: number;
    clienteId: number;
    cliente?: ClienteOutputDto; 
    nombre: string;
    email: string;
    telefono: string;
    direccion: string;
    foto?: string;
    disponible: boolean;
    aprobado: boolean;
}

export interface RepartidorInputDto {
    clienteId: number;
    disponible: boolean;
}