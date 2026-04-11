package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "empresa")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Empresa extends User {

    @Column(length = 500)
    private String descripcion;

    @Column(name = "correo_contacto")
    private String correoContacto;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @Column(name = "tipo_cocina")
    private String tipoCocina;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Producto> productos;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Apertura> aperturas;
}