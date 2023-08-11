package com.johnnycarreiro.crs.modules.customer.unitary.application.natural_person;

import com.johnnycarreiro.crs.modules.customer.application.natural_person.delete.DefaultDeleteNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@DisplayName("Delete NaturalPerson Use Case")
public class DeleteNaturalPersonUseCaseTest {

  @Test
  @DisplayName("Valid Id - Delete Natural Person")
  public void givenValidId_whenCallDeleteNaturalPerson_thenShouldBeOk() {
    final var aName = "John Doe";
    final  var aCpf = "935.411.347-80";
    NaturalPerson aNaturalPerson = NaturalPerson.create(aName, aCpf);
    final var expectedId = aNaturalPerson.getId();

    final var aStreet = "Logradouro";
    final var aNumber = 100;
    final String aComplement = "";
    final var anArea = "Bairro";
    final var aCity = "Mogi Guaçu";
    final var anState = "SP";
    final var aCep = "00100-000";
    final var anUnitType = "Residential";

    final var anAddress = Address.create(aStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType, expectedId.getValue());

    final var aPhoneNumber = "(12) 9.9720-4431";
    final var anEmail = "john.doe@acme.digital";

    final var aContact = Contact.create(anEmail, aPhoneNumber,anAddress, expectedId);
    aNaturalPerson.addContact(aContact);

    final NaturalPersonGateway personGateway = Mockito.mock(NaturalPersonGateway.class);

    Mockito.when(personGateway.findById(eq(expectedId)))
      .thenReturn(Optional.of(aNaturalPerson));
    final DefaultDeleteNaturalPersonUseCase useCase = new DefaultDeleteNaturalPersonUseCase(personGateway);

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
    aNaturalPerson.delete();
    aNaturalPerson.getContact().delete();
    aNaturalPerson.getContact().getAddresses().forEach(Address::delete);
    Mockito.verify(personGateway, times(1)).update(eq(aNaturalPerson));
  }
}

// TODO: Assert it does throws Exception if there is not found entity by Id
