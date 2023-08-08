package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UpdateContactAPIRequest(
  @JsonProperty("id") String id,
  @JsonProperty("phoneNumber") String phoneNumber,
  @JsonProperty("email") String email,
  @JsonProperty("addresses") List<UpdateAddressAPIRequest> addresses,
  @JsonProperty("customer_id")  String customerId

  ) {
}
// Customer id could be provided by request, or it can be provided on payload.

