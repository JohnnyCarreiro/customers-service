package com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.value_objects.Cpf;

import java.time.Instant;

public record GetNaturalPersonOutput(
    EntityId id,
    String name,
    Cpf cpf,
    Contact contact,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {
  public static GetNaturalPersonOutput from(NaturalPerson aPerson) {
    return new GetNaturalPersonOutput(
        aPerson.getId(),
        aPerson.getName(),
        aPerson.getCpf(),
        aPerson.getContact(),
        aPerson.getCreatedAt(),
        aPerson.getUpdatedAt(),
        aPerson.getDeletedAt()
    );
  }
}
