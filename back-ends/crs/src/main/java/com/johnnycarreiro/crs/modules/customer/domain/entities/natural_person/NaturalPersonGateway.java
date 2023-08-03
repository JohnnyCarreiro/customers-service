package com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface NaturalPersonGateway {
  NaturalPerson create(NaturalPerson aNaturalPerson);

  NaturalPerson create(NaturalPerson aNaturalPerson, List<Address> addresses);

  void deleteById(String anId);

  Optional<NaturalPerson> findById(EntityId anId);

  NaturalPerson update(NaturalPerson aNaturalPerson);

  Pagination<NaturalPerson> findAll(SearchQuery aQuery);

  List<String> existsByIds(Iterable<String> ids);
}
