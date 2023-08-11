package com.johnnycarreiro.crs.modules.customer.integration.application.natural_person;

import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.modules.customer.IntegrationTest;
import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.modules.customer.application.address.CreateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.CreateContactCommand;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonRepository;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class CreateNaturalPersonUseCaseIT {

  @Autowired
  private CreateNaturalPersonUseCase useCase;
  @Autowired
  private NaturalPersonRepository personRepository;
  @Test()
  @DisplayName("Valid Command - Create new Natural Person")
  public void givenValidCommand_whenCallExecute_thenCreateANewNaturalPerson() {
    final var expectedName = "John Doe";
    final var expectedCpf = "93541134780";
    final var cpf = "935.411.347-80";

    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final var aComplement = "";
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var anEmail = "john.doe@acme.com";
    final var aPhoneNumber = "(12) 99720-4431";

    final var anAddressCmd =
      CreateAddressCommand.with(aStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType);

    final var anContactCmd =
      CreateContactCommand.with(anEmail, aPhoneNumber, List.of(anAddressCmd));

    final var aCommand =
      CreateNaturalPersonCommand.with(expectedName, cpf, anContactCmd);

    Assertions.assertEquals(0, personRepository.count());

    Either<StackValidationHandler, Void> sut = useCase.execute(aCommand);

    Assertions.assertEquals(1, personRepository.count());
    Assertions.assertNull(sut.get());
  }

  @Test()
  @DisplayName("Invalid Name - Throws DomainException")
  public void givenInvalidName_whenCallExecute_thenThrowADomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Name` shouldn't be null";

    final String aName = null;
    final var cpf = "935.411.347-80";

    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final var aComplement = "";
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var anEmail = "john.doe@acme.com";
    final var aPhoneNumber = "(12) 99720-4431";

    final var anAddressCmd =
      CreateAddressCommand.with(aStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType);

    final var anContactCmd =
      CreateContactCommand.with(anEmail, aPhoneNumber, List.of(anAddressCmd));

    final var aCommand =
      CreateNaturalPersonCommand.with(aName, cpf, anContactCmd);

    Assertions.assertEquals(0, personRepository.count());

    StackValidationHandler sut = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(0, personRepository.count());
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
}
