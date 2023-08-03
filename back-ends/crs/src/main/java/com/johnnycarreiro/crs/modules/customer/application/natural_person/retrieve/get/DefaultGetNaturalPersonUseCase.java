package com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.NotFoundException;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetNaturalPersonUseCase extends GetNaturalPersonUseCase {

  private final NaturalPersonGateway personGateway;

  public DefaultGetNaturalPersonUseCase(final NaturalPersonGateway personGateway) {
    this.personGateway = Objects.requireNonNull(personGateway);
  }

  @Override
  public GetNaturalPersonOutput execute(String anId) {
    final EntityId aPersonId =  EntityId.from(anId);
    return this.personGateway.findById(aPersonId)
        .map(GetNaturalPersonOutput::from)
        .orElseThrow(notFound(aPersonId));
  }

  private Supplier<NotFoundException> notFound(final EntityId anId) {
    return () -> NotFoundException.with(NaturalPerson.class, anId);
  }
}
