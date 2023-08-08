package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.johnnycarreiro.crs.modules.customer.application.address.UpdateAddressCommand;

public record UpdateAddressAPIRequest(
  @JsonProperty("id")  String id,
  @JsonProperty("street")  String street,
  @JsonProperty("number")  Integer number,
  @JsonProperty("complement")  String complement,
  @JsonProperty("area")  String area,
  @JsonProperty("city")  String city,
  @JsonProperty("state")  String state,
  @JsonProperty("cep")  String cep,
  @JsonProperty("unitType")  String unitType,
  @JsonProperty("customer_id")  String customerId
) {

  public UpdateAddressCommand toCommand() {
    return UpdateAddressCommand.with(
      this.id(),
      this.street(),
      this.number(),
      this.complement(),
      this.area(),
      this.city(),
      this.state(),
      this.cep(),
      this.unitType(),
      this.customerId()
    );
  }
}
// Customer id could be provided by request, or it can be provided on payload.
