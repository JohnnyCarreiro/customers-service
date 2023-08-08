package com.johnnycarreiro.crs.modules.customer.application.address;

import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateAddressAPIRequest;

public record CreateAddressCommand(
  String street,
  Integer number,
  String complement,
  String area,
  String city,
  String state,
  String cep,
  String unitType
) {
  public static CreateAddressCommand with(
    final String aStreet,
    final Integer aNumber,
    final String aComplement,
    final String anArea,
    final String aCity,
    final String aState,
    final String aCep,
    final String anUnitType
  ) {
    return new CreateAddressCommand(aStreet, aNumber, aComplement, anArea, aCity, aState, aCep, anUnitType);
  }

  public static CreateAddressCommand with(CreateAddressAPIRequest addr) {
    return new CreateAddressCommand(addr.street(),
      addr.number(),
      addr.complement(),
      addr.area(),
      addr.city(),
      addr.state(),
      addr.cep(),
      addr.unitType());
  }
}
