/* Validadores:
-isNombre: Comprueba si no tiene números
-isPrecio: Compruebe si es un formato válido de dinero
-isTelefono: Comprueba si es un número español
-before(fecha:Date) : Comprueba si la fecha es anterior a otra
-isStrongPassword : Comprueba si la contraseña es fuerte
*/
import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export class Validador {

    /**
     * Valida que el texto solo contenga letras y espacios (Nombres/Apellidos)
     */
    public static isNombre(control: AbstractControl): ValidationErrors | null {
        if (!control.value) return null;
        const nombreRegexp = /^[a-zA-ZÁÉÍÓÚáéíóúÑñÇç\s]+$/;
        return nombreRegexp.test(control.value) ? null : { nombreInvalido: true };
    }
    /**
     * Valida que el precio tenga como máximo 8 enteros y 2 decimales.
     * Ejemplo válido: 12345678.99, 100, 0.5
     */
    public static isPrecio(control: AbstractControl): ValidationErrors | null {
        if (!control.value && control.value !== 0) return null;

        // Explicación Regex:
        // ^[0-9]{1,8}      -> De 1 a 8 dígitos enteros
        // (\.[0-9]{1,2})?  -> Opcionalmente, un punto seguido de 1 o 2 decimales
        // $                -> Fin de cadena
        const precioRegexp = /^[0-9]{1,8}(\.[0-9]{1,2})?$/;

        const valor = control.value.toString();
        return precioRegexp.test(valor) ? null : { precioInvalido: true };
    }

    /**
     * Valida formato de teléfono español (9 dígitos empezando por 6, 7, 8 o 9)
     */
    public static isTelefono(control: AbstractControl): ValidationErrors | null {
        if (!control.value) return null;
        const telRegexp = /^[6789]\d{8}$/;
        return telRegexp.test(control.value.toString()) ? null : { telefonoInvalido: true };
    }

    /**
     * Comprueba si la fecha es anterior a una fecha fija
     */
    public static before(fecha: Date): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            if (!control.value) return null;
            const inputDate = new Date(control.value);
            return inputDate < fecha ? null : { fechaPosterior: { limite: fecha } };
        };
    }

    /**
     * Valida que una fecha sea anterior a otra del mismo formulario (Validación cruzada)
     * Uso: Validador.fechaAnteriorA('fechaNacimiento', 'fechaFallecimiento')
     */
    public static fechaAnteriorA(fechaInicioKey: string, fechaFinKey: string): ValidatorFn {
        return (group: AbstractControl): ValidationErrors | null => {
            const inicio = group.get(fechaInicioKey)?.value;
            const fin = group.get(fechaFinKey)?.value;

            if (inicio && fin && new Date(inicio) > new Date(fin)) {
                return { fechasIncoherentes: true };
            }
            return null;
        };
    }

    /**
     * Comprueba si la contraseña es fuerte (Min 8 caracteres, Mayús, Minús, Número y Especial)
     */
    public static isStrongPassword(control: AbstractControl): ValidationErrors | null {
        const value = control.value;
        if (!value) return null;

        const strongPasswordRegexp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
        return strongPasswordRegexp.test(value) ? null : { strongPassword: true };
    }
}
