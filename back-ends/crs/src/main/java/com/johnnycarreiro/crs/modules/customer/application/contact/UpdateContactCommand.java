package com.johnnycarreiro.crs.modules.customer.application.contact;

import com.johnnycarreiro.crs.modules.customer.application.address.UpdateAddressCommand;

import java.util.Collections;
import java.util.List;

public record UpdateContactCommand(
  String id,
  String email,
  String phoneNumber,
  List<UpdateAddressCommand> addresses,
  String customerId
) {
  public static UpdateContactCommand with(
    final String anId,
    final String anEmail,
    final String aPhoneNumber,
    final List<UpdateAddressCommand> anAddresses,
    final String aCustomerId
  ) {
    return new UpdateContactCommand(anId, anEmail, aPhoneNumber, anAddresses, aCustomerId);
  }

  public static UpdateContactCommand with(String anId, String aPhoneNumber, String anEmail, UpdateAddressCommand anAddressCmd, String aCustomerId) {
    return new UpdateContactCommand(anId, anEmail, aPhoneNumber, Collections.singletonList(anAddressCmd), aCustomerId);
  }
}
