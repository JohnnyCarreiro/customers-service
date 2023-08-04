package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAddressAPIRequest(
  @JsonProperty("street")  String street,
  @JsonProperty("number")  Integer number,
  @JsonProperty("complement")  String complement,
  @JsonProperty("area")  String area,
  @JsonProperty("city")  String city,
  @JsonProperty("state")  String state,
  @JsonProperty("cep")  String cep,
  @JsonProperty("unitType")  String unitType
) {
}
