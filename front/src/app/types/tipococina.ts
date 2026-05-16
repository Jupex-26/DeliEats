export interface TipoCocina {
    id?: number;
    nombre: string;
}

export interface TipoCocinaInputDto {
    nombre: string;
}

export interface TipoCocinaOutputDto {
    id: number;
    nombre: string;
}