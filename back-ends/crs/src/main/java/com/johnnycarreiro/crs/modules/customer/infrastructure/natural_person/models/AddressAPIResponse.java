package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AddressAPIResponse(
  @JsonProperty("id")  String id,
  @JsonProperty("street")  String street,
  @JsonProperty("number")  Integer number,
  @JsonProperty("complement")  String complement,
  @JsonProperty("area")  String area,
  @JsonProperty("city")  String city,
  @JsonProperty("state")  String state,
  @JsonProperty("cep")  String cep,
  @JsonProperty("unitType")  String unitType,
	@JsonProperty("customer_id") String customerId,
  @JsonProperty("created_at") Instant createdAt,
  @JsonProperty("updated_at") Instant updatedAt,
  @JsonProperty("deleted_at") Instant deletedAt
) {
}
