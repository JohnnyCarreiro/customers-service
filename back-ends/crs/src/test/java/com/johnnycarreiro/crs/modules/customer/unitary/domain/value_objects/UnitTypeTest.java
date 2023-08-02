package com.johnnycarreiro.crs.modules.customer.unitary.domain.value_objects;

import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Type Test Suite")
public class UnitTypeTest {

  @Test
  @DisplayName("Label - Instantiate an  Unit Type")
  public void givenAValidUnitTypeLabel_whenCallNewUnitType_thenInstantiateNewUnitType() {
    var label = "Residential";
    var expectedunitType = UnitType.RESIDENTIAL;

    var sut = UnitType.from(label);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedunitType, sut);
  }

  @Test
  @DisplayName("Value - Create a valid Unit Type")
  public void givenAValidUnitTypeValue_whenCallNewUnitType_thenInstantiateANewUnitType() {
    var expectedUnitType = UnitType.COMMERCIAL;
    var rawUnitType = 2;
    var sut = UnitType.from(rawUnitType);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedUnitType, sut);
  }

  @Test
  @DisplayName("Empty - Throws an Exception")
  public void givenInvalidEmptyUnitType_whenCallNewUnitType_thenItShouldThrowsAnException() {
    var expectedErrorMessage = "`Unit Type` is Invalid";
    var expectedErrorCount = 1;
    String rawUnitType = "";
    var actualUnitType = UnitType.from(rawUnitType);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> actualUnitType.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Null - Throws an Exception")
  public void givenInvalidNullUnitType_whenCallNewUnitType_thenItShouldThrowsAnException() {
    var expectedErrorMessage = "`Unit Type` is Invalid";
    var expectedErrorCount = 1;
    var actualUnitType = UnitType.from(null);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> actualUnitType.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
}
