export interface Producto {
    id?: number;
    nombre: string;
    foto?: string;
    descripcion: string;
    precio: number;
    cantidad: number;
    empresaId?: number;
}

export interface ProductoInputDto {
    nombre: string;
    foto?: string;
    descripcion: string;
    precio: number;
    cantidad: number;
    empresaId: number;
}

export interface ProductoOutputDto {
    id: number;
    nombre: string;
    foto?: string;
    descripcion: string;
    precio: number;
    cantidad: number;
    empresaId: number;
    nombreEmpresa: string;
}