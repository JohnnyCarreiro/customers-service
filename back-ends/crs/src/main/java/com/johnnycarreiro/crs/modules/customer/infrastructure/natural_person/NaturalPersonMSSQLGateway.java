package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.NotFoundException;
import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence.NaturalPersonRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.utils.SpecificationUtils;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static com.johnnycarreiro.crs.modules.customer.infrastructure.utils.SpecificationUtils.like;

@Service
public class NaturalPersonMSSQLGateway implements NaturalPersonGateway {

  private final NaturalPersonRepository personRepository;
//  private final ContactRepository contactRepository;
//  private final AddressRepository addressRepository;

  private final EntityManager entityManager;

  public NaturalPersonMSSQLGateway(
    final NaturalPersonRepository personRepository,
    final EntityManager entityManager
  ) {
    this.personRepository = personRepository;
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public NaturalPerson create(NaturalPerson aNaturalPerson) {
    try {
      final var aContactEntity = ContactJpaEntity.from(aNaturalPerson.getContact());
      final var addressesEntity =  aContactEntity.getAddresses();
      addressesEntity.forEach(entityManager::persist);
      entityManager.persist(aContactEntity);
      return  this.personRepository.save(NaturalPersonJpaEntity.from(aNaturalPerson)).toAggregate();
    } catch (Exception e) {
      // TODO: Handle Exception;
      throw e;
    }
  }

  @Override
  @Transactional
  public NaturalPerson create(final NaturalPerson aNaturalPerson, final List<Address> addresses) {
    // TODO; Before save call validate for all entities in this aggregate;
    NaturalPersonJpaEntity persistedEntity = this.personRepository.save(NaturalPersonJpaEntity.from(aNaturalPerson));

    if(addresses != null && !addresses.isEmpty()) {
      List<AddressJpaEntity> addressEntity = addresses.stream().map(AddressJpaEntity::from).toList();
      addressEntity.forEach(entityManager::persist);

    }
    return persistedEntity.toAggregate();
  }

  @Override
  public void deleteById(String anId) {
    NaturalPersonJpaEntity personEntity =
        this.personRepository.findById(UUID.fromString(anId)).orElseThrow(notFound(EntityId.from(anId)));
    NaturalPerson aPerson = personEntity.toAggregate();
    StackValidationHandler validationHandler = StackValidationHandler.create();
    aPerson.delete().validate(validationHandler);
    if (validationHandler.hasErrors()) return;
    this.personRepository.save(NaturalPersonJpaEntity.from(aPerson));
  }

  @Override
  public Optional<NaturalPerson> findById(EntityId anId) {
       return  this.personRepository.findById(UUID.fromString(anId.getValue()))
           .map(NaturalPersonJpaEntity::toAggregate);
  }

  @Override
  public Optional<NaturalPerson> findCustomerByIdWithContact(EntityId anId) {
    return Optional.empty();
  }

  @Override
  public NaturalPerson update(NaturalPerson aNaturalPerson) {
    return this.personRepository.save(NaturalPersonJpaEntity.from(aNaturalPerson)).toAggregate();
  }

  @Override
  public Pagination<NaturalPerson> findAll(SearchQuery aQuery) {
    final PageRequest page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(
            Direction.fromString(aQuery.direction()),
            aQuery.sort()
        )
    );

    final var especifications = Optional.ofNullable(aQuery.terms())
        .filter(str -> !str.isBlank())
        .map(str ->  SpecificationUtils
            .<NaturalPersonJpaEntity>like("name", str)
            .or(like("cpf", str))
        )
        .orElseGet(() -> Specification.where(null));

    Page<NaturalPersonJpaEntity> pageResult
        = this.personRepository.findAll(Specification.where(especifications), page);
    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(NaturalPersonJpaEntity::toAggregate).toList()
    );
  }

  @Override
  public List<String> existsByIds(Iterable<String> ids) {
    return null;
  }

  private Supplier<NotFoundException> notFound(final EntityId anId) {
    return () -> NotFoundException.with(NaturalPerson.class, anId);
  }
}
