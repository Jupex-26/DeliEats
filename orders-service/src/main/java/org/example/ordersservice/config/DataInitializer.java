package org.example.ordersservice.config;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.*;
import org.example.ordersservice.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final EstadoRepository estadoRepository;
    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final RepartidorRepository repartidorRepository;
    private final ProductoRepository productoRepository;
    private final TipoCocinaRepository tipoCocinaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AperturaRepository aperturaRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    @Override
    public void run(String... args) {
        if (rolRepository.count() == 0) {
            initData();
        }
    }

    private void initData() {
        // 1. Crear Roles
        Rol rolAdmin = rolRepository.save(Rol.builder().nombre("ROLE_ADMIN").build());
        Rol rolCliente = rolRepository.save(Rol.builder().nombre("ROLE_CLIENTE").build());
        Rol rolEmpresa = rolRepository.save(Rol.builder().nombre("ROLE_EMPRESA").build());
        Rol rolRepartidor = rolRepository.save(Rol.builder().nombre("ROLE_REPARTIDOR").build());

        // 2. Crear Estados de Pedido
        estadoRepository.save(Estado.builder().nombre("PENDIENTE").build());
        estadoRepository.save(Estado.builder().nombre("PREPARANDO").build());
        estadoRepository.save(Estado.builder().nombre("EN CAMINO").build());
        Estado estadoEntregado = estadoRepository.save(Estado.builder().nombre("ENTREGADO").build());
        Estado estadoCancelado = estadoRepository.save(Estado.builder().nombre("CANCELADO").build());

        // 3. Crear Tipos de Cocina
        TipoCocina cocinaItaliana = tipoCocinaRepository.save(TipoCocina.builder().nombre("Italiana").build());
        TipoCocina cocinaJaponesa = tipoCocinaRepository.save(TipoCocina.builder().nombre("Japonesa").build());
        tipoCocinaRepository.save(TipoCocina.builder().nombre("Mediterránea").build());
        tipoCocinaRepository.save(TipoCocina.builder().nombre("Mexicana").build());
        tipoCocinaRepository.save(TipoCocina.builder().nombre("Americana").build());

        String defaultPassword = passwordEncoder.encode("Admin@1234");

        // 4. Crear Admin
        userRepository.save(User.builder()
                .nombre("Administrador")
                .email("admin@gmail.com")
                .password(defaultPassword)
                .telefono(666666666L)
                .direccion("Oficina Central")
                .rol(rolAdmin)
                .build());

        // 5. Crear Clientes
        Cliente cliente1 = clienteRepository.save(Cliente.builder()
                .nombre("Juan Pérez")
                .email("juan@cliente.com")
                .password(defaultPassword)
                .telefono(666666666L)
                .direccion("Calle Falsa 123")
                .rol(rolCliente)
                .foto("5f088ad3-448c-42fb-82e8-3802b19ae9d5_columbina.jpeg")
                .fechaNacimiento(LocalDateTime.of(1990, 5, 15, 0, 0))
                .build());

        Cliente cliente2 = clienteRepository.save(Cliente.builder()
                .nombre("María López")
                .email("maria@cliente.com")
                .password(defaultPassword)
                .telefono(987654321L)
                .direccion("Avenida Siempre Viva 456")
                .rol(rolCliente)
                .fechaNacimiento(LocalDateTime.of(1995, 8, 20, 0, 0))
                .build());

        // 6. Crear Empresas
        Empresa pizzaNostra = Empresa.builder()
                .nombre("Pizza Nostra")
                .email("contacto@pizzanostra.com")
                .password(defaultPassword)
                .telefono(666666666L)
                .direccion("Plaza Mayor 1")
                .rol(rolEmpresa)
                .descripcion("La mejor pizza italiana de la ciudad")
                .correoContacto("info@pizzanostra.com")
                .telefonoContacto("666666666")
                .tipoCocina(cocinaItaliana)
                .foto("pizza.png")
                .build();
        pizzaNostra = empresaRepository.save(pizzaNostra);

        Empresa sushiMaster = Empresa.builder()
                .nombre("Sushi Master")
                .email("contacto@sushimaster.com")
                .password(defaultPassword)
                .telefono(666666666L)
                .direccion("Calle del Pez 2")
                .rol(rolEmpresa)
                .descripcion("Sushi fresco todos los días")
                .correoContacto("info@sushimaster.com")
                .telefonoContacto("666666666")
                .tipoCocina(cocinaJaponesa)
                .foto("sushi.png")
                .build();
        sushiMaster = empresaRepository.save(sushiMaster);

        // 6.1 Crear Aperturas (Horarios) - Todos los días de 13:00 a 23:30 para Pizza Nostra
        List<Apertura> aperturasPizza = new ArrayList<>();
        for (Dia dia : Dia.values()) {
            aperturasPizza.add(Apertura.builder()
                    .dia(dia)
                    .horaApertura(LocalTime.of(0, 1))
                    .horaCierre(LocalTime.of(23, 59))
                    .empresa(pizzaNostra)
                    .build());
        }
        aperturaRepository.saveAll(aperturasPizza);

        // Todos los días de 13:00 a 23:30 para Sushi Master
        List<Apertura> aperturasSushi = new ArrayList<>();
        for (Dia dia : Dia.values()) {
            aperturasSushi.add(Apertura.builder()
                    .dia(dia)
                    .horaApertura(LocalTime.of(13, 0))
                    .horaCierre(LocalTime.of(23, 30))
                    .empresa(sushiMaster)
                    .build());
        }
        aperturaRepository.saveAll(aperturasSushi);

        // 7. Crear Repartidores
        Repartidor repartidor1 = repartidorRepository.save(Repartidor.builder()
                .nombre("Carlos Gómez")
                .email("carlos@repartidor.com")
                .password(defaultPassword)
                .telefono(777888999L)
                .direccion("Calle Central 10")
                .rol(rolRepartidor)
                .disponible(true)
                .aprobado(true)
                .build());

        Repartidor repartidor2 = repartidorRepository.save(Repartidor.builder()
                .nombre("Ana Torres")
                .email("ana@repartidor.com")
                .password(defaultPassword)
                .telefono(101010101L)
                .direccion("Calle Norte 5")
                .rol(rolRepartidor)
                .disponible(false)
                .aprobado(true)
                .build());

        // 8. Crear Productos
        Producto pizzaMarg = productoRepository.save(Producto.builder()
                .nombre("Pizza Margarita")
                .descripcion("Salsa de tomate, mozzarella y albahaca fresca")
                .precio(new BigDecimal("10.50"))
                .cantidad(50)
                .empresa(pizzaNostra)
                .build());

        Producto pizzaPepp = productoRepository.save(Producto.builder()
                .nombre("Pizza Pepperoni")
                .descripcion("Salsa de tomate, mozzarella y pepperoni picante")
                .precio(new BigDecimal("12.00"))
                .cantidad(30)
                .empresa(pizzaNostra)
                .build());

        Producto makiSalmon = productoRepository.save(Producto.builder()
                .nombre("Maki de Salmón")
                .descripcion("Rollo de arroz relleno de salmón fresco (6 uds)")
                .precio(new BigDecimal("8.50"))
                .cantidad(100)
                .foto("b88a00c5-14c8-49f7-85ab-f4cf85c03256_makis-salmon.webp")
                .empresa(sushiMaster)
                .build());

        Producto nigiriAtun = productoRepository.save(Producto.builder()
                .nombre("Nigiri de Atún")
                .descripcion("Corte de atún sobre base de arroz (2 uds)")
                .precio(new BigDecimal("4.50"))
                .cantidad(40)
                .foto("7579ce13-a553-4efc-a224-12676b2993ee_csm_417-recipe-page-Nigiri_desktop_4f926bea38.jpg")
                .empresa(sushiMaster)
                .build());

        // 9. Crear Pedidos para Sushi Master
        crearPedidoEjemplo(sushiMaster, cliente1, repartidor1, estadoEntregado, LocalDateTime.now().minusMonths(1).withDayOfMonth(15), List.of(makiSalmon, makiSalmon, nigiriAtun));
        crearPedidoEjemplo(sushiMaster, cliente2, repartidor2, estadoEntregado, LocalDateTime.now().minusMonths(2).withDayOfMonth(10), List.of(nigiriAtun, nigiriAtun, nigiriAtun, nigiriAtun));
        crearPedidoEjemplo(sushiMaster, cliente1, null, estadoCancelado, LocalDateTime.now().minusMonths(3).withDayOfMonth(5), List.of(makiSalmon));
        crearPedidoEjemplo(sushiMaster, cliente2, repartidor1, estadoEntregado, LocalDateTime.now().minusDays(2), List.of(makiSalmon, nigiriAtun));
        crearPedidoEjemplo(sushiMaster, cliente1, repartidor2, estadoEntregado, LocalDateTime.now().minusMonths(5).withDayOfMonth(20), List.of(makiSalmon, makiSalmon, makiSalmon));


        System.out.println("✅ Datos iniciales cargados con éxito.");
    }

    private void crearPedidoEjemplo(Empresa empresa, Cliente cliente, Repartidor repartidor, Estado estado, LocalDateTime fechaCompra, List<Producto> productos) {
        BigDecimal precioTotal = productos.stream()
                .map(Producto::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = Pedido.builder()
                .empresa(empresa)
                .cliente(cliente)
                .repartidor(repartidor)
                .estado(estado)
                .fechaCompra(fechaCompra)
                .precio(precioTotal)
                .build();

        pedido = pedidoRepository.save(pedido);

        List<DetallePedido> detalles = new ArrayList<>();
        // Agrupar productos por cantidad para no crear un detalle por cada producto repetido si quisiéramos,
        // pero por simplicidad de este mock lo agregamos uno a uno o sumamos cantidad.
        // Aquí asumimos 1 de cantidad por cada entrada en la lista para simplificar.
        for (Producto producto : productos) {
            boolean encontrado = false;
            for (DetallePedido dp : detalles) {
                if (dp.getProducto().getId().equals(producto.getId())) {
                    dp.setCantidad(dp.getCantidad() + 1);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                detalles.add(DetallePedido.builder()
                        .pedido(pedido)
                        .producto(producto)
                        .cantidad(1)
                        .precioUnitario(producto.getPrecio())
                        .build());
            }
        }

        detallePedidoRepository.saveAll(detalles);
        pedido.setDetalles(detalles);
        pedidoRepository.save(pedido);
    }
}
