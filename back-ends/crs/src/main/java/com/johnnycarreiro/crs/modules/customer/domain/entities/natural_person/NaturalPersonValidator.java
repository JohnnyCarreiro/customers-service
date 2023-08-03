package com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person;

import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.Validator;

public class NaturalPersonValidator extends Validator {

  private final NaturalPerson naturalPerson;
  public NaturalPersonValidator(final NaturalPerson aNaturalPerson, final ValidationHandler aHandler) {
    super(aHandler);
    this.naturalPerson = aNaturalPerson;
  }

  @Override
  public void validate() {
    checkNameConstraints();
  }

  private void checkNameConstraints() {
    var name = naturalPerson.getName();
    if (name == null) {
      this.validationHandler().append(new Error(("`Name` shouldn't be null")));
      return;
    }
    if (name.isBlank()) {
      this.validationHandler().append(new Error(("`Name` shouldn't be Empty")));
      return;
    }
    var length = name.trim().length();
    if (length < 3 || length > 255) {
      this.validationHandler().append(new Error(("`Name` must have at least three and less than 255 characters")));
    }
  }
}
