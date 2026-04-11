package org.example.ordersservice.dtos.producto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoOutputDto {

    private Long id;
    private String nombre;
    private String foto;
    private String descripcion;
    private BigDecimal precio;
    private Integer cantidad;
    private Long empresaId;
    private String nombreEmpresa;
}
