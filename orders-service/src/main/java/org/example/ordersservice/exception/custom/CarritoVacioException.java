package org.example.ordersservice.exception.custom;

public class CarritoVacioException extends RuntimeException {
  public CarritoVacioException(String message) {
    super(message);
  }
}
