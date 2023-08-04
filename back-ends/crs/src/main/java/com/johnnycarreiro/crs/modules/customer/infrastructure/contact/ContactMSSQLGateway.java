package com.johnnycarreiro.crs.modules.customer.infrastructure.contact;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.ContactGateway;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.utils.SpecificationUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.johnnycarreiro.crs.modules.customer.infrastructure.utils.SpecificationUtils.isNull;
import static com.johnnycarreiro.crs.modules.customer.infrastructure.utils.SpecificationUtils.like;

@Service
public class ContactMSSQLGateway implements ContactGateway {

  private final  ContactRepository contactRepository;

  private final EntityManager entityManager;

  public ContactMSSQLGateway(final ContactRepository contactRepository, final EntityManager entityManager) {
    this.contactRepository = contactRepository;
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Contact> findById(EntityId anId) {
    return  this.contactRepository.findById(UUID.fromString(anId.getValue()))
      .map(ContactJpaEntity::toEntity);
  }

  @Override
  @Transactional
  public Contact update(Contact aContact) {
    if (aContact.getAddresses().isEmpty()) {
      return this.contactRepository.save(ContactJpaEntity.from(aContact)).toEntity();
    }
    List<AddressJpaEntity> addresses = aContact.getAddresses().stream().map(AddressJpaEntity::from).toList();
    addresses.forEach(entityManager::merge); // TODO: Maybe use entityManager in this case makes no sense
    return this.contactRepository.save(ContactJpaEntity.from(aContact)).toEntity();
  }

  @Override
  public Pagination<Contact> findAll(SearchQuery aQuery) {
    final PageRequest page = PageRequest.of(
      aQuery.page(),
      aQuery.perPage(),
      Sort.by(
        Sort.Direction.fromString(aQuery.direction()),
        aQuery.sort()
      )
    );

    final var specifications = Optional.ofNullable(aQuery.terms())
      .filter(str -> !str.isBlank())
      .map(str ->  SpecificationUtils
        .<ContactJpaEntity>like("phoneNumber", str)
        .or(like("email", str))
        .or(like("customerId", str))
        .and(isNull("deletedAt"))
      )
      .orElseGet(() -> Specification.where(null));

    Page<ContactJpaEntity> pageResult
      = this.contactRepository.findAll(Specification.where(specifications), page);
    return new Pagination<>(
      pageResult.getNumber(),
      pageResult.getSize(),
      pageResult.getTotalElements(),
      pageResult.map(ContactJpaEntity::toEntity).toList()
    );
  }
}
