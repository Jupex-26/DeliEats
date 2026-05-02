import { User, UserInputDto, UserOutputDto } from './user';

export interface Cliente extends User {
    fechaNacimiento: string; // ISO date string
}

export interface ClienteInputDto extends UserInputDto {
    fechaNacimiento: string;
}

export interface ClienteOutputDto extends UserOutputDto {
    fechaNacimiento: string;
}
