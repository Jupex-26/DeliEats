export interface Rol {
    id?: number;
    nombre: string;
}

export interface RolInputDto {
    nombre: string;
}

export interface RolOutputDto {
    id: number;
    nombre: string;
}