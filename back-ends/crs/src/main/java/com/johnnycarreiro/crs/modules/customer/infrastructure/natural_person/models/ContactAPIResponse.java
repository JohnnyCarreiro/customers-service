package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record ContactAPIResponse(
  @JsonProperty("id") String id,
  @JsonProperty("phone_number") String phoneNumber,
  @JsonProperty("email") String email,
  @JsonProperty("addresses") List<AddressAPIResponse> addresses,
  @JsonProperty("customer_id") String customerId,
  @JsonProperty("created_at") Instant createdAt,
  @JsonProperty("updated_at") Instant updatedAt,
  @JsonProperty("deleted_at") Instant deletedAt
  ) {
}
