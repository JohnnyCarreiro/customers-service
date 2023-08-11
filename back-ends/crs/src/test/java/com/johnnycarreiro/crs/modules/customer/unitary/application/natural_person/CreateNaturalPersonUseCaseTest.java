package com.johnnycarreiro.crs.modules.customer.unitary.application.natural_person;

import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.modules.customer.application.address.CreateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.CreateContactCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.DefaultCreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@DisplayName("Create Natural Person Use Case Tests Suite")
public class CreateNaturalPersonUseCaseTest {

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
    final NaturalPersonGateway personGateway = Mockito.mock(NaturalPersonGateway.class);

    Mockito.when(personGateway.create(any()))
        .thenAnswer(returnsFirstArg());
    final CreateNaturalPersonUseCase useCase = new DefaultCreateNaturalPersonUseCase(personGateway);

    Either<StackValidationHandler, Void> sut = useCase.execute(aCommand);

    Assertions.assertNull(sut.get());
    Mockito.verify(personGateway, times(1))
        .create(Mockito.argThat(aPerson -> {
          return Objects.equals(expectedName, aPerson.getName())
              && Objects.equals(expectedCpf, aPerson.getCpf().getValue())
              && Objects.nonNull(aPerson.getId())
              && Objects.nonNull(aPerson.getCreatedAt())
              && Objects.nonNull(aPerson.getUpdatedAt());
        }));
  }

  @Test()
  @DisplayName("Invalid Name Command - Throw an Exception")
  public void givenInvalidName_whenCallExecute_thenItShouldTrowsADomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Name` shouldn't be null";

    final var cpf = "935.411.347-80";

    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final String aComplement = "";
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

    final var aCommand = CreateNaturalPersonCommand.with(null, cpf, anContactCmd);
    final NaturalPersonGateway personGateway = Mockito.mock(NaturalPersonGateway.class);
    final CreateNaturalPersonUseCase useCase = new DefaultCreateNaturalPersonUseCase(personGateway);

    StackValidationHandler sut = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
    Mockito.verify(personGateway, times(0)).create(any());
  }

  @Test()
  @DisplayName("Gateway throws - Throw an Exception")
  public void givenInvalidName_whenNaturalPersonGatewayThrowsAnException_thenItShouldTrowsADomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Gateway Unexpected Error";
    final var expectedName = "John Doe";
    final var expectedCpf = "93541134780";

    final var aCpf = "935.411.347-80";

    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final String aComplement = "";
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

    final var aCommand = CreateNaturalPersonCommand.with(expectedName, aCpf, anContactCmd);
    final NaturalPersonGateway personGateway = Mockito.mock(NaturalPersonGateway.class);
    Mockito.when(personGateway.create(any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));
    final CreateNaturalPersonUseCase useCase = new DefaultCreateNaturalPersonUseCase(personGateway);

    StackValidationHandler sut = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
    Mockito.verify(personGateway, times(1))
        .create(Mockito.argThat(aPerson -> {
          return Objects.equals(expectedName, aPerson.getName())
                 && Objects.equals(expectedCpf, aPerson.getCpf().getValue())
                 && Objects.nonNull(aPerson.getId())
                 && Objects.nonNull(aPerson.getCreatedAt())
                 && Objects.nonNull(aPerson.getUpdatedAt());
        }));
  }
}
