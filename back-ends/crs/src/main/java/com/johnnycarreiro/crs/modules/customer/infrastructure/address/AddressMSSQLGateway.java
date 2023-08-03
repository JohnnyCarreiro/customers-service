package com.johnnycarreiro.crs.modules.customer.infrastructure.address;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.NotFoundException;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.AddressGateway;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressRepository;
import com.johnnycarreiro.crs.modules.customer.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class AddressMSSQLGateway implements AddressGateway {

  private final AddressRepository addressRepository;

  public AddressMSSQLGateway(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
  }

  @Override
  public void create(Address anAddress) {
  }

  @Override
  public Optional<Address> findById(EntityId anId) {
    return  this.addressRepository.findById(UUID.fromString(anId.getValue()))
      .map(AddressJpaEntity::toEntity);
  }

  @Override
  public Address update(Address anAddress) {
    return this.addressRepository.save(AddressJpaEntity.from(anAddress)).toEntity();
  }

  @Override
  public Pagination<Address> findAll(SearchQuery aQuery) {
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
        .<AddressJpaEntity>isNull("deletedAt")
      )
      .orElseGet(() -> Specification.where(null));

    Page<AddressJpaEntity> pageResult
      = this.addressRepository.findAll(Specification.where(specifications), page);
    return new Pagination<>(
      pageResult.getNumber(),
      pageResult.getSize(),
      pageResult.getTotalElements(),
      pageResult.map(AddressJpaEntity::toEntity).toList()
    );
  }

  @Override
  public List<String> existsByIds(Iterable<String> ids) {
    return null;
  }

  private Supplier<NotFoundException> notFound(final EntityId anId) {
    return () -> NotFoundException.with(Address.class, anId);
  }
}
