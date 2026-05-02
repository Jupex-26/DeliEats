import { User, UserInputDto, UserOutputDto } from './user';

export interface Repartidor extends User {
    disponible: boolean;
}

export interface RepartidorInputDto extends UserInputDto {
    disponible: boolean;
}

export interface RepartidorOutputDto extends UserOutputDto {
    disponible: boolean;
}
