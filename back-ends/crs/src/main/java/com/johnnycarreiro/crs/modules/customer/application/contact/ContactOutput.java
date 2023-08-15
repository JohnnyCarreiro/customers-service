package com.johnnycarreiro.crs.modules.customer.application.contact;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.application.address.AddressOutput;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;

import java.time.Instant;
import java.util.List;

public record ContactOutput(
  EntityId id,
  String phoneNumber,
  String email,
  List<AddressOutput> addresses,
  EntityId customerId,
  Instant createdAt,
  Instant updatedAt,
  Instant deletedAt
) {

  public static  ContactOutput from(Contact aContact) {
   return new ContactOutput(
     aContact.getId(),
     aContact.getPhoneNumber(),
     aContact.getEmail(),
     aContact.getAddresses().stream().map(AddressOutput::from).toList(),
     aContact.getCustomerId(),
     aContact.getCreatedAt(),
     aContact.getUpdatedAt(),
     aContact.getDeletedAt()
   );
  }

}
