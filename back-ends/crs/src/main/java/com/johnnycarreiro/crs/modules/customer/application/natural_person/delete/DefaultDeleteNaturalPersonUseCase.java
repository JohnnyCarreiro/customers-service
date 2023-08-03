package com.johnnycarreiro.crs.modules.customer.application.natural_person.delete;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.NotFoundException;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class DefaultDeleteNaturalPersonUseCase extends DeleteNaturalPersonUseCase {

  private final NaturalPersonGateway personGateway;

  public DefaultDeleteNaturalPersonUseCase(NaturalPersonGateway aPersonGateway) {
    this.personGateway = Objects.requireNonNull(aPersonGateway);
  }

  @Override
  public void execute(String anId) {
    final EntityId aNaturalPersonId = EntityId.from(anId);
    final NaturalPerson persistedNaturalPerson =
        this.personGateway.findById(aNaturalPersonId)
            .orElseThrow(notFound(aNaturalPersonId));
    persistedNaturalPerson.delete();
    persistedNaturalPerson.getContact().delete();
    persistedNaturalPerson.getContact().getAddresses().forEach(Address::delete);
    personGateway.update(persistedNaturalPerson);
  }
  private Supplier<NotFoundException> notFound(final EntityId anId) {
    return () -> NotFoundException.with(NaturalPerson.class, anId);
  }
}
