package com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list;

import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListNaturalPersonUseCase extends ListNaturalPersonUseCase {

  private final NaturalPersonGateway personGateway;

  public DefaultListNaturalPersonUseCase(final NaturalPersonGateway aPersonGateway) {
    this.personGateway = Objects.requireNonNull(aPersonGateway);
  }

  @Override
  public Pagination<NaturalPersonListOutput> execute(SearchQuery aQuery) {
    return this.personGateway.findAll(aQuery)
        .map(NaturalPersonListOutput::from);
  }
}
