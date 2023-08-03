package com.johnnycarreiro.crs.modules.customer.domain.entities.address;

import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.Validator;

public class StateValidator extends Validator {

  private final State state;

  protected StateValidator(final State state, final ValidationHandler aHandler) {
    super(aHandler);
    this.state = state;
  }

  @Override
  public void validate() {
    if (this.state == null) {
      this.validationHandler().append(new Error("`State` is Invalid"));
      return;
    }
    if(state == State.HAS_ERROR) {
      this.validationHandler().append(new Error("`State` is Invalid"));
    }
  }
}
