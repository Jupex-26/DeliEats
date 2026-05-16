import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export class Validador {

    public static isNombre(control: AbstractControl): ValidationErrors | null {
        if (!control.value) return null;
        const nombreRegexp = /^[a-zA-ZÁÉÍÓÚáéíóúÑñÇç\s]+$/;
        return nombreRegexp.test(control.value) ? null : { nombreInvalido: true };
    }
    
    public static isPrecio(control: AbstractControl): ValidationErrors | null {
        if (!control.value && control.value !== 0) return null;

        const precioRegexp = /^[0-9]{1,8}(\.[0-9]{1,2})?$/;

        const valor = control.value.toString();
        return precioRegexp.test(valor) ? null : { precioInvalido: true };
    }

    public static isTelefono(control: AbstractControl): ValidationErrors | null {
        if (!control.value) return null;
        const telRegexp = /^[6789]\d{8}$/;
        return telRegexp.test(control.value.toString()) ? null : { telefonoInvalido: true };
    }

    public static before(fecha: Date): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            if (!control.value) return null;
            const inputDate = new Date(control.value);
            return inputDate < fecha ? null : { fechaPosterior: { limite: fecha } };
        };
    }

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

    public static isStrongPassword(control: AbstractControl): ValidationErrors | null {
        const value = control.value;
        if (!value) return null;

        const strongPasswordRegexp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
        return strongPasswordRegexp.test(value) ? null : { strongPassword: true };
    }
}