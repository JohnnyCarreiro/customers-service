package com.johnnycarreiro.crs.core.domain.validation;

import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;

public class StackValidationHandler implements ValidationHandler {

  private final List<Error> errors;

  private StackValidationHandler(final List<Error> errors) {
    this.errors = errors;
  }

  public static StackValidationHandler create() {
    return new StackValidationHandler(new ArrayList<>());
  }

  public static StackValidationHandler create(final Throwable t) {
    return create(new Error(t.getMessage()));
  }

  public static StackValidationHandler create(final Error anError) {
    return new StackValidationHandler(new ArrayList<>()).append(anError);
  }
  @Override
  public StackValidationHandler append(final Error anError) {
    this.errors.add(anError);
    return this;
  }

  @Override
  public StackValidationHandler append(final ValidationHandler aHandler) {
    this.errors.addAll(aHandler.getErrors());
    return this;
  }

  @Override
  public StackValidationHandler validate(final Validation aValidation) {
    try {
      aValidation.validate();
    } catch (DomainException ex) {
      this.errors.addAll(ex.getErrors());
    } catch (Throwable t) {
      this.errors.add(new Error(t.getMessage()));
    }
    return this;
  }

  @Override
  public List<Error> getErrors() {
    return this.errors;
  }
}
