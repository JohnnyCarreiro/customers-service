package com.johnnycarreiro.crs.modules.customer.application.natural_person.update;

import com.johnnycarreiro.crs.core.application.UseCase;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import io.vavr.control.Either;

public abstract class UpdateNatualPersonUseCase
    extends UseCase<UpdateNaturalPersonCommand, Either<ValidationHandler, Void>> {
}
