package com.johnnycarreiro.crs.modules.customer.application.natural_person.create;

import com.johnnycarreiro.crs.modules.customer.application.address.CreateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.CreateContactCommand;

import java.util.List;

public record CreateNaturalPersonCommand(
    String name,
    String cpf,
    CreateContactCommand contact
) {
  public static CreateNaturalPersonCommand with(
      final String aName,
      final String aCpf,
      final CreateContactCommand aContact
  ) {
    return new CreateNaturalPersonCommand(aName, aCpf, aContact);
  }
}


