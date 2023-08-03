package com.johnnycarreiro.crs.modules.customer.application.natural_person.update;

import com.johnnycarreiro.crs.modules.customer.application.contact.UpdateContactCommand;

public record UpdateNaturalPersonCommand(
    String id,
    String name,
    String cpf,
    UpdateContactCommand contact,
    String customerId
) {
  public static UpdateNaturalPersonCommand with(
      final String anId,
      final String aName,
      final String aCpf,
      final UpdateContactCommand aContact,
      final String aCustomerId
  ) {
    return new UpdateNaturalPersonCommand(anId, aName, aCpf, aContact, aCustomerId);
  }
}
