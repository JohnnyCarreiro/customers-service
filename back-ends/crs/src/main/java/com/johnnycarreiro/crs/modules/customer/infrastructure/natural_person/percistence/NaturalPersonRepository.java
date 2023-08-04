package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NaturalPersonRepository extends JpaRepository<NaturalPersonJpaEntity, UUID> {

  Page<NaturalPersonJpaEntity> findAll(
      Specification<NaturalPersonJpaEntity> whereClause,
      Pageable page
  );
}
