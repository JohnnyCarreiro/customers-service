package com.johnnycarreiro.crs.modules.customer.infrastructure.api.controllers;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.modules.customer.application.address.CreateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.CreateContactCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.infrastructure.api.NaturalPersonAPI;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.CreateNaturalPersonAPIRequest;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.NaturalPersonAPIResponse;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.presenters.NaturalPersonPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class NaturalPersonController implements NaturalPersonAPI {

  private final CreateNaturalPersonUseCase createNaturalPersonUseCase;
  private final GetNaturalPersonUseCase getNaturalPersonUseCase;

  public NaturalPersonController(
    final CreateNaturalPersonUseCase useCase,
    final GetNaturalPersonUseCase getNaturalPersonUseCase
  ) {
    this.createNaturalPersonUseCase = Objects.requireNonNull(useCase);
    this.getNaturalPersonUseCase = Objects.requireNonNull(getNaturalPersonUseCase);
  }

  @Override
  public ResponseEntity<?> createNaturalPerson(final CreateNaturalPersonAPIRequest anInput) {
    final var contactRequest = anInput.contact();
    List<CreateAddressCommand> createAddressCommands =
      contactRequest.addresses().stream().map(CreateAddressCommand::with).toList();
    CreateContactCommand contactCommand =
      CreateContactCommand.with(contactRequest.email(), contactRequest.phoneNumber(), createAddressCommands);
    final var aCommand = CreateNaturalPersonCommand.with(
      anInput.name(),
      anInput.cpf(),
      contactCommand
    );

//    final Function<ValidationHandler, ResponseEntity<?>> onError =
//        ResponseEntity.unprocessableEntity()::body;
    final Function<ValidationHandler, ResponseEntity<?>> onError = ValidationHandler ->
      ResponseEntity.unprocessableEntity().body(ValidationHandler);

    final Function<Void, ResponseEntity<?>> onSuccess = Void ->
      ResponseEntity.created(URI.create("/natural_persons")).build();

    return this.createNaturalPersonUseCase.execute(aCommand)
      .fold(onError, onSuccess);
  }

  @Override
  public Pagination<?> listNaturalPerson(String search, Integer page, Integer perPage, String sort, String direction) {
    return null;
  }

  @Override
  public NaturalPersonAPIResponse getById(final String id) {
    return NaturalPersonPresenter.present.apply(this.getNaturalPersonUseCase.execute(id));
//    return NaturalPersonPresenter.present
//      .compose(this.getNaturalPersonUseCase::execute)
//      .apply(id);
  }
}
