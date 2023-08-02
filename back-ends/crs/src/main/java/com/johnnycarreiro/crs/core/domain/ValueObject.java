package com.johnnycarreiro.crs.core.domain;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;

public abstract class ValueObject<T> {
  public abstract T getValue();

  public abstract void validate(ValidationHandler handler);
}
