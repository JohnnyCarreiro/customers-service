package com.johnnycarreiro.crs.modules.customer.infrastructure.api.controllers;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.modules.customer.application.address.CreateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.address.UpdateAddressCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.CreateContactCommand;
import com.johnnycarreiro.crs.modules.customer.application.contact.UpdateContactCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.delete.DeleteNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list.ListNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.UpdateNaturalPersonCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.UpdateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import com.johnnycarreiro.crs.modules.customer.infrastructure.api.NaturalPersonAPI;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateAddressAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateNaturalPersonAPIRequest;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonCommand;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.NaturalPersonAPIResponse;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update.UpdateAddressAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update.UpdateContactAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update.UpdateNaturalPersonAPIRequest;
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
  private final UpdateNaturalPersonUseCase updateNaturalPersonUseCase;
  private final DeleteNaturalPersonUseCase deleteNaturalPersonUseCase;
  private final ListNaturalPersonUseCase listNaturalPersonUseCase;

  public NaturalPersonController(
    final CreateNaturalPersonUseCase useCase,
    final GetNaturalPersonUseCase getNaturalPersonUseCase,
    final UpdateNaturalPersonUseCase updateNaturalPersonUseCase,
    final DeleteNaturalPersonUseCase deleteNaturalPersonUseCase,
    final ListNaturalPersonUseCase listNaturalPersonUseCase
    ) {
    this.createNaturalPersonUseCase = Objects.requireNonNull(useCase);
    this.getNaturalPersonUseCase = Objects.requireNonNull(getNaturalPersonUseCase);
    this.updateNaturalPersonUseCase = Objects.requireNonNull(updateNaturalPersonUseCase);
    this.deleteNaturalPersonUseCase =  Objects.requireNonNull(deleteNaturalPersonUseCase);
    this.listNaturalPersonUseCase = Objects.requireNonNull((listNaturalPersonUseCase));
  }

  @Override
  public ResponseEntity<?> createNaturalPerson(final CreateNaturalPersonAPIRequest anInput) {
    final var contactRequest = anInput.contact();
    List<CreateAddressCommand> createAddressCommands =
      contactRequest.addresses().stream().map(CreateAddressAPIRequest::toCommand).toList();
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
  public Pagination<?> listNaturalPerson(
    final String search,
    final Integer page,
    final Integer perPage,
    final String sort,
    final String direction
  ) {
    return listNaturalPersonUseCase
      .execute(new SearchQuery(page, perPage, search, sort, direction))
      .map(NaturalPersonPresenter::present);
  }

  @Override
  public NaturalPersonAPIResponse getById(final String id) {
    return NaturalPersonPresenter.present.apply(this.getNaturalPersonUseCase.execute(id));
//    return NaturalPersonPresenter.present
//      .compose(this.getNaturalPersonUseCase::execute)
//      .apply(id);
  }

  @Override
  public ResponseEntity<?> updateById(String id, UpdateNaturalPersonAPIRequest anInput) {
    UpdateContactAPIRequest contactRequest = anInput.contact();
    List<UpdateAddressCommand> updateAddressCommands =
      contactRequest.addresses().stream().map(UpdateAddressAPIRequest::toCommand).toList();

    UpdateContactCommand contactCommand =
      UpdateContactCommand.with(contactRequest.id(), contactRequest.email(), contactRequest.phoneNumber(), updateAddressCommands, id);

    final var aCommand = UpdateNaturalPersonCommand.with(
      id,
      anInput.name(),
      anInput.cpf(),
      contactCommand
    );
    final Function<ValidationHandler, ResponseEntity<?>> onError = ValidationHandler ->
      ResponseEntity.unprocessableEntity().body(ValidationHandler);

    final Function<Void, ResponseEntity<?>> onSuccess = Void -> {
      return ResponseEntity.noContent().build();
    };

    return this.updateNaturalPersonUseCase.execute(aCommand)
      .fold(onError, onSuccess);
  }

  @Override
  public ResponseEntity<Object> deleteById(final String id) {
    this.deleteNaturalPersonUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}
