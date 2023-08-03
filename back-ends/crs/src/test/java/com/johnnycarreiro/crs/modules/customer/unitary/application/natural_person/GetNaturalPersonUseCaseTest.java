package com.johnnycarreiro.crs.modules.customer.unitary.application.natural_person;

import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.DefaultGetNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonOutput;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@DisplayName("Get Natural Person UC Tests Suite")
public class GetNaturalPersonUseCaseTest {

  @Test
  @DisplayName("Valid Id - Returns Entity Output")
  public void givenValidId_whenCallGetNaturalPerson_thenItShouldReturnsANaturalPersonOutPut() {
    final var aName = "John Doe";
    final var aCpf = "935.411.347-80";
    NaturalPerson aNaturalPerson = NaturalPerson.create(aName, aCpf);
    final var expectedName = "John Doe";
    final var expectedCpf = "93541134780";
    final var expectedId = aNaturalPerson.getId();
    final var expectedCreateAt = aNaturalPerson.getCreatedAt();
    final var expectedUpdatedAt = aNaturalPerson.getUpdatedAt();

    final NaturalPersonGateway personGateway = Mockito.mock(NaturalPersonGateway.class);

    Mockito.when(personGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(aNaturalPerson));

    final var useCase = new DefaultGetNaturalPersonUseCase(personGateway);
    GetNaturalPersonOutput sut = useCase.execute(expectedId.getValue());

    Assertions.assertNotNull(sut);
    Mockito.verify(personGateway, times(1))
        .findById(Mockito.any());
    Assertions.assertEquals(expectedName, sut.name());
    Assertions.assertEquals(expectedCpf, sut.cpf().getValue());
    Assertions.assertEquals(expectedId, sut.id());
    Assertions.assertEquals(expectedCreateAt, sut.createdAt());
    Assertions.assertEquals(expectedUpdatedAt, sut.updatedAt());
  }
}
