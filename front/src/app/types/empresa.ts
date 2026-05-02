import { User, UserInputDto, UserOutputDto } from './user';
import { ProductoOutputDto } from './producto';

export interface Empresa extends User {
    descripcion?: string;
    correoContacto?: string;
    telefonoContacto?: string;
    tipoCocina?: string;
}

export interface EmpresaInputDto extends UserInputDto {
    descripcion: string;
    correoContacto?: string;
    telefonoContacto?: string;
    tipoCocina: string;
}

export interface EmpresaOutputDto extends UserOutputDto {
    descripcion: string;
    correoContacto: string;
    telefonoContacto: string;
    tipoCocina: string;
    productos?: ProductoOutputDto[];
}
