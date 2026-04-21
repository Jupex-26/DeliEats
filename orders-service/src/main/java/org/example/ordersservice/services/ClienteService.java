package org.example.ordersservice.services;

import org.example.ordersservice.models.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente save(Cliente cliente);

    List<Cliente> findAll();

    Cliente findById(Long id);

    Cliente update(Long id, Cliente cliente);

    void deleteById(Long id);

}