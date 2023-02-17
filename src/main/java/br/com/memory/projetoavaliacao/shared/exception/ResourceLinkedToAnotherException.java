package br.com.memory.projetoavaliacao.shared.exception;

public class ResourceLinkedToAnotherException extends RuntimeException {
  public ResourceLinkedToAnotherException(String message) {
    super(message);
  }
}
