package com.johnnycarreiro.crs.modules.customer.integration.infrastructure;

import com.johnnycarreiro.crs.core.domain.validation.ThrowsValidationHandler;
import com.johnnycarreiro.crs.modules.customer.MSSQLGatewayTest;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.AddressMSSQLGateway;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@MSSQLGatewayTest()
@DisplayName("Address Gateway Tests Suite")
public class AddressMSSQLGatewayTest {

  @Autowired
  private AddressMSSQLGateway addressGateway;

  @Autowired
  private AddressRepository addressRepository;

  @BeforeEach
  void cleanUp() {
    addressRepository.deleteAll();
  }

  @Test
  @DisplayName("Injections")
  public void testDependencyInjection() {
    Assertions.assertNotNull(addressGateway);
    Assertions.assertNotNull(addressRepository);
  }

  @Test
  @DisplayName("Valid Update Address - Update  Record")
  public void givenValidUpdatedName_whenCallUpdate_thenUpdateRecord() {
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

    Assertions.assertDoesNotThrow(() -> anAddress.validate(new ThrowsValidationHandler()));
    Assertions.assertEquals(0, addressRepository.count());

    final var updatedAt = anAddress.getUpdatedAt();

    final var expectedStreet = "Logradouro 2";

    anAddress.update( expectedStreet, aNumber, aComplement, anArea, aCity, anState, aCep, anUnitType, aCustomerId);
    anAddress.validate(new ThrowsValidationHandler());

    Address sut = addressGateway.update(anAddress);

    Assertions.assertEquals(1, addressRepository.count());

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(anAddress.getId().getValue(), sut.getId().getValue());
    Assertions.assertEquals(expectedStreet, sut.getStreet());
    Assertions.assertEquals(anAddress.getCreatedAt(), sut.getCreatedAt());
    Assertions.assertTrue(sut.getUpdatedAt().isAfter(updatedAt));
    Assertions.assertEquals(anAddress.getDeletedAt(), sut.getDeletedAt());

    AddressJpaEntity addressEntity =
      this.addressRepository.findById(UUID.fromString(anAddress.getId().getValue())).get();

    Assertions.assertEquals(addressEntity.getId().toString(), anAddress.getId().getValue());
    Assertions.assertEquals(addressEntity.getStreet(), anAddress.getStreet());
    Assertions.assertEquals(addressEntity.getCreatedAt(), anAddress.getCreatedAt());
    Assertions.assertEquals(addressEntity.getUpdatedAt(), anAddress.getUpdatedAt());
    Assertions.assertEquals(addressEntity.getDeletedAt(), anAddress.getDeletedAt());
  }

  @Test
  @DisplayName("Find by id Address - Returns Record")
  public void givenValidId_whenCallFindById_thenReturnsARecord() {
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

    Assertions.assertEquals(0, addressRepository.count());

    addressRepository.saveAndFlush(AddressJpaEntity.from(anAddress));

    Assertions.assertEquals(1, addressRepository.count());

    Address sut = addressGateway.findById(anAddress.getId()).get();

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(anAddress.getId().getValue(), sut.getId().getValue());
    Assertions.assertEquals(anAddress.getCreatedAt(), sut.getCreatedAt());
    Assertions.assertEquals(anAddress.getUpdatedAt(), sut.getUpdatedAt());
    Assertions.assertEquals(anAddress.getDeletedAt(), sut.getDeletedAt());

  }

  @Test
  @DisplayName("Find All Address - Returns Records")
  public void whenCallFindAll_thenReturnsRecords() {
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

    List<Address> anAddresses = List.of(anAddress);

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
      SearchQuery.from(null, null, null, null, null);
    final var expectedPagination =
      new Pagination<>(expectedPage, expectedPerPage, anAddresses.size(), anAddresses);

    final var expectedItemsCount = 1;

    Assertions.assertEquals(0, addressRepository.count());

    addressRepository.saveAndFlush(AddressJpaEntity.from(anAddress));

    Assertions.assertEquals(1, addressRepository.count());


    Pagination<Address> sut = addressGateway.findAll(aQuery);
    System.out.println(sut);

    Assertions.assertNotNull(sut);
    Assertions.assertEquals(expectedItemsCount, sut.items().size());
    Assertions.assertEquals(expectedPagination, sut);
    Assertions.assertEquals(expectedPage, sut.currentPage());
    Assertions.assertEquals(expectedPerPage, sut.perPage());
    Assertions.assertEquals(anAddresses.size(), sut.total());

  }

}
