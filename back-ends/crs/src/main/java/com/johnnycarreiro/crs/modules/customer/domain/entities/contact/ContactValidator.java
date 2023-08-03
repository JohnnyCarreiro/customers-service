package com.johnnycarreiro.crs.modules.customer.domain.entities.contact;

import com.johnnycarreiro.crs.core.Utils;
import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.Validator;

import java.util.Objects;

public class ContactValidator extends Validator {

  private final Contact contact;

  public ContactValidator(final Contact aContact, final ValidationHandler aHandler) {
    super(aHandler);
    this.contact = aContact;
  }

  @Override
  public void validate() {
    assertEmailIsValid();
    assertPhoneNumberIsValid();
  }

  private void assertPhoneNumberIsValid() {
    final var phoneNumber = this.contact.getPhoneNumber();
    int MIN_PHONE_NUMBER_LENGTH = 10;
    int MAX_PHONE_NUMBER_LENGTH = 11;
    final String exceptionMessage = Utils.validateStringConstraints(phoneNumber,
      "Phone Number",
      MIN_PHONE_NUMBER_LENGTH,
      MAX_PHONE_NUMBER_LENGTH);
    if(Objects.isNull(exceptionMessage)) {
      return;
    }
    this.validationHandler().append(new Error((exceptionMessage)));
  }

  private void assertEmailIsValid() {
    final var email = this.contact.getEmail();
    if (Objects.isNull(email)) {
      this.validationHandler().append(new Error("`Email` shouldn't be Null"));
      return;
    }
    final var emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`\\{|\\}~^.-]+@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})+$";
    final var isValid = Utils.patternMatches(email, emailPattern);
    if(!isValid) {
      this.validationHandler().append(new Error("Invalid Email Address"));
    }
  }
}
