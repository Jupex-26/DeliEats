package org.example.ordersservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.exception.custom.NotFoundException;
import org.example.ordersservice.models.Cliente;
import org.example.ordersservice.models.Repartidor;
import org.example.ordersservice.repositories.RepartidorRepository;
import org.example.ordersservice.services.RepartidorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repartidorRepository;

    @Override
    public Repartidor save(Repartidor repartidor) {
        return repartidorRepository.save(repartidor);
    }

    @Override
    public Page<Repartidor> findAll(Pageable pageable) {
        return repartidorRepository.findAll(pageable);
    }

    @Override
    public Repartidor findById(Long id) {
        return repartidorRepository.findById(id)
                .or(() -> repartidorRepository.findByClienteId(id))
                .orElseThrow(() -> new NotFoundException("Repartidor no encontrado con ID o Cliente ID: " + id));
    }

    @Override
    public Repartidor findByClienteId(Long clienteId) {
        return repartidorRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new NotFoundException("Repartidor no encontrado para el cliente con ID: " + clienteId));
    }

    @Override
    public Page<Repartidor> findByDisponible(boolean disponible, Pageable pageable) {
        return repartidorRepository.findByDisponible(disponible, pageable);
    }

    @Override
    public Page<Repartidor> findByAprobado(boolean aprobado, Pageable pageable) {
        return repartidorRepository.findByAprobado(aprobado, pageable);
    }

    @Override
    public Repartidor update(Long id, Repartidor repartidor) {
        Repartidor existingRepartidor = findById(id);
        
        // Update fields if needed. Note: Cliente association usually shouldn't change
        existingRepartidor.setDisponible(repartidor.getDisponible());
        existingRepartidor.setAprobado(repartidor.getAprobado());
        
        return repartidorRepository.save(existingRepartidor);
    }

    @Override
    public void deleteById(Long id) {
        repartidorRepository.deleteById(id);
    }

    @Override
    public Repartidor updateDisponibilidad(Long clienteId, boolean disponible) {
        Repartidor repartidor = findById(clienteId);
        repartidor.setDisponible(disponible);
        return repartidorRepository.save(repartidor);
    }

    @Override
    public void createFromCliente(Cliente cliente) {
        if (repartidorRepository.existsByClienteId(cliente.getId())) {
            return;
        }
        Repartidor repartidor = Repartidor.builder()
                .cliente(cliente)
                .disponible(false)
                .aprobado(false)
                .build();
        repartidorRepository.save(repartidor);
    }

    @Override
    public void aprobarRepartidor(Long id, boolean aprobado) {
        Repartidor repartidor = findById(id);
        repartidor.setAprobado(aprobado);
        repartidorRepository.save(repartidor);
    }

    @Override
    public boolean existsById(Long id) {
        return repartidorRepository.existsById(id);
    }

    @Override
    public boolean existsByClienteId(Long clienteId) {
        return repartidorRepository.existsByClienteId(clienteId);
    }

    @Override
    public boolean isRepartidor(Long clienteId) {
        return repartidorRepository.findByClienteId(clienteId)
                .map(Repartidor::getAprobado)
                .orElse(false);
    }

}
