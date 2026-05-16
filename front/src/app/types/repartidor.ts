export interface RepartidorOutputDto {
    id: number;
    clienteId: number;
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