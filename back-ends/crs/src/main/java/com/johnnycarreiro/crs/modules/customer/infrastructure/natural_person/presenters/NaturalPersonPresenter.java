package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.presenters;

import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonOutput;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.AddressAPIResponse;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.ContactAPIResponse;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.NaturalPersonAPIResponse;

import java.util.List;
import java.util.function.Function;

public interface NaturalPersonPresenter {

  Function<GetNaturalPersonOutput, NaturalPersonAPIResponse> present = output -> {
    Contact contact = output.contact();
    List<Address> addresses = contact.getAddresses();
    final var  addressesResponse = addresses.stream().map(adr -> new AddressAPIResponse(
      adr.getId().getValue(),
      adr.getStreet(),
      adr.getNumber(),
      adr.getComplement(),
      adr.getArea(),
      adr.getCity(),
      adr.getState().getAcronym(),
      adr.getCep(),
      adr.getUnitType().getLabel(),
      adr.getCustomerId().getValue(),
      adr.getCreatedAt(),
      adr.getUpdatedAt(),
      adr.getDeletedAt()
    )).toList();
    final var contactResponse = new ContactAPIResponse(
      contact.getId().getValue(),
      contact.getPhoneNumber(),
      contact.getEmail(),
      addressesResponse,
      contact.getCustomerId().getValue(),
      contact.getCreatedAt(),
      contact.getUpdatedAt(),
      contact.getDeletedAt()
    );
    return new NaturalPersonAPIResponse(
      output.id().getValue(),
      output.name(),
      output.cpf().getValue(),
      contactResponse,
      output.createdAt(),
      output.updatedAt(),
      output.deletedAt()
    );
  };
  static NaturalPersonAPIResponse present(GetNaturalPersonOutput output) {
    Contact contact = output.contact();
    List<Address> addresses = contact.getAddresses();
    final var  addressesResponse = addresses.stream().map(adr -> new AddressAPIResponse(
      adr.getId().getValue(),
      adr.getStreet(),
      adr.getNumber(),
      adr.getComplement(),
      adr.getArea(),
      adr.getCity(),
      adr.getState().getAcronym(),
      adr.getCep(),
      adr.getUnitType().getLabel(),
      adr.getCustomerId().getValue(),
      adr.getCreatedAt(),
      adr.getUpdatedAt(),
      adr.getDeletedAt()
    )).toList();
    final var contactResponse = new ContactAPIResponse(
      contact.getId().getValue(),
      contact.getPhoneNumber(),
      contact.getEmail(),
      addressesResponse,
      contact.getCustomerId().getValue(),
      contact.getCreatedAt(),
      contact.getUpdatedAt(),
      contact.getDeletedAt()
    );
    return new NaturalPersonAPIResponse(
      output.id().getValue(),
      output.name(),
      output.cpf().getValue(),
      contactResponse,
      output.createdAt(),
      output.updatedAt(),
      output.deletedAt()
    );
  }
}
