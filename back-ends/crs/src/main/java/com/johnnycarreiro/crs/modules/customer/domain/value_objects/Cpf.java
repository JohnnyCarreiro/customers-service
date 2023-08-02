package com.johnnycarreiro.crs.modules.customer.domain.value_objects;

import com.johnnycarreiro.crs.core.domain.ValueObject;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;

import java.util.Objects;

public class Cpf extends ValueObject<String> {
  private final String value;

  private Cpf(final String rawCpf) {
    if(rawCpf == null) {
    this.value = null;
    return;
    }
    this.value = rawCpf.replaceAll("[\\D.]", "");
  }

  @Override
  public void validate(final ValidationHandler handler) {
    new CpfValidator(this, handler).validate();
  }

  public static Cpf create(String rawCpf) {
    return new Cpf(rawCpf);
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Cpf cpf = (Cpf) o;
    return Objects.equals(getValue(), cpf.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
