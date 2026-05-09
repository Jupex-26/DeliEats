import { User, UserInputDto, UserOutputDto } from './user';
import { ProductoOutputDto } from './producto';
import { Apertura, AperturaInputDto, AperturaOutputDto } from './apertura';

export interface Empresa extends User {
    descripcion?: string;
    correoContacto?: string;
    telefonoContacto?: string;
    tipoCocina?: string;
    aperturas?: Apertura[];
}

export interface EmpresaInputDto extends UserInputDto {
    descripcion: string;
    correoContacto: string;
    telefonoContacto: string;
    tipoCocina: string;
    aperturas?: AperturaInputDto[];
}

export interface EmpresaOutputDto extends UserOutputDto {
    descripcion: string;
    correoContacto: string;
    telefonoContacto: string;
    tipoCocina: string;
    productos?: ProductoOutputDto[];
    aperturas: AperturaOutputDto[];
}
