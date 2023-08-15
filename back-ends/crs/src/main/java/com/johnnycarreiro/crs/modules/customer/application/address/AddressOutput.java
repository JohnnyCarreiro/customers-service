package com.johnnycarreiro.crs.modules.customer.application.address;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.State;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.UnitType;

import java.time.Instant;

public record AddressOutput(
  EntityId id,
  String street,
  Integer number,
  String complement,
  String area,
  String city,
  State state,
  String cep,
  UnitType unitType,
  EntityId customerId,
  Instant createdAt,
  Instant updatedAt,
  Instant deletedAt
) {
  public static AddressOutput from(final Address anAddress) {
    return new AddressOutput(
      anAddress.getId(),
      anAddress.getStreet(),
      anAddress.getNumber(),
      anAddress.getComplement(),
      anAddress.getArea(),
      anAddress.getCity(),
      anAddress.getState(),
      anAddress.getCep(),
      anAddress.getUnitType(),
      anAddress.getCustomerId(),
      anAddress.getCreatedAt(),
      anAddress.getUpdatedAt(),
      anAddress.getDeletedAt()
    );
  }
}
