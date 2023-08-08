package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateNaturalPersonAPIRequest(
    @JsonProperty("name") String name,
    @JsonProperty("cpf") String cpf,
    @JsonProperty("contact") UpdateContactAPIRequest contact
) {
}
