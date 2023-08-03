package com.johnnycarreiro.crs.core.domain.validation;

import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
  @Override
  public ValidationHandler append(final Error anError) {
    throw DomainException.with(anError);
  }

  @Override
  public ValidationHandler append(final ValidationHandler aHandler) {
    throw DomainException.with(aHandler.getErrors());
  }

  @Override
  public ValidationHandler validate(final Validation aValidation) {
    try {
      aValidation.validate();
    } catch (Exception ex) {
      throw  DomainException.with(List.of(new Error(ex.getMessage())));
    }
    return this;
  }

  @Override
  public List<Error> getErrors() {
    return List.of();
  }
}
