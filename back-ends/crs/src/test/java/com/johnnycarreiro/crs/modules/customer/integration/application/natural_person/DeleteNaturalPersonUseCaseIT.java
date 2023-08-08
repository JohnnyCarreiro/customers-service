package com.johnnycarreiro.crs.modules.customer.integration.application.natural_person;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.exceptions.NotFoundException;
import com.johnnycarreiro.crs.modules.customer.IntegrationTest;
import com.johnnycarreiro.crs.modules.customer.application.address.UpdateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.UpdateContactCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.delete.DeleteNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonOutput;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@IntegrationTest
public class DeleteNaturalPersonUseCaseIT {

  @Autowired
  private DeleteNaturalPersonUseCase useCase;

  @Autowired
  private NaturalPersonRepository personRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private ContactRepository contactRepository;

  @Test
  @DisplayName("Valid Id - Delete Natural Person")
  public void givenValidId_whenCallDeleteNaturalPerson_thenShouldBeOk() {
    final var aName = "John Smith";
    final var aCpf = "935.411.347-80";
    final var aPerson = NaturalPerson.create(aName, aCpf);
    final var aPersonId = aPerson.getId();

    final var expectedCpf = "93541134780";
    final var expectedName = "John Smith";
    final var expectedId = aPerson.getId();
    final var expectedCreateAt = aPerson.getCreatedAt();
    final var expectedUpdatedAt = aPerson.getUpdatedAt();

    final var aStreet = "Logradouro 1";
    final var aNumber = 100;
    final String aComplement = null;
    final var anArea = "Bairro 1";
    final var aCity = "Mogi Guaçu 1";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var anAddress = Address
      .create(aStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType, aPersonId.getValue());
    this.addressRepository.saveAndFlush(AddressJpaEntity.from(anAddress));
    final var anAddressCmd = UpdateAddressCommand.with(
      anAddress.getId().getValue(),
      aStreet,
      aNumber,
      aComplement,
      anArea,
      aCity,
      anState,
      aCep,
      anUnitType,
      aPersonId.getValue()
    );

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, aPersonId);
    this.contactRepository.saveAndFlush(ContactJpaEntity.from(aContact));
    final var aContactCmd = UpdateContactCommand.with(
      aContact.getId().getValue(),
      aPhoneNumber,
      anEmail,
      anAddressCmd,
      aPerson.getId().getValue()
    );

    aPerson.addContact(aContact);
    this.personRepository.saveAndFlush(NaturalPersonJpaEntity.from(aPerson));

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
  }
}

// TODO: Assert it does throws Exception if there is not found entity by Id

