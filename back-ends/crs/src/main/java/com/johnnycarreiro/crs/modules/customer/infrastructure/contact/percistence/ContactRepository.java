package com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactRepository extends JpaRepository<ContactJpaEntity, UUID> {
  Page<ContactJpaEntity> findAll(
    Specification<ContactJpaEntity> whereClause,
    Pageable page
  );
}
