package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateNaturalPersonAPIRequest(
    @JsonProperty("name") String name,
    @JsonProperty("cpf") String cpf,
    @JsonProperty("contact") CreateContactAPIRequest contact
) {
}
