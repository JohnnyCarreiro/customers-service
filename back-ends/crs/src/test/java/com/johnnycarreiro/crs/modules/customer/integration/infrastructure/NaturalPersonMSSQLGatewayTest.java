package com.johnnycarreiro.crs.modules.customer.integration.infrastructure;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.MSSQLGatewayTest;
import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.NaturalPersonMSSQLGateway;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonRepository;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@MSSQLGatewayTest
@DisplayName("Natural Person Gateway Tests Suit")
public class NaturalPersonMSSQLGatewayTest {

  @Autowired
  private NaturalPersonMSSQLGateway gateway;

  @Autowired
  private NaturalPersonRepository personRepository;

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private AddressRepository addressRepository;

  @BeforeEach
  void cleanUp() {
    personRepository.deleteAll();
  }

  @Test
  @DisplayName("Injections")
  public void testDependencyInjection() {
    Assertions.assertNotNull(gateway);
    Assertions.assertNotNull(personRepository);
  }

  @Test
  @DisplayName("Valid Natural Person - Create new Record")
  public void givenValidNaturalPerson_whenCallCreate_thenCreateNewRecord() {
    final var expectedName = "John Doe";
    final var expectedCpf = "93541134780";
    final var cpf = "935.411.347-80";
    final var aPerson = NaturalPerson.create(expectedName, cpf);

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

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, aPerson.getId());
    aPerson.addContact(aContact);

    Assertions.assertEquals(0, personRepository.count());

    NaturalPerson sut = gateway.create(aPerson);

    Assertions.assertEquals(1, personRepository.count());

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(aPerson.getId().getValue(), sut.getId().getValue());
    Assertions.assertEquals(expectedName, sut.getName());
    Assertions.assertEquals(expectedCpf, sut.getCpf().getValue());
    Assertions.assertEquals(aPerson.getCreatedAt(), sut.getCreatedAt());
    Assertions.assertEquals(aPerson.getUpdatedAt(), sut.getUpdatedAt());
    Assertions.assertEquals(aPerson.getDeletedAt(), sut.getDeletedAt());

    NaturalPersonJpaEntity personEntity =
        this.personRepository.findById(UUID.fromString(aPerson.getId().getValue())).get();

    Assertions.assertNotNull(personEntity.toAggregate().getContact());
    Assertions.assertFalse(personEntity.toAggregate().getContact().getAddresses().isEmpty());
    Assertions.assertEquals(personEntity.getId().toString(), aPerson.getId().getValue());
    Assertions.assertEquals(personEntity.getName(), aPerson.getName());
    Assertions.assertEquals(personEntity.getCpf(), aPerson.getCpf().getValue());
    Assertions.assertEquals(personEntity.getCreatedAt(), aPerson.getCreatedAt());
    Assertions.assertEquals(personEntity.getUpdatedAt(), aPerson.getUpdatedAt());
    Assertions.assertEquals(personEntity.getDeletedAt(), aPerson.getDeletedAt());
  }

  @Test
  @DisplayName("Valid Update Natural Person - Update  Record")
  public void givenValidUpdatedName_whenCallUpdate_thenUpdateRecord() {
    final var expectedName = "John Doe";
    final var expectedCpf = "93541134780";
    final var aName = "John Smith";
    final var cpf = "935.411.347-80";
    final var aPerson = NaturalPerson.create(aName, cpf);

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

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, aPerson.getId());
    this.contactRepository.saveAndFlush(ContactJpaEntity.from(aContact));

    aPerson.addContact(aContact);
    aPerson.update(expectedName, expectedCpf);

    Assertions.assertDoesNotThrow(() -> aPerson.validate(new ThrowsValidationHandler()));
    Assertions.assertEquals(0, personRepository.count());

    NaturalPerson sut = gateway.update(aPerson);

    Assertions.assertEquals(1, personRepository.count());

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(aPerson.getId().getValue(), sut.getId().getValue());
    Assertions.assertEquals(expectedName, sut.getName());
    Assertions.assertEquals(expectedCpf, sut.getCpf().getValue());
    Assertions.assertEquals(aPerson.getCreatedAt(), sut.getCreatedAt());
    Assertions.assertEquals(aPerson.getUpdatedAt(), sut.getUpdatedAt());
    Assertions.assertEquals(aPerson.getDeletedAt(), sut.getDeletedAt());

    NaturalPersonJpaEntity personEntity =
        this.personRepository.findById(UUID.fromString(aPerson.getId().getValue())).get();

    Assertions.assertEquals(personEntity.getId().toString(), aPerson.getId().getValue());
    Assertions.assertEquals(personEntity.getName(), aPerson.getName());
    Assertions.assertEquals(personEntity.getCpf(), aPerson.getCpf().getValue());
    Assertions.assertEquals(personEntity.getCreatedAt(), aPerson.getCreatedAt());
    Assertions.assertEquals(personEntity.getUpdatedAt(), aPerson.getUpdatedAt());
    Assertions.assertEquals(personEntity.getDeletedAt(), aPerson.getDeletedAt());
  }

  @Test
  @DisplayName("Delete Natural Person - Update Record")
  public void givenPersistedNaturalPerson_whenCallDelete_thenUpdateRecordDeletionDate() {
    final var aName = "John Smith";
    final var cpf = "935.411.347-80";
    final var aPerson = NaturalPerson.create(aName, cpf);

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

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, aPerson.getId());
    this.contactRepository.saveAndFlush(ContactJpaEntity.from(aContact));

    aPerson.addContact(aContact);

    Assertions.assertEquals(0, personRepository.count());

    personRepository.saveAndFlush(NaturalPersonJpaEntity.from(aPerson));

    Assertions.assertEquals(1, personRepository.count());
    Assertions.assertNull(aPerson.getDeletedAt());

    aPerson.delete();
    gateway.deleteById(aPerson.getId().getValue());

    NaturalPersonJpaEntity personEntity =
        this.personRepository.findById(UUID.fromString(aPerson.getId().getValue())).get();

    Assertions.assertNotNull(personEntity.getDeletedAt());

  }

  @Test
  @DisplayName("Find by id Natural Person - Returns Record")
  public void givenValidId_whenCallFindById_thenReturnsARecord() {
    final var expectedName = "John Smith";
    final var expectedCpf = "93541134780";

    final var cpf = "935.411.347-80";
    final var aPerson = NaturalPerson.create(expectedName, cpf);

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

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, aPerson.getId());
    this.contactRepository.saveAndFlush(ContactJpaEntity.from(aContact));

    aPerson.addContact(aContact);

    Assertions.assertEquals(0, personRepository.count());

    personRepository.saveAndFlush(NaturalPersonJpaEntity.from(aPerson));

    Assertions.assertEquals(1, personRepository.count());

    NaturalPerson sut = gateway.findById(aPerson.getId()).get();

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(aPerson.getId().getValue(), sut.getId().getValue());
    Assertions.assertEquals(expectedName, sut.getName());
    Assertions.assertEquals(expectedCpf, sut.getCpf().getValue());
    Assertions.assertEquals(aPerson.getCreatedAt(), sut.getCreatedAt());
    Assertions.assertEquals(aPerson.getUpdatedAt(), sut.getUpdatedAt());
    Assertions.assertEquals(aPerson.getDeletedAt(), sut.getDeletedAt());

  }

  @Test
  @DisplayName("Find All Natural Person - Returns Records")
  public void whenCallFindAll_thenReturnsRecords() {
    final var aName = "John Smith";
    final var cpf = "935.411.347-80";
    final var aPerson = NaturalPerson.create(aName, cpf);

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

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, aPerson.getId());
    this.contactRepository.saveAndFlush(ContactJpaEntity.from(aContact));

    aPerson.addContact(aContact);

    List<NaturalPerson> naturalPeople = List.of(aPerson);

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
        SearchQuery.from(null, null, null, null, null);
    final var expectedPagination =
        new Pagination<>(expectedPage, expectedPerPage, naturalPeople.size(), naturalPeople);

    final var expectedItemsCount = 1;

    Assertions.assertEquals(0, personRepository.count());

    personRepository.saveAndFlush(NaturalPersonJpaEntity.from(aPerson));

    Assertions.assertEquals(1, personRepository.count());


    Pagination<NaturalPerson> sut = gateway.findAll(aQuery);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedItemsCount, sut.items().size());
    Assertions.assertEquals(expectedPagination, sut);
    Assertions.assertEquals(expectedPage, sut.currentPage());
    Assertions.assertEquals(expectedPerPage, sut.perPage());
    Assertions.assertEquals(naturalPeople.size(), sut.total());

  }

//  @Test
//  @DisplayName("Valid Address & Natural Person - Create new Records")
//  public void givenValidAddressAndNaturalPerson_whenCallCreate_thenCreateNewRecord() {
//    final var expectedName = "John Doe";
//    final var expectedCpf = "93541134780";
//    final var cpf = "935.411.347-80";
//
//    final var aStreet = "Logradouro";
//    final var aNumber = 100;
//    final var anArea = "Bairro";
//    final var aCity = "Mogi Guaçu";
//    final var expectedCep = "00100-000";
//    final var anState = "SP";
//    final var anUnitType = "Residential";
//
//    final var aPerson = NaturalPerson.create(expectedName, cpf);
//
//    var anAddress = Address
//        .create(aPerson.getId().getValue(), aStreet, aNumber, null, anArea, aCity,  anState, expectedCep, anUnitType);
//
//    aPerson.addAddress(anAddress);
//    Assertions.assertEquals(0, personRepository.count());
//
//    NaturalPerson sut = gateway.create(aPerson, aPerson.getAddresses());
//
//    Assertions.assertEquals(1, personRepository.count());
//
////    Assertions.assertNotNull(sut);
////    Assertions.assertEquals(aPerson.getId().getValue(), sut.getId().getValue());
////    Assertions.assertEquals(expectedName, sut.getName());
////    Assertions.assertEquals(expectedCpf, sut.getCpf().getValue());
////    Assertions.assertEquals(aPerson.getCreatedAt(), sut.getCreatedAt());
////    Assertions.assertEquals(aPerson.getUpdatedAt(), sut.getUpdatedAt());
////    Assertions.assertEquals(aPerson.getDeletedAt(), sut.getDeletedAt());
////
////    NaturalPersonJpaEntity personEntity =
////        this.personRepository.findById(UUID.fromString(aPerson.getId().getValue())).get();
////
////    Assertions.assertEquals(personEntity.getId().toString(), aPerson.getId().getValue());
////    Assertions.assertEquals(personEntity.getName(), aPerson.getName());
////    Assertions.assertEquals(personEntity.getCpf(), aPerson.getCpf().getValue());
////    Assertions.assertEquals(personEntity.getCreatedAt(), aPerson.getCreatedAt());
////    Assertions.assertEquals(personEntity.getUpdatedAt(), aPerson.getUpdatedAt());
////    Assertions.assertEquals(personEntity.getDeletedAt(), aPerson.getDeletedAt());
//  }
}

// TODO: Implement in Create Natural Person methods for create new Natural Person with Addresses;
