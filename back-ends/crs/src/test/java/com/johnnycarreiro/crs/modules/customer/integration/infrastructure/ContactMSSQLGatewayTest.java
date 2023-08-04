package com.johnnycarreiro.crs.modules.customer.integration.infrastructure;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.MSSQLGatewayTest;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.ContactMSSQLGateway;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MSSQLGatewayTest()
@DisplayName("Address Gateway Tests Suite")
public class ContactMSSQLGatewayTest {

  @Autowired
  private ContactMSSQLGateway contactGateway;

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private AddressRepository addressRepository;

  @BeforeEach
  void cleanUp() {
    contactRepository.deleteAll();
  }

  @Test
  @DisplayName("Injections")
  public void testDependencyInjection() {
    Assertions.assertNotNull(contactGateway);
    Assertions.assertNotNull(contactRepository);
  }

  @Test
  @DisplayName("Valid Update Contact - Update  Record")
  public void givenValidUpdatedName_whenCallUpdate_thenUpdateRecord() {
    final var expectedEmail = "john.smith@acme.com";
    final var expectedStreet = "Logradouro 2";

    final var aStreet = "Logradouro 1";
    final var aNumber = 100;
    final String aComplement = null;
    final var anArea = "Bairro 1";
    final var aCity = "Mogi Guaçu 1";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";

    final var anAddress = Address
      .create(aStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType, aCustomerId);
    addressRepository.save(AddressJpaEntity.from(anAddress)); // TODO: Check if in save method on ContactGateway impl, this line is not needed with Transactional annotation

    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    Contact aContact = Contact.create(aPhoneNumber, anEmail, anAddress, EntityId.from(aCustomerId));

    Assertions.assertEquals(0, contactRepository.count());
    contactRepository.save(ContactJpaEntity.from(aContact));

    Address updatedAddress = anAddress.update(expectedStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType, aCustomerId);
    aContact.update(aPhoneNumber, expectedEmail, updatedAddress, EntityId.from(aCustomerId));

    Contact sut = contactGateway.update(aContact);

    Assertions.assertEquals(1, contactRepository.count()); // TODO: Looking for why this method throws

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedEmail, sut.getEmail());

    List<Address> updatedAddresses = sut.getAddresses();
    Assertions.assertEquals(expectedStreet, updatedAddresses.get(0).getStreet());
  }

  @Test
  @DisplayName("Find by id Address - Returns Record")
  public void givenValidId_whenCallFindById_thenReturnsARecord() {
    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";

    Contact aContact = Contact.create(aPhoneNumber, anEmail, EntityId.from(aCustomerId));

    Assertions.assertEquals(0, contactRepository.count());

    contactRepository.saveAndFlush(ContactJpaEntity.from(aContact));

    Assertions.assertEquals(1, contactRepository.count());

    Contact sut = contactGateway.findById(aContact.getId()).get();

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(aContact.getId().getValue(), sut.getId().getValue());
    Assertions.assertEquals(aContact.getCreatedAt(), sut.getCreatedAt());
    Assertions.assertEquals(aContact.getUpdatedAt(), sut.getUpdatedAt());
    Assertions.assertEquals(aContact.getDeletedAt(), sut.getDeletedAt());

  }

  @Test
  @DisplayName("Find All Address - Returns Records")
  public void whenCallFindAll_thenReturnsRecords() {
    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    final var aCustomerId = "cc4eb100-7b02-482f-96ed-c48241648b5d";

    Contact aContact = Contact.create(aPhoneNumber, anEmail, EntityId.from(aCustomerId));

    List<Contact> aContacts = List.of(aContact);

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
      SearchQuery.from(null, null, null, null, null);
    final var expectedPagination =
      new Pagination<>(expectedPage, expectedPerPage, aContacts.size(), aContacts);

    final var expectedItemsCount = 1;

    Assertions.assertEquals(0, contactRepository.count());

    contactRepository.saveAndFlush(ContactJpaEntity.from(aContact));

    Assertions.assertEquals(1, contactRepository.count());


    Pagination<Contact> sut = contactGateway.findAll(aQuery);
    System.out.println(sut);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedItemsCount, sut.items().size());
    Assertions.assertEquals(expectedPagination, sut);
    Assertions.assertEquals(expectedPage, sut.currentPage());
    Assertions.assertEquals(expectedPerPage, sut.perPage());
    Assertions.assertEquals(aContacts.size(), sut.total());

  }

}
