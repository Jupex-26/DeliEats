export interface Apertura {
    id?: number;
    dia: string;
    horaApertura: string;
    horaCierre: string;
}

export interface AperturaInputDto {
    dia: string;
    horaApertura: string;
    horaCierre: string;
}

export interface AperturaOutputDto {
    id: number;
    dia: string;
    horaApertura: string;
    horaCierre: string;
}
