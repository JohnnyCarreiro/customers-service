package com.johnnycarreiro.crs.modules.customer.unitary.domain.value_objects;

import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("State Test Suite")
public class StateTest {

  @Test
  @DisplayName("Acronym - Instantiate a State")
  public void givenAValidStateAcronym_whenCallNewState_thenInstantiateANewState() {
    var expectedState = State.SAO_PAULO;
    var rawState = "sp";
    var sut = State.from(rawState);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedState, sut);
  }

  @Test
  @DisplayName("Full Name - Instantiate a State")
  public void givenAValidStateName_whenCallNewState_thenInstantiateANewState() {
    var expectedState = State.SAO_PAULO;
    var rawState = "São Paulo";
    var sut = State.from(rawState);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedState, sut);
  }

  @Test
  @DisplayName("Empty - Throws an Exception ")
  public void givenInvalidEmptyState_whenCallNewState_thenItShouldThrowsAnException() {
    var expectedErrorMessage = "`State` is Invalid";
    var expectedErrorCount = 1;
    String rawState = "";
    var actualState = State.from(rawState);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> actualState.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

  @Test
  @DisplayName("Null - Throws an Exception ")
  public void givenInvalidNullState_whenCallNewState_thenItShouldThrowsAnException() {
    var expectedErrorMessage = "`State` is Invalid";
    var expectedErrorCount = 1;
    var actualState = State.from(null);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> actualState.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
}
