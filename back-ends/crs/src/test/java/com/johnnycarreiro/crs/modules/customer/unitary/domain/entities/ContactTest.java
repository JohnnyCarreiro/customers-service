package com.johnnycarreiro.crs.modules.customer.unitary.domain.entities;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Contact Tests Suite")
public class ContactTest {

  @Test
  @DisplayName("Valid Params - Instantiate Nem Contact")
  public void givenValidParams_whenCallsCreate_thenInstantiateNewContact() {
    final var expectedCustomerId = "2d3220a0-1ed3-481b-852e-9def51fbf640";
    final var expectedPhoneNumber = "12997204431";
    final var expectedEmail = "john.doe@acme.digital";

    final var phoneNumber = "(12) 9.9720-4431";

    final var sut = Contact.create(EntityId.from(expectedCustomerId), phoneNumber, expectedEmail);

    Assertions.assertNotNull(sut.getId());
    Assertions.assertEquals(expectedCustomerId, sut.getCustomerId().getValue());
    Assertions.assertEquals(expectedPhoneNumber, sut.getPhoneNumber());
    Assertions.assertEquals(expectedEmail, sut.getEmail());
    Assertions.assertNotNull(sut.getCreatedAt());
    Assertions.assertNotNull(sut.getUpdatedAt());
    Assertions.assertNull(sut.getDeletedAt());
  }

  @Test
  @DisplayName("Valid Address - Instantiate Nem Contact")
  public void givenValidAddress_whenCallsCreate_thenInstantiateNewContact() {
    final var expectedAddressesLength = 1;
    final var aCustomerId = "2d3220a0-1ed3-481b-852e-9def51fbf640";
    final var aPhoneNumber = "(12) 9.9720-4431";
    final var anEmail = "john.doe@acme.digital";

    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var aAddress = Address
      .create(
        aStreet,
        aNumber,
        null,
        anArea,
        aCity,
        anState,
        expectedCep,
        anUnitType,
        aCustomerId)
      ;

    final var sut = Contact.create(EntityId.from(aCustomerId), aPhoneNumber, anEmail, aAddress);

    Assertions.assertFalse(sut.getAddresses().isEmpty());
    Assertions.assertEquals(expectedAddressesLength, sut.getAddresses().size());

  }

  @Test
  @DisplayName("Invalid Email Address - Throws an Exception")
  public void givenInvalidEmail_whenCallsValidate_thenShouldThrowDomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Invalid Email Address";
    final var aCustomerID = "2d3220a0-1ed3-481b-852e-9def51fbf640";
    final var anEmail = "john.doe@acme";

    final var aPhoneNumber = "(12) 9.9720-4431";

    final var currentContact = Contact.create(EntityId.from(aCustomerID), aPhoneNumber, anEmail);

    final var sut = Assertions.assertThrows(
      DomainException.class, () -> currentContact.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Invalid null Email Address - Throws an Exception")
  public void givenInvalidEmailNull_whenCallsValidate_thenShouldThrowDomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Email` shouldn't be Null";
    final var aCustomerID = "2d3220a0-1ed3-481b-852e-9def51fbf640";
    final String anEmail = null;

    final var aPhoneNumber = "(12) 9.9720-4431";

    final var currentContact = Contact.create(EntityId.from(aCustomerID), aPhoneNumber, anEmail);

    final var sut = Assertions.assertThrows(
      DomainException.class, () -> currentContact.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Invalid Null Phone Number - Throws an Exception")
  public void givenInvalidNullPhoneNumber_whenCallsValidate_thenShouldThrowDomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Phone Number` shouldn't be Null";
    final var aCustomerID = "2d3220a0-1ed3-481b-852e-9def51fbf640";
    final var anEmail = "john.doe@acme.digital";
    final String aPhoneNumber = null;

    final var currentContact = Contact.create(EntityId.from(aCustomerID), aPhoneNumber, anEmail);

    final var sut = Assertions.assertThrows(
      DomainException.class, () -> currentContact.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Too Short Phone Number - Throws an Exception")
  public void givenInvalidPhoneNumberLengthLessThan10Chars_whenCallsValidate_thenShouldThrowDomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Phone Number` must have at least 10 and less than 11 characters";
    final var aCustomerID = "2d3220a0-1ed3-481b-852e-9def51fbf640";
    final var anEmail = "john.doe@acme.digital";
    final var aPhoneNumber = "(12) 9.9720-44";

    final var currentContact = Contact.create(EntityId.from(aCustomerID), aPhoneNumber, anEmail);

    final var sut = Assertions.assertThrows(
      DomainException.class, () -> currentContact.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Too Long Phone Number - Throws an Exception")
  public void givenInvalidPhoneNumberLengthGraterThan11Chars_whenCallsValidate_thenShouldThrowDomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Phone Number` must have at least 10 and less than 11 characters";
    final var aCustomerID = "2d3220a0-1ed3-481b-852e-9def51fbf640";
    final var anEmail = "john.doe@acme.digital";
    final var aPhoneNumber = "(12) 9.9720-44311";

    final var currentContact = Contact.create(EntityId.from(aCustomerID), aPhoneNumber, anEmail);

    final var sut = Assertions.assertThrows(
      DomainException.class, () -> currentContact.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
}
