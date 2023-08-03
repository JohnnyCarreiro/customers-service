package com.johnnycarreiro.crs.modules.customer.domain.entities.address;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface AddressGateway {

  public void create(Address anAddress);

  Optional<Address> findById(EntityId anId);

  Address update(Address anAddress);

  Pagination<Address> findAll(SearchQuery aQuery);

  List<String> existsByIds(Iterable<String> ids);
}
