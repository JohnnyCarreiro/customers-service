package com.johnnycarreiro.crs.modules.customer.domain.entities.contact;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;

import java.util.Optional;

public interface ContactGateway {
  Optional<Contact> findById(EntityId anId);

  Contact update(Contact aContact);

  Pagination<Contact> findAll(SearchQuery aQuery);
}

// TODO: Create contract for Save and implements it for test Transactional implementation to persist a Contact and its addresses at once;
// See Contact Impl Gateway test first todo;
//TODO: Implement findAllActive to retrieve where Deletion Date is null, and findAll for retrieve all and findDeleted to retrieve all Deleted,
//Same for findById
