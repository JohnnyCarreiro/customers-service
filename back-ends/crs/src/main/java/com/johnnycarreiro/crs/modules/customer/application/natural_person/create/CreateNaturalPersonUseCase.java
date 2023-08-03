package com.johnnycarreiro.crs.modules.customer.application.natural_person.create;

import com.johnnycarreiro.crs.core.application.UseCase;
import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import io.vavr.control.Either;

public abstract class CreateNaturalPersonUseCase
    extends UseCase<CreateNaturalPersonCommand, Either<StackValidationHandler, Void>> {
}
