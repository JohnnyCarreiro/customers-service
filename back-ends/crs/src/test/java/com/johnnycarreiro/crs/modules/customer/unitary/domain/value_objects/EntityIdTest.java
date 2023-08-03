package com.johnnycarreiro.crs.modules.customer.unitary.domain.value_objects;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Entity ID Tests Suite ")
public class EntityIdTest {

  @Test
  @DisplayName("New - Instantiate new Entity ID")
  public void whenCallCreateNewEntityId_thenInstantiateNewEntityId() {
    var sut = EntityId.create();

    Assertions.assertNotNull(sut);
    Assertions.assertNotNull(sut.getValue());
  }

  @Test
  @DisplayName("Valid ID - Instantiate new Entity ID")
  public void givenAValidId_whenCallFromNewEntityId_thenInstantiateNewEntityId() {
    var expectedId = "cc4eb100-7b02-482f-96ed-c48241648b5d";

    var sut = EntityId.from(expectedId);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedId, sut.getValue());
  }

  @Test
  @DisplayName("Invalid ID - Throws an Exception")
  public void givenAnInvalidValidId_whenCallFromNewEntityId_thenThrowsNewDomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`ID` is Invalid";
    String id = null;

    var currentEntityId = EntityId.from(id);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> currentEntityId.validate(new ThrowsValidationHandler())
    );

    Assertions.assertNotNull(currentEntityId);
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
}
