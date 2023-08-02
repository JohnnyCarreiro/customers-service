package com.johnnycarreiro.crs.modules.customer.domain.entities.address;

import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.Validator;

public class UnitTypeValidator extends Validator {

  private final UnitType  unitType;
  public UnitTypeValidator(final UnitType unitType, final ValidationHandler aHandler) {
    super(aHandler);
    this.unitType = unitType;
  }

  @Override
  public void validate() {
    if (unitType == null) {
      this.validationHandler().append(new Error("`Unit Type` is Invalid"));
      return;
    }
    if(unitType == UnitType.HAS_ERROR) {
      this.validationHandler().append(new Error("`Unit Type` is Invalid"));
    }
  }
}
