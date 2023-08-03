package com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<AddressJpaEntity, UUID> {
  Page<AddressJpaEntity> findAll(
    Specification<AddressJpaEntity> whereClause,
    Pageable page
  );
}
