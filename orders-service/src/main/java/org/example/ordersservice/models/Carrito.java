package org.example.ordersservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "carrito")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCarrito> detalles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carrito carrito)) return false;
        return id != null && id.equals(carrito.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addDetalles(List<DetalleCarrito> detalles){
        this.detalles.addAll(detalles);
    }

    public void eliminarProducto(Long productoId) {
        this.detalles.removeIf(d -> d.getProducto().getId().equals(productoId));
    }

    public void agregarProducto(Producto producto, Integer cantidad) {
        if (Objects.isNull(cantidad) || cantidad <= 0) throw new RuntimeException("Cantidad no válida");

        eliminarProducto(producto.getId());

        DetalleCarrito nuevoDetalle = DetalleCarrito.builder()
                .carrito(this)
                .producto(producto)
                .cantidad(cantidad)
                .build();

        this.detalles.add(nuevoDetalle);
    }

    public BigDecimal getPrecioTotal() {
        if (this.detalles == null || this.detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return this.detalles.stream()
                .map(d -> d.getProducto().getPrecio().multiply(new BigDecimal(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public void cleanDetalles() {
        this.getDetalles().clear();
    }

    public boolean hasNotDetalles() {
        return CollectionUtils.isEmpty(this.detalles);
    }
}