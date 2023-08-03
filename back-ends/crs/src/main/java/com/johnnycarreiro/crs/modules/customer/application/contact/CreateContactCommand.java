package com.johnnycarreiro.crs.modules.customer.application.contact;

import com.johnnycarreiro.crs.modules.customer.application.address.CreateAddressCommand;

import java.util.List;

public record CreateContactCommand(
  String email,
  String phoneNumber,
  List<CreateAddressCommand> addresses
) {
  public static CreateContactCommand with(
    final String anEmail,
    final String aPhoneNumber,
    final List<CreateAddressCommand> anAddresses
  ) {
    return new CreateContactCommand(anEmail, aPhoneNumber, anAddresses);
  }
}
