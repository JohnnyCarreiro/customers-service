package com.johnnycarreiro.crs.modules.customer.application.natural_person.create;

import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.modules.customer.application.address.CreateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import io.vavr.control.Either;

import static io.vavr.API.*;

public class DefaultCreateNaturalPersonUseCase extends CreateNaturalPersonUseCase {

  private final NaturalPersonGateway personGateway;

  public DefaultCreateNaturalPersonUseCase(NaturalPersonGateway naturalPersonGateway) {
    this.personGateway = naturalPersonGateway;
  }

  @Override
  public Either<StackValidationHandler, Void> execute(CreateNaturalPersonCommand aCommand) {
    final var aName = aCommand.name();
    final var aCpf = aCommand.cpf();
    NaturalPerson newNaturalPerson = NaturalPerson.create(aName, aCpf);

    final var aContactCmd = aCommand.contact();
    final var anAddresses =
      aContactCmd.addresses().stream().map(anAddress ->
        Address.create(
          anAddress.street(),
          anAddress.number(),
          anAddress.complement(),
          anAddress.area(),
          anAddress.city(),
          anAddress.state(),
          anAddress.cep(),
          anAddress.unitType(),
          newNaturalPerson.getId().getValue()
        )
      ).toList();

    final var aContact =
      Contact.create(newNaturalPerson.getId(), aContactCmd.phoneNumber(), aContactCmd.email(), anAddresses);

    newNaturalPerson.addContact(aContact);

    StackValidationHandler validationHandler = StackValidationHandler.create();
    newNaturalPerson.getContact().getAddresses().forEach(anAdress -> anAdress.validate(validationHandler));
    newNaturalPerson.getContact().validate(validationHandler);
    newNaturalPerson.validate(validationHandler);


    return validationHandler.hasErrors()
        ? Left(validationHandler)
        : create(newNaturalPerson).bimap(left -> left, right -> null);
  }

  private Either<StackValidationHandler, NaturalPerson> create(NaturalPerson aPerson) {
    return Try(() -> this.personGateway.create(aPerson))
        .toEither()
        .mapLeft(StackValidationHandler::create);
  }
}

// TODO: For this exemple Default Create Natural Person Use Case will create a full Aggregate. But is a good practice create one UC for
