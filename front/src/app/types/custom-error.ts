export interface CustomError {
    message: string;
    timestamp?: string;
    httpCode?: number;
    fieldErrors?: Record<string, string>;
}