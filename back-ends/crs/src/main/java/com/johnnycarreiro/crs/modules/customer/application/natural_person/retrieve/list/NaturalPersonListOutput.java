package com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.application.contact.ContactOutput;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.value_objects.Cpf;

import java.time.Instant;

public record NaturalPersonListOutput(
    EntityId id,
    String name,
    Cpf cpf,

    ContactOutput contact,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {
  public static NaturalPersonListOutput from(NaturalPerson aPerson) {
    return new NaturalPersonListOutput(
        aPerson.getId(),
        aPerson.getName(),
        aPerson.getCpf(),
        ContactOutput.from(aPerson.getContact()),
        aPerson.getCreatedAt(),
        aPerson.getUpdatedAt(),
        aPerson.getDeletedAt()
    );
  }
}
