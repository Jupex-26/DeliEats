export interface User {
    id?: number;
    nombre: string;
    email: string;
    password?: string;
    telefono?: number;
    direccion?: string;
    foto?: string;
    rolId?: number;
}

export interface UserInputDto {
    nombre: string;
    email: string;
    password?: string;
    telefono?: number;
    direccion?: string;
    foto?: string;
    rolId: number;
}

export interface UserOutputDto {
    id: number;
    nombre: string;
    email: string;
    telefono?: number;
    direccion?: string;
    foto?: string;
    nombreRol: string;
}
