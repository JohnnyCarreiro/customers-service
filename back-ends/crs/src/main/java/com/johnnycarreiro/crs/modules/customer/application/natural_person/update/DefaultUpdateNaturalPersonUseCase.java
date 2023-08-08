package com.johnnycarreiro.crs.modules.customer.application.natural_person.update;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.exceptions.NotFoundException;
import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateNaturalPersonUseCase extends UpdateNaturalPersonUseCase {

  private final NaturalPersonGateway naturalPersonGateway;

  public DefaultUpdateNaturalPersonUseCase(final NaturalPersonGateway naturalPersonGateway) {
    this.naturalPersonGateway = Objects.requireNonNull(naturalPersonGateway);
  }

  @Override
  public Either<ValidationHandler, Void> execute(UpdateNaturalPersonCommand aCommand) {
    final var anId = EntityId.from(aCommand.id());
    final var aName = aCommand.name();
    final var aCpf = aCommand.cpf();

    final var aPerson = this.naturalPersonGateway.findById(anId)
        .orElseThrow(notFound(anId));

    StackValidationHandler validationHandler = StackValidationHandler.create();
    aPerson
        .update(aName, aCpf)
        .validate(validationHandler);
    return validationHandler.hasErrors()
        ? Left(validationHandler)
        : this.update(aPerson).bimap(left -> left, right -> null);

//    final var anId = EntityId.from(aCommand.id());
//    final var aName = aCommand.name();
//    final var aCpf = aCommand.cpf();
//
//    final var aPerson = this.naturalPersonGateway.findById(anId)
//        .orElseThrow(notFound(anId));
//
//    StackValidationHandler validationHandler = StackValidationHandler.create();
//
////    final var updatedPerson = NaturalPerson.with(aPerson).update(aName, aCpf);
//    final var updatedPerson = aPerson.update(aName, aCpf);
//    updatedPerson.validate(validationHandler);
//    var isEqual = aPerson.equals(updatedPerson);
//    if (isEqual) {
//      return Left(validationHandler.append(new Error("New params are equals to stored entity")));
//    }
//    return validationHandler.hasErrors()
//        ? Left(validationHandler)
//        : this.update(updatedPerson).bimap(left -> left, right -> null);
  }

  private Either<ValidationHandler, NaturalPerson> update(final NaturalPerson aPerson ) {
    return Try(() -> this.naturalPersonGateway.update(aPerson))
        .toEither()
        .mapLeft(StackValidationHandler::create);
  }

  private Supplier<DomainException> notFound(final EntityId anId) {
    return () -> NotFoundException.with(NaturalPerson.class, anId);
  }
}
