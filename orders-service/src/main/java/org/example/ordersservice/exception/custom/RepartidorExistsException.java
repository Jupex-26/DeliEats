package org.example.ordersservice.exception.custom;

public class RepartidorExistsException extends RuntimeException {
  public RepartidorExistsException(String message) {
    super(message);
  }
}
