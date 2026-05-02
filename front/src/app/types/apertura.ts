export interface Apertura {
    id?: number;
    dia: string;
    horaApertura: string;
    horaCierre: string;
    empresaId?: number;
}

export interface AperturaInputDto {
    dia: string;
    horaApertura: string;
    horaCierre: string;
    empresaId: number;
}

export interface AperturaOutputDto {
    id: number;
    dia: string;
    horaApertura: string;
    horaCierre: string;
    empresaId: number;
    nombreEmpresa: string;
}
