package org.example.ordersservice.config;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.models.*;
import org.example.ordersservice.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (rolRepository.count() == 0) {
            initData();
        }
    }

    private void initData() {
        // 1. Crear Roles
        Rol rolAdmin = rolRepository.save(Rol.builder().nombre("ADMIN").build());
        Rol rolCliente = rolRepository.save(Rol.builder().nombre("CLIENTE").build());
        Rol rolEmpresa = rolRepository.save(Rol.builder().nombre("EMPRESA").build());
        Rol rolRepartidor = rolRepository.save(Rol.builder().nombre("REPARTIDOR").build());

        // 2. Crear Estados de Pedido
        estadoRepository.save(Estado.builder().nombre("PENDIENTE").build());
        estadoRepository.save(Estado.builder().nombre("PREPARANDO").build());
        estadoRepository.save(Estado.builder().nombre("EN CAMINO").build());
        estadoRepository.save(Estado.builder().nombre("ENTREGADO").build());
        estadoRepository.save(Estado.builder().nombre("CANCELADO").build());

        String defaultPassword = passwordEncoder.encode("Admin@1234");

        // 3. Crear Admin
        userRepository.save(User.builder()
                .nombre("Administrador")
                .email("admin@gmail.com")
                .password(defaultPassword)
                .telefono(000000000L)
                .direccion("Oficina Central")
                .rol(rolAdmin)
                .build());

        // 4. Crear Clientes
        clienteRepository.save(Cliente.builder()
                .nombre("Juan Pérez")
                .email("juan@cliente.com")
                .password(defaultPassword)
                .telefono(123456789L)
                .direccion("Calle Falsa 123")
                .rol(rolCliente)
                .fechaNacimiento(LocalDateTime.of(1990, 5, 15, 0, 0))
                .build());

        clienteRepository.save(Cliente.builder()
                .nombre("María López")
                .email("maria@cliente.com")
                .password(defaultPassword)
                .telefono(987654321L)
                .direccion("Avenida Siempre Viva 456")
                .rol(rolCliente)
                .fechaNacimiento(LocalDateTime.of(1995, 8, 20, 0, 0))
                .build());

        // 5. Crear Empresas
        Empresa empresa1 = empresaRepository.save(Empresa.builder()
                .nombre("Pizza Nostra")
                .email("contacto@pizzanostra.com")
                .password(defaultPassword)
                .telefono(111222333L)
                .direccion("Plaza Mayor 1")
                .rol(rolEmpresa)
                .descripcion("La mejor pizza italiana de la ciudad")
                .correoContacto("info@pizzanostra.com")
                .telefonoContacto("111222333")
                .tipoCocina("Italiana")
                .build());

        Empresa empresa2 = empresaRepository.save(Empresa.builder()
                .nombre("Sushi Master")
                .email("contacto@sushimaster.com")
                .password(defaultPassword)
                .telefono(444555666L)
                .direccion("Calle del Pez 2")
                .rol(rolEmpresa)
                .descripcion("Sushi fresco todos los días")
                .correoContacto("info@sushimaster.com")
                .telefonoContacto("444555666")
                .tipoCocina("Japonesa")
                .build());

        // 6. Crear Repartidores
        repartidorRepository.save(Repartidor.builder()
                .nombre("Carlos Gómez")
                .email("carlos@repartidor.com")
                .password(defaultPassword)
                .telefono(777888999L)
                .direccion("Calle Central 10")
                .rol(rolRepartidor)
                .disponible(true)
                .build());

        repartidorRepository.save(Repartidor.builder()
                .nombre("Ana Torres")
                .email("ana@repartidor.com")
                .password(defaultPassword)
                .telefono(101010101L)
                .direccion("Calle Norte 5")
                .rol(rolRepartidor)
                .disponible(false)
                .build());

        // 7. Crear Productos
        productoRepository.save(Producto.builder()
                .nombre("Pizza Margarita")
                .descripcion("Salsa de tomate, mozzarella y albahaca fresca")
                .precio(new BigDecimal("10.50"))
                .cantidad(50)
                .empresa(empresa1)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Pizza Pepperoni")
                .descripcion("Salsa de tomate, mozzarella y pepperoni picante")
                .precio(new BigDecimal("12.00"))
                .cantidad(30)
                .empresa(empresa1)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Maki de Salmón")
                .descripcion("Rollo de arroz relleno de salmón fresco (6 uds)")
                .precio(new BigDecimal("8.50"))
                .cantidad(100)
                .empresa(empresa2)
                .build());

        productoRepository.save(Producto.builder()
                .nombre("Nigiri de Atún")
                .descripcion("Corte de atún sobre base de arroz (2 uds)")
                .precio(new BigDecimal("4.50"))
                .cantidad(40)
                .empresa(empresa2)
                .build());

        System.out.println("✅ Datos iniciales cargados con éxito.");
    }
}
