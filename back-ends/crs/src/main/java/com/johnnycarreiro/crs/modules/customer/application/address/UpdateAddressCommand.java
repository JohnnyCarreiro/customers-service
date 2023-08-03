package com.johnnycarreiro.crs.modules.customer.application.address;

public record UpdateAddressCommand(
  String id,
  String street,
  Integer number,
  String complement,
  String area,
  String city,
  String state,
  String cep,
  String unitType,
  String customerID
) {
  public static UpdateAddressCommand with(
    final String anId,
    final String aStreet,
    final Integer aNumber,
    final String aComplement,
    final String anArea,
    final String aCity,
    final String aState,
    final String aCep,
    final String anUnitType,
    final String aCustomerID
  ) {
    return new UpdateAddressCommand(anId, aStreet, aNumber, aComplement, anArea, aCity, aState, aCep, anUnitType, aCustomerID);
  }
}
