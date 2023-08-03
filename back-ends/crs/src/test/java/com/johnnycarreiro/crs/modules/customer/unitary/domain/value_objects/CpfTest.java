package com.johnnycarreiro.crs.modules.customer.unitary.domain.value_objects;

import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.value_objects.Cpf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CpfTest {
  @Test
  public void givenAValidCpf_whenCallNewCpf_thenInstantiateANewCpf() {
    final var expectedCpf = "93541134780";

    final var cpf = "935.411.347-80";
    var sut = Cpf.create(cpf);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedCpf, sut.getValue());
  }

  @Test()
  public void givenAInvalidCpf_whenCallNewCpf_thenItShouldReceivesAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Invalid CPF";
    final var cpf = "123.456.789-99";

    final var actualCpf = Cpf.create(cpf);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> actualCpf.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
  @Test
  public void givenAInvalidCpfWithAllEqualDigits_whenCallNewCpf_thenItShouldReceivesAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Invalid CPF";
    final var cpf = "111.111.111-11";

    final var actualCpf = Cpf.create(cpf);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> actualCpf.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
  @Test
  public void givenAInvalidLongCpf_whenCallNewCpf_thenItShouldReceivesAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Invalid CPF";
    final var cpf = "111.111.111-1100";

    final var actualCpf = Cpf.create(cpf);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> actualCpf.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
  @Test
  public void givenAInvalidShortCpf_whenCallNewCpf_thenItShouldReceivesAnException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Invalid CPF";
    final var cpf = "111.111.111-";

    final var actualCpf = Cpf.create(cpf);

    var sut = Assertions.assertThrows(
        DomainException.class, () -> actualCpf.validate(new ThrowsValidationHandler())
    );

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
}
