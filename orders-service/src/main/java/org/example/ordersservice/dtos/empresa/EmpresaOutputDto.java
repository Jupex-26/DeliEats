package org.example.ordersservice.dtos.empresa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.ordersservice.dtos.user.UserOutputDto;
import org.example.ordersservice.dtos.producto.ProductoOutputDto;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmpresaOutputDto extends UserOutputDto {

    private String descripcion;
    private String correoContacto;
    private String telefonoContacto;
    private String tipoCocina;

    private List<ProductoOutputDto> productos;



}
