package com.johnnycarreiro.crs.modules.customer.domain.entities.address;

import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.Validator;

import java.util.Objects;
import java.util.regex.Pattern;

public class AddressValidator extends Validator {

  private final Address address;
  public AddressValidator(final Address anAddress, final ValidationHandler aHandler) {
    super(aHandler);
    this.address = anAddress;
  }

  @Override
  public void validate() {
    final var street = address.getStreet();
    final var number = address.getNumber();
    final var complement = address.getComplement();
    final var area = address.getArea();
    final var city = address.getCity();
    final var cep = address.getCep();

    validateStringConstraints(street, "Street");
    validateAddressNumber(number);
    validateComplement(complement);
    validateStringConstraints(area, "Area");
    validateStringConstraints(city, "City");
    validateCep(cep);
  }

  private void validateComplement(String value) {
    if(isNull(value)) return;
    var length = value.trim().length();
    if(isBlank(value)) {
      this.validationHandler().append(new Error("`Complement` is Invalid"));
      return;
    }
    if (length < 1 || length > 255) this.validationHandler()
        .append(new Error("`Complement` must have at least one and less than 255 characters"));
  }

  private void validateAddressNumber(Integer number) {
    if(isNull(number)) this.validationHandler().append(new Error("`Number` shouldn't be Null"));
    if(!isNull(number) && number < 0) this.validationHandler().append(new Error("`Number` should be a positive value"));
  }
  private void validateStringConstraints(String value, String valueName) {
    if(isNull(value)) this.validationHandler().append(new Error(String.format("`%s` shouldn't be Null", valueName)));
    if(!isNull(value) && isBlank(value)) this.validationHandler().append(new Error(String.format("`%s` shouldn't be Empty", valueName)));
    if(!isNull(value) &&isInvalidLength(value)) this.validationHandler()
        .append(new Error(String.format("`%s` must have at least two and less than 255 characters", valueName)));
  }

  private void validateCep(String value) {
    if(isNull(value)) this.validationHandler().append(new Error("`CEP` is Invalid"));
    Pattern pattern = Pattern.compile("[0-9]{5}-[0-9]{3}");
    if(!isNull(value) && !pattern.matcher(value).matches()) this.validationHandler().append(new Error("`CEP` is Invalid"));
  }

  private boolean isNull(Object value) {
    return Objects.isNull(value);
  }
  private boolean isBlank(String value) {
    return value.trim().isBlank();
  }

  private boolean isInvalidLength(String value) {
    var length = value.trim().length();
    return (length < 2 || length > 255);
  }
}

// TODO: Extract CEP validation to its own Value Object;
