package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateContactAPIRequest(
  @JsonProperty("phoneNumber") String phoneNumber,
  @JsonProperty("email") String email,
  @JsonProperty("addresses") List<CreateAddressAPIRequest> addresses
  ) {
}
