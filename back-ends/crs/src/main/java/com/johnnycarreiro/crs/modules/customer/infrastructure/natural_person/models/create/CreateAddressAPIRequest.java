package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.johnnycarreiro.crs.modules.customer.application.address.CreateAddressCommand;

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

  public CreateAddressCommand toCommand() {
    return new CreateAddressCommand(
      this.street(),
      this.number(),
      this.complement(),
      this.area(),
      this.city(),
      this.state(),
      this.cep(),
      this.unitType()
    );
  }
}
