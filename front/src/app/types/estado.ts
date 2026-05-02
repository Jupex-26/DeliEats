export interface Estado {
    id?: number;
    nombre: string;
}

export interface EstadoInputDto {
    nombre: string;
}

export interface EstadoOutputDto {
    id: number;
    nombre: string;
}
