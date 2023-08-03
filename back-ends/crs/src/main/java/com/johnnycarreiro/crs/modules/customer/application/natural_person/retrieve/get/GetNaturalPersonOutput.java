package com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.value_objects.Cpf;

import java.time.Instant;

public record GetNaturalPersonOutput(
    EntityId id,
    String name,
    Cpf cpf,
    Instant createdAt,
    Instant updatedAt
) {
  public static GetNaturalPersonOutput from(NaturalPerson aPerson) {
    return new GetNaturalPersonOutput(
        aPerson.getId(),
        aPerson.getName(),
        aPerson.getCpf(),
        aPerson.getCreatedAt(),
        aPerson.getUpdatedAt()
    );
  }
}
