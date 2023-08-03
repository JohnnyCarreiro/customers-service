package com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.value_objects.Cpf;

import java.time.Instant;

public record NaturalPersonListOutput(
    EntityId id,
    String name,
    Cpf cpf,
    Instant createdAt,
    Instant updatedAt
) {
  public static NaturalPersonListOutput from(NaturalPerson aPerson) {
    return new NaturalPersonListOutput(
        aPerson.getId(),
        aPerson.getName(),
        aPerson.getCpf(),
        aPerson.getCreatedAt(),
        aPerson.getUpdatedAt()
    );
  }
}
