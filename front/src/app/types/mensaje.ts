export interface Mensaje {
    id?: number;
    fecha: string;
    mensaje: string;
    emisorId?: number;
    receptorId?: number;
    leido: boolean;
}

export interface MensajeInputDto {
    contenido: string;
    receptorId: number;
}

export interface MensajeOutputDto {
    id: number;
    contenido: string;
    fechaEnvio: string;
    emisorId: number;
    nombreEmisor: string;
    receptorId: number;
    nombreReceptor: string;
}