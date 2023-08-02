package com.johnnycarreiro.crs.core.domain;

import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.Validator;

import java.util.regex.Pattern;

public class EntityIdValidator extends Validator {

  private final EntityId entityId;
  protected EntityIdValidator(final EntityId entityId, final ValidationHandler aHandler) {
    super(aHandler);
    this.entityId = entityId;
  }

  @Override
  public void validate() {
    var id = this.entityId.getValue();
    if(!isValidId(id)) this.validationHandler().append(new Error("`ID` is Invalid"));
  }

  private boolean isValidId(String value) {
    if(value == null || value.isBlank()) return false;
    var UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    return UUID_REGEX.matcher(value).matches();
  }
}
