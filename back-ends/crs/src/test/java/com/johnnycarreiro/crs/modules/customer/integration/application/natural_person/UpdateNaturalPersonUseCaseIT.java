package com.johnnycarreiro.crs.modules.customer.integration.application.natural_person;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.modules.customer.IntegrationTest;
import com.johnnycarreiro.crs.modules.customer.application.address.UpdateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.UpdateContactCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.UpdateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.UpdateNaturalPersonCommand;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonRepository;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class UpdateNaturalPersonUseCaseIT {

  @Autowired
  private UpdateNaturalPersonUseCase useCase;

  @Autowired
  private NaturalPersonRepository personRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private ContactRepository contactRepository;

  @Test()
  @DisplayName("Valid Command - Update new Natural Person")
  public void givenValidCommand_whenCallExecute_thenUpdateANewNaturalPerson() {
    final var expectedName = "John Doe";
    final var aName = "John Smith";
    final var aCpf = "935.411.347-80";
    final var aPerson = NaturalPerson.create(aName, aCpf);

    final var aStreet = "Logradouro 1";
    final var aNumber = 100;
    final var aComplement = "";
    final var anArea = "Bairro 1";
    final var aCity = "Mogi Guaçu 1";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var anAddress = Address
      .create(aStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType, aPerson.getId().getValue());
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
      aPerson.getId().getValue()
    );

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, aPerson.getId());
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

    aPerson.update(expectedName, aCpf);

    final var aCommand =
      UpdateNaturalPersonCommand.with(aPerson.getId().getValue(), expectedName, aCpf, aContactCmd);

    //TODO: Retrieve Data from Natural Person Repository and check Contact and Address stored data;

    Either<ValidationHandler, Void> sut = useCase.execute(aCommand);
    Assertions.assertNull(sut.get());
  }

  @Test()
  @DisplayName("Invalid name Command - Throws a DomainException")
  public void givenInvalidNameInCommand_whenCallExecute_thenThrowsADomainException() {
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "`Name` shouldn't be null";
    final String expectedName = null;
    final var aName = "John Smith";
    final var aCpf = "935.411.347-80";
    final var aPerson = NaturalPerson.create(aName, aCpf);

    final var aStreet = "Logradouro 1";
    final var aNumber = 100;
    final var aComplement = "";
    final var anArea = "Bairro 1";
    final var aCity = "Mogi Guaçu 1";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";

    final var anAddress = Address
      .create(aStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType, aPerson.getId().getValue());
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
      aPerson.getId().getValue()
    );

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, aPerson.getId());
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

    aPerson.update(expectedName, aCpf, aContact);


    final var aCommand =
      UpdateNaturalPersonCommand.with(aPerson.getId().getValue(), expectedName, aCpf, aContactCmd);

    ValidationHandler sut = useCase.execute(aCommand).getLeft();
    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedErrorCount, sut.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, sut.getErrors().get(0).message());
  }
}
