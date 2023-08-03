package com.johnnycarreiro.crs.modules.customer.unitary.domain.value_objects;

import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.State;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Address Tests Suite")
public class AddressTest {

  @Test
  @DisplayName("Address - Instantiate an  Address")
  public void givenAValidAddress_whenCallNewAddress_thenInstantiateNewAddress() {
    final var expectedCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var expectedStreet = "Logradouro";
    final var expectedNumber = 100;
    final String expectedComplement = null;
    final var expectedArea = "Bairro";
    final var expectedCity = "Mogi Guaçu";
    final var expectedState = State.SAO_PAULO;
    final var expectedCep = "00100-000";
    final var expectedUnitType =  UnitType.RESIDENTIAL;

    final var rawState = "SP";
    final var rawUnitType = "Residential";

    final var sut = Address.create(
        expectedStreet,
        expectedNumber,
        expectedComplement,
        expectedArea,
        expectedCity,
        rawState,
        expectedCep,
        rawUnitType,
        expectedCustomerId
    );

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedCustomerId, sut.getCustomerId().getValue());
    Assertions.assertEquals(expectedStreet, sut.getStreet());
    Assertions.assertEquals(expectedNumber, sut.getNumber());
    Assertions.assertEquals(expectedComplement, sut.getComplement());
    Assertions.assertEquals(expectedArea, sut.getArea());
    Assertions.assertEquals(expectedCity, sut.getCity());
    Assertions.assertEquals(expectedState, sut.getState());
    Assertions.assertEquals(expectedCep, sut.getCep());
    Assertions.assertEquals(expectedUnitType, sut.getUnitType());
  }

  @Test
  @DisplayName("Validate - Instantiate a Valid Address")
  public void givenAValidAddress_whenCallNewAddressValidate_thenInstantiateAValidNewAddress() {
    final var expectedErrorCount = 0;
    final List<?> expectedErrors = List.of();

    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var currentAddress = Address
        .create(aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);

    final var sut = StackValidationHandler.create();
    currentAddress.validate(sut);

    Assertions.assertNotNull(currentAddress);
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrors, sut.getErrors());
  }

  @Test
  @DisplayName("Null Street - Throws an Exception")
  public void givenAnInvalidNullAddressStreet_whenCallNewAddressValidate_thenItShouldThrowsAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Street` shouldn't be Null";
    final String aStreet = null;
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var currentAddress = Address
        .create(aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);

    final var sut = Assertions.assertThrows(
        DomainException.class,() ->  currentAddress.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Empty Street - Throws an Exception")
  public void givenAnInvalidEmptyAddressStreet_whenCallNewAddressValidate_thenItShouldThrowsAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Street` shouldn't be Empty";

    final var aStreet = "   ";
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var currentAddress = Address
        .create(aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);
    final var sut = Assertions.assertThrows(
        DomainException.class,() ->  currentAddress.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Short Street - Throws an Exception")
  public void givenAnInvalidAddressStreetLengthShorterThan2Chars_whenCallNewAddressValidate_thenItShouldThrowsAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Street` must have at least two and less than 255 characters";
    final String aStreet = "u ";
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var currentAddress = Address
        .create(aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);


    final var sut = Assertions.assertThrows(
        DomainException.class, () -> currentAddress.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Long Street - Throws an Exception")
  public void givenAnInvalidAddressStreetLengthGraderThan255Chars_whenCallNewAddressValidate_thenItShouldThrowsAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Street` must have at least two and less than 255 characters";
    final String aStreet =  """
          Mussum Ipsum, cacilds vidis litro abertis. Posuere libero varius. Nullam a nisl ut ante blandit hendrerit.
          Aenean sit amet nisi.Si u mundo tá muito paradis?
          Toma um mé que o mundo vai girarzis!Quem num gosta di mim que vai caçá sua turmis!
          Nullam volutpat risus nec leo commodo, ut interdum diam laoreet. Sed non consequat odio.
        """;
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var currentAddress = Address
        .create(aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);

    final var sut = Assertions.assertThrows(
        DomainException.class, () -> currentAddress.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Null Street - Throws an Exception")
  public void givenAnInvalidNullAddressCity_whenCallNewAddressValidate_thenItShouldThrowsAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`City` shouldn't be Null";
    final var  aStreet = "Logradouro";
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final String aCity = null;
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var currentAddress = Address
      .create(aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);

    final var sut = Assertions.assertThrows(
      DomainException.class,() ->  currentAddress.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Empty Complement - Throws an Exception")
  public void givenAnInvalidEmptyAddressComplement_whenCallNewAddressValidate_thenItShouldThrowsAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Complement` is Invalid";
    final String aComplement = "  ";
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var currentAddress = Address
        .create(aStreet, aNumber, aComplement, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);

    final var sut = Assertions.assertThrows(
        DomainException.class, () -> currentAddress.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Long Complement - Throws an Exception")
  public void givenAnInvalidAddressComplementLengthGraterThan255Chars_whenCallNewAddressValidate_thenItShouldThrowsAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Complement` must have at least one and less than 255 characters";
    final String aComplement = """
          Mussum Ipsum, cacilds vidis litro abertis. Posuere libero varius. Nullam a nisl ut ante blandit hendrerit.
          Aenean sit amet nisi.Si u mundo tá muito paradis?
          Toma um mé que o mundo vai girarzis!Quem num gosta di mim que vai caçá sua turmis!
          Nullam volutpat risus nec leo commodo, ut interdum diam laoreet. Sed non consequat odio.
        """;
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var currentAddress = Address
        .create(aStreet, aNumber, aComplement, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);

    final var sut = Assertions.assertThrows(
        DomainException.class, () -> currentAddress.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("All Invalid - Throws an Exception")
  public void givenAllInvalidAddressStreetParams_whenCallNewAddressValidate_thenItShouldThrowsAnException() {
    final var expectedErrorCount = 9;
    final var expectedErrorMessage = "`Street` shouldn't be Null";
    final String aCustomerId = null;
    final String aStreet = null;
    final Integer aNumber = null;
    final String aComplement = "  ";
    final String anArea = null;
    final String aCity = null;
    final String expectedCep = null;
    final String anState = null;
    final String anUnitType = null;

    final var currentAddress = Address
        .create(aStreet, aNumber, aComplement, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);

    var sut = StackValidationHandler.create();
    currentAddress.validate(sut);

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Delete Address - Set Deletion Date")
  public void whenCallDeleteAddressValidate_thenItShouldSetDeletionDate() {
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";
    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var expectedCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var sut = Address
        .create(aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType, aCustomerId);
    sut.delete();

    Assertions.assertNotNull(sut.getDeletedAt());
  }

  @Test
  @DisplayName("Update Address - Set Deletion Date")
  public void whenCallUpdateAddressValidate_thenItShouldSetDeletionDate() {
    final var aStreet = "Logradouro 1";
    final var aNumber = 100;
    final String aComplement = null;
    final var anArea = "Bairro 1";
    final var aCity = "Mogi Guaçu 1";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";

    final var sut = Address
      .create(aStreet, aNumber, aComplement, anArea, aCity,  anState, aCep, anUnitType, aCustomerId);

    final var updatedAt = sut.getUpdatedAt();

    final var expectedStreet = "Logradouro 2";
    final var expectedNumber = 200;
    final var expectedComplement = "B";
    final var expectedArea = "Bairro";
    final var expectedCity = "Mogi Guaçu";
    final var expectedState = State.DISTRITO_FEDERAL;
    final var expectedCep = "00200-000";
    final var expectedUnitType =  UnitType.COMMERCIAL;
    final var expectedCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";

    final var rawState = "DF";
    final var rawUnitType = "Commercial";

    sut.update(
      expectedStreet,
      expectedNumber,
      expectedComplement,
      expectedArea,
      expectedCity,
      rawState,
      expectedCep,
      rawUnitType,
      expectedCustomerId
    );

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedStreet, sut.getStreet());
    Assertions.assertEquals(expectedNumber, sut.getNumber());
    Assertions.assertEquals(expectedComplement, sut.getComplement());
    Assertions.assertEquals(expectedArea, sut.getArea());
    Assertions.assertEquals(expectedCity, sut.getCity());
    Assertions.assertEquals(expectedState, sut.getState());
    Assertions.assertEquals(expectedCep, sut.getCep());
    Assertions.assertEquals(expectedUnitType, sut.getUnitType());
    Assertions.assertEquals(expectedCustomerId, sut.getCustomerId().getValue());
    Assertions.assertTrue(sut.getUpdatedAt().isAfter(updatedAt));
  }
}
