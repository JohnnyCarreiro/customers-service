package com.johnnycarreiro.crs.modules.customer.unitary.application.natural_person;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.modules.customer.application.address.UpdateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.UpdateContactCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.DefaultUpdateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.UpdateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.UpdateNaturalPersonCommand;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@DisplayName("Create Natural Person Use Case Tests Suite")
public class UpdateNaturalPersonUseCaseTest {
  @Test()
  @DisplayName("Valid Command - Update new Natural Person")
  public void givenValidCommand_whenCallExecute_thenUpdateANewNaturalPerson() {
    final var name = "John Smith";
    final var cpf = "935.411.347-80";
    final var actualPerson = NaturalPerson.create(name, cpf);

    final var expectedId = actualPerson.getId();
    final var expectedName = "John Doe";
    final var expectedCpf = "93541134780";
    final var expectedCreatedAt = actualPerson.getCreatedAt();
    final var updatedAt = actualPerson.getUpdatedAt();

    final var anAddressId = "811f4d7b-ab17-4d7a-8911-295dd09276c4";
    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final String aComplement = null;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var anState = "SP";
    final var aCep = "00100-000";
    final var anUnitType = "Residential";

    final var anAddresses = List.of(
      UpdateAddressCommand.with(
        anAddressId,
        aStreet,
        aNumber,
        aComplement,
        anArea,
        aCity,
        anState,
        aCep,
        anUnitType,
        actualPerson.getId().getValue()
      )
    );

    final var aContactId = "b701c0fb-42f6-46ca-83a0-5a38bc0694a8";
    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";

    final var aContactCmd =
      UpdateContactCommand.with(aContactId, aPhoneNumber, anEmail, anAddresses, expectedId.getValue());

    final var aCommand = UpdateNaturalPersonCommand
        .with(expectedId.getValue(), expectedName, actualPerson.getCpf().getValue(), aContactCmd);

    final NaturalPersonGateway personGateway = Mockito.mock(NaturalPersonGateway.class);
    Mockito.when(personGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(NaturalPerson.with(actualPerson)));
    Mockito.when(personGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final UpdateNaturalPersonUseCase useCase = new DefaultUpdateNaturalPersonUseCase(personGateway);
    Either<ValidationHandler, Void> sut = useCase.execute(aCommand);

    Mockito.verify(personGateway, times(1)).findById(eq(expectedId));

    Assertions.assertNull(sut.get());
    Mockito.verify(personGateway, times(1))
        .update(Mockito.argThat(anUpdatedPerson -> {
          return Objects.equals(expectedName, anUpdatedPerson.getName())
                 && Objects.equals(expectedCpf, anUpdatedPerson.getCpf().getValue())
                 && Objects.equals(expectedId, anUpdatedPerson.getId())
                 && Objects.equals(expectedCreatedAt, anUpdatedPerson.getCreatedAt())
                 && anUpdatedPerson.getUpdatedAt().isAfter(updatedAt);
        }));
  }

  @Test()
  @DisplayName("Invalid name Command - Update new Natural Person")
  public void givenInvalidNameInCommand_whenCallExecute_thenThrowsADomainException() {
    final var expectedErrorMessage = "`Name` shouldn't be null";
    final var expectedErrorCount = 1;

    final var name = "John Smith";
    final var cpf = "935.411.347-80";
    final var actualPerson = NaturalPerson.create(name, cpf);

    final var aPersonId = actualPerson.getId();
    final var expectedCpf = "93541134780";
    final var expectedCreatedAt = actualPerson.getCreatedAt();
    final var updatedAt = actualPerson.getUpdatedAt();

    final var anAddressId = "811f4d7b-ab17-4d7a-8911-295dd09276c4";
    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final String aComplement = null;
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var anState = "SP";
    final var aCep = "00100-000";
    final var anUnitType = "Residential";

    final var anAddresses = List.of(
      UpdateAddressCommand.with(
        anAddressId,
        aStreet,
        aNumber,
        aComplement,
        anArea,
        aCity,
        anState,
        aCep,
        anUnitType,
        actualPerson.getId().getValue()
      )
    );

    final var aContactId = "b701c0fb-42f6-46ca-83a0-5a38bc0694a8";
    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";

    final var aContactCmd =
      UpdateContactCommand.with(aContactId, aPhoneNumber, anEmail, anAddresses, aPersonId.getValue());

    final String expectedName = null;
    final var aCommand = UpdateNaturalPersonCommand
      .with(aPersonId.getValue(), expectedName, actualPerson.getCpf().getValue(),aContactCmd);

    final NaturalPersonGateway personGateway = Mockito.mock(NaturalPersonGateway.class);
    Mockito.when(personGateway.findById(eq(aPersonId)))
        .thenReturn(Optional.of(NaturalPerson.with(actualPerson)));

    final UpdateNaturalPersonUseCase useCase = new DefaultUpdateNaturalPersonUseCase(personGateway);
    ValidationHandler sut = useCase.execute(aCommand).getLeft();

    Mockito.verify(personGateway, times(1)).findById(eq(aPersonId));

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }

}
