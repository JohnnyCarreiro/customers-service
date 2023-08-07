package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record NaturalPersonAPIResponse(
  @JsonProperty("id") String id,
  @JsonProperty("name") String name,
  @JsonProperty("cpf") String cpf,
  @JsonProperty("contact") ContactAPIResponse contact,
  @JsonProperty("created_at") Instant createdAt,
  @JsonProperty("updated_at") Instant updatedAt,
  @JsonProperty("deleted_at") Instant deletedAt
) {
}
