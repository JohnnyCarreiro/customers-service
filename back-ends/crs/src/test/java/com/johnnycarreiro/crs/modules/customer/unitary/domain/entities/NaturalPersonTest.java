package com.johnnycarreiro.crs.modules.customer.unitary.domain.entities;

import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Natural Person Test Suite")
public class NaturalPersonTest {
  @Test
  @DisplayName("Valid - Instantiate Natural Person")
  public void givenValidParams_whenCallCreateANewNaturalPerson_thenInstantiateNewNaturalPerson() {
    final var expectedName = "John Doe";
    final var expectedCpf = "93541134780";
    final var cpf = "935.411.347-80";

    final var sut = NaturalPerson.create(expectedName, cpf);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedName, sut.getName());
    Assertions.assertEquals(expectedCpf, sut.getCpf().getValue());
  }

  @Test
  @DisplayName("Null Name - Throws an Exception")
  public void givenInvalidNullName_whenCallValidateANewNaturalPersonAndValidate_thenItShouldThrowsADomainException() {
    final var expectedErrorCount = 1;
    final var cpf = "935.411.347-80";

    final var expectedErrorMessage = "`Name` shouldn't be null";

    final var actualNaturalPerson = NaturalPerson.create(null, cpf);

    final var sut = Assertions.assertThrows(
        DomainException.class, () -> actualNaturalPerson.validate(new ThrowsValidationHandler())
    );
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Empty Name - Throws an Exception")
  public void givenInvalidEmptyNameParam_whenCallValidateANewNaturalPersonAndValidate_thenItShouldThrowsADomainException() {
    final String expectedName = "   ";
    final var expectedErrorCount = 1;
    final var cpf = "935.411.347-80";

    final var expectedErrorMessage = "`Name` shouldn't be Empty";

    final var actualNaturalPerson = NaturalPerson.create(expectedName, cpf);

    final DomainException sut = Assertions.assertThrows(
        DomainException.class, () -> actualNaturalPerson.validate(new ThrowsValidationHandler())
    );
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Too Short Name - Throws an Exception")
  public void givenInvalidNameLengthLessThan3Chars_whenCallValidadeANewNaturalPersonAndValidate_thenItShouldThrowsADomainException() {
    final String expectedName = "aa ";
    final var expectedErrorCount = 1;
    final var cpf = "935.411.347-80";

    final var expectedErrorMessage = "`Name` must have at least three and less than 255 characters";

    final var actualNaturalPerson = NaturalPerson.create(expectedName, cpf);

    final var sut = Assertions.assertThrows(
        DomainException.class, () -> actualNaturalPerson.validate(new ThrowsValidationHandler())
    );
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Too Long Name - Throws an Exception")
  public void givenInvalidNameLengthGraterThan255Chars_whenCallValidateANewNaturalPersonAndValidate_thenItShouldThrowsADomainException() {
    final String expectedName = """
          Mussum Ipsum, cacilds vidis litro abertis. Posuere libero varius. Nullam a nisl ut ante blandit hendrerit.
          Aenean sit amet nisi.Si u mundo tá muito paradis?
          Toma um mé que o mundo vai girarzis!Quem num gosta di mim que vai caçá sua turmis!
          Nullam volutpat risus nec leo commodo, ut interdum diam laoreet. Sed non consequat odio.
        """;
    final var expectedErrorCount = 1;
    final var cpf = "935.411.347-80";

    final var expectedErrorMessage = "`Name` must have at least three and less than 255 characters";

    final var actualNaturalPerson = NaturalPerson.create(expectedName, cpf);

    final var sut = Assertions.assertThrows(
        DomainException.class,
        () -> actualNaturalPerson.validate(new ThrowsValidationHandler())
    );
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Invalid CPF - Throws an Exception")
  public void givenInvalidCpf_whenCallValidateANewNaturalPersonAndValidate_thenItShouldThrowsADomainException() {
    final String expectedName ="John Doe";
    final var expectedErrorCount = 1;
    final var cpf = "935.411.347-90";

    final var expectedErrorMessage = "Invalid CPF";

    final var actualNaturalPerson = NaturalPerson.create(expectedName, cpf);

    final var sut = Assertions.assertThrows(
        DomainException.class, () -> actualNaturalPerson.validate(new ThrowsValidationHandler())
    );
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("All Invalid - Throws an Exception")
  public void givenAllInvalidParams_whenCallValidateANewNaturalPersonAndValidate_thenItShouldThrowsADomainException() {
    final var expectedErrorCount = 2;
    final var expectedErrorMessage = "`Name` shouldn't be null";

    final var actualNaturalPerson = NaturalPerson.create(null, null);

    var sut = StackValidationHandler.create();

    actualNaturalPerson.validate(sut);

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Valid Address - Instantiate Natural Person")
  public void givenValidAddressParam_whenCallValidateANewNaturalPerson_thenInstantiateNewNaturalPerson() {
    final var expectedAddressNumber = 1;

    final var name = "John Doe";
    final var cpf = "935.411.347-80";

    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var newAddress = Address
        .create(aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);

    final var sut = NaturalPerson.create(name, cpf);
    sut.addAddress(newAddress);

    sut.validate(new ThrowsValidationHandler());

    Assertions.assertNotNull(sut);
    Assertions.assertNotNull(newAddress);
    Assertions.assertEquals(expectedAddressNumber, sut.getAddresses().size());
  }

  @Test
  @DisplayName("Invalid Address - Throws an Exception")
  public void givenInvalidValidAddressParam_whenCallValidateANewNaturalPerson_thenThrowsNewDomainException() {
    final var expectedAddressNumber = 8;

    final var name = "John Doe";
    final var cpf = "935.411.347-80";
    final var newAddress = Address
        .create(null, null, null, null, null, null, null,  null, null);

    final var currentNaturalPerson = NaturalPerson.create(name, cpf);
    currentNaturalPerson.addAddress(newAddress);

    var sut = StackValidationHandler.create();
    currentNaturalPerson.validate(sut);

    Assertions.assertEquals(expectedAddressNumber, sut.getErrors().size());
  }

  @Test
  @DisplayName("Update Natural Person - Instantiate Natural Person")
  public void givenValidParams_whenUpdateANewNaturalPerson_thenInstantiateNewNaturalPerson() {
    final var expectedName = "John Doe";
    final String expectedCpf = "93541134780";

    final var name = "John Smith";
    final var cpf = "935.411.347-80";

    final var sut = NaturalPerson.create(name, cpf);
    Assertions.assertDoesNotThrow( () -> sut.validate(new ThrowsValidationHandler()));

    final var createdAt = sut.getCreatedAt();
    final var updatedAt = sut.getUpdatedAt();

    sut.update(expectedName, cpf);
    Assertions.assertDoesNotThrow( () -> sut.validate(new ThrowsValidationHandler()));

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedName, sut.getName());
    Assertions.assertEquals(expectedCpf, sut.getCpf().getValue());
    Assertions.assertTrue(sut.getUpdatedAt().isAfter(updatedAt));
    Assertions.assertEquals(createdAt, sut.getCreatedAt());
  }

  @Test
  @DisplayName("Delete Natural Person - Set Deletion Date")
  public void whenCallDeleteANewNaturalPerson_thenSetADeletionDate() {
    final var expectedName = "John Doe";
    final String expectedCpf = "93541134780";

    final var name = "John Smith";
    final var cpf = "935.411.347-80";

    final var sut = NaturalPerson.create(name, cpf);
;

    sut.delete();

    Assertions.assertNotNull(sut.getDeletedAt());
  }

}
