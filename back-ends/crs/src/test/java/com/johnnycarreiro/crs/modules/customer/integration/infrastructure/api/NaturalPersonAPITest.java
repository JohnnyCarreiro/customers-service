package com.johnnycarreiro.crs.modules.customer.integration.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.exceptions.NotFoundException;
import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.modules.customer.ControllerTest;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.delete.DeleteNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonOutput;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list.ListNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list.NaturalPersonListOutput;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.UpdateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import com.johnnycarreiro.crs.modules.customer.infrastructure.api.NaturalPersonAPI;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateAddressAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateContactAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateNaturalPersonAPIRequest;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update.UpdateAddressAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update.UpdateContactAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update.UpdateNaturalPersonAPIRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = NaturalPersonAPI.class)
public class NaturalPersonAPITest {

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private CreateNaturalPersonUseCase createNaturalPersonUseCase;

  @MockBean
  private GetNaturalPersonUseCase getNaturalPersonUseCase;

  @MockBean
  private UpdateNaturalPersonUseCase updateNaturalPersonUseCase;

  @MockBean
  private DeleteNaturalPersonUseCase deleteNaturalPersonUseCase;

  @MockBean
  private ListNaturalPersonUseCase listNaturalPersonUseCase;

  @Autowired
  private MockMvc mvc;

  @Test()
  @DisplayName("Valid Command - Create new Natural Person")
  public void givenValidCommand_whenCallsCreate_thenCreateANewNaturalPerson() throws Exception {
    final var expectedName = "John Doe";
    final var expectedCpf = "935.411.347-80";

    final var anInput = getCreateNaturalPersonAPIRequest(expectedName, expectedCpf);

    when(createNaturalPersonUseCase.execute(any()))
        .thenReturn(Right(null));

    final var aRequest = post("/natural_persons")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(anInput));

    this.mvc.perform(aRequest)
        .andDo(print())
        .andExpectAll(
            status().isCreated(),
            header().string("Location", "/natural_persons")
        );

    verify(createNaturalPersonUseCase, times(1))
        .execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCpf, cmd.cpf())
            ));
    //TODO: Verify Contact and Addresses
  }

  @Test()
  @DisplayName("Invalid Name - Throws an Exceptions")
  public void givenInvalidName_whenCallsCreate_thenItShouldThrowsStackValidation() throws Exception {
    final var expectedMessage = "`Name` shouldn't be null";

    final String aName = null;
    final var aCpf = "935.411.347-80";

    final var anInput = getCreateNaturalPersonAPIRequest(aName, aCpf);

    when(createNaturalPersonUseCase.execute(any()))
      .thenReturn(Left(StackValidationHandler.create(new Error(expectedMessage))));

    final var aRequest = post("/natural_persons")
      .contentType(MediaType.APPLICATION_JSON)
      .content(this.mapper.writeValueAsString(anInput));

    this.mvc.perform(aRequest)
      .andDo(print())
      .andExpectAll(
        status().isUnprocessableEntity(),
        header().string("Location", nullValue()),
        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
        jsonPath("$.errors", hasSize(1)),
        jsonPath("$.errors[0].message", equalTo(expectedMessage))
      );

    verify(createNaturalPersonUseCase, times(1))
      .execute(argThat(cmd ->
        Objects.equals(aName, cmd.name())
        && Objects.equals(aCpf, cmd.cpf())
      ));
  }

  @Test()
  @DisplayName("Invalid Cmd - Throws an Exceptions")
  public void givenInvalidCommand_whenCallsCreate_thenItShouldThrowsADomainException() throws Exception {
    final var expectedMessage = "`Name` shouldn't be null";

    final String aName = null;
    final var aCpf = "935.411.347-80";

    final var anInput = getCreateNaturalPersonAPIRequest(aName, aCpf);

    when(createNaturalPersonUseCase.execute(any()))
      .thenThrow(DomainException.with(new Error(expectedMessage)));

    final var aRequest = post("/natural_persons")
      .contentType(MediaType.APPLICATION_JSON)
      .content(this.mapper.writeValueAsString(anInput));

    this.mvc.perform(aRequest)
      .andDo(print())
      .andExpect(status().isUnprocessableEntity())
      .andExpect(header().string("Location", nullValue()))
      .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.message", equalTo(expectedMessage)))
      .andExpect(jsonPath("$.errors", hasSize(1)))
      .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));


    verify(createNaturalPersonUseCase, times(1))
      .execute(argThat(cmd ->
        Objects.equals(aName, cmd.name())
        && Objects.equals(aCpf, cmd.cpf())
      ));
  }

  @Test
  @DisplayName("Get by Valid Id - Returns Entity Output")
  public void givenValidId_whenCallGetNaturalPerson_thenItShouldReturnsANaturalPersonOutPut() throws Exception {
    final var aName = "John Doe";
    final var aCpf = "935.411.347-80";
    NaturalPerson aNaturalPerson = NaturalPerson.create(aName, aCpf);
    final var expectedName = "John Doe";
    final var expectedCpf = "93541134780";
    final var expectedId = aNaturalPerson.getId().getValue();
    final var expectedCreateAt = aNaturalPerson.getCreatedAt();
    final var expectedUpdatedAt = aNaturalPerson.getUpdatedAt();

    Address anAddress = Address.create(
      "logradouro",
      100,
      null,
      "Bairro",
      "Mogi-guaçu",
      "sp",
      "00100-000",
      "Commercial",
      expectedId
    );
    Contact aContact = Contact.create(
      "(12) 99720-4431",
      "john.doe@acme.com",
      anAddress,
      EntityId.from(expectedId)
    );
    aNaturalPerson.addContact(aContact);

    Mockito.when(getNaturalPersonUseCase.execute(any()))
      .thenReturn(GetNaturalPersonOutput.from(aNaturalPerson));

    final var aRequest = get("/natural_persons/{id}", expectedId)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(aRequest)
      .andDo(print());

    response.andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(expectedId)))
        .andExpect(jsonPath("$.name", equalTo(expectedName)))
        .andExpect(jsonPath("$.cpf", equalTo(expectedCpf)))
        .andExpect(jsonPath("$.created_at", equalTo(expectedCreateAt.toString())))
        .andExpect(jsonPath("$.updated_at", equalTo(expectedUpdatedAt.toString())))
    ;
  }

  @Test
  @DisplayName("Get Invalid Id - Returns Not Found Exc.")
  public void givenInvalidId_whenCallGetNaturalPerson_thenItShouldReturnsNotFound() throws Exception {
    final var expectedMessage = "NaturalPerson with ID a7b128d7-fa53-4e23-ac70-cff8fd7f9a60 was not found";
    final var expectedId = EntityId.from("a7b128d7-fa53-4e23-ac70-cff8fd7f9a60");

    final var aRequest = get("/natural_persons/{id}", expectedId.getValue())
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON);

    when(getNaturalPersonUseCase.execute(any()))
      .thenThrow(NotFoundException.with(
        NaturalPerson.class, expectedId
      ));

    final var response = this.mvc.perform(aRequest)
      .andDo(print());

    response.andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
  }

  @Test()
  @DisplayName("Valid Command - Update a Natural Person")
  public void givenValidCommand_whenCallExecute_thenUpdateANewNaturalPerson() throws Exception {
    final var expectedId = "a7b128d7-fa53-4e23-ac70-cff8fd7f9a60";
    final var expectedName = "John Doe";
    final var expectedCpf = "935.411.347-80";

    final var anInput =
      getUpdateNaturalPersonAPIRequest(expectedName, expectedCpf);

    when(updateNaturalPersonUseCase.execute(any()))
      .thenReturn(Right(null));

    final var aRequest = put("/natural_persons/{id}", expectedId)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(anInput));

    this.mvc.perform(aRequest)
      .andDo(print())
      .andExpectAll(
        status().isNoContent()
      );

    verify(updateNaturalPersonUseCase, times(1))
      .execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name())
        && Objects.equals(expectedCpf, cmd.cpf())
        && Objects.nonNull(cmd.contact())
        && Objects.equals(1, cmd.contact().addresses().size())
      ));
    //TODO: Verify Contact and Addresses
  }

  @Test()
  @DisplayName("Delete Valid Id - Delete a Natural Person")
  public void givenAValidID_whenCallExecute_thenDeleteANewNaturalPerson() throws Exception {
    final var aName = "John Doe";
    final var aCpf = "935.411.347-80";
    NaturalPerson aNaturalPerson = NaturalPerson.create(aName, aCpf);
    final var expectedId = aNaturalPerson.getId().getValue();

    Address anAddress = Address.create(
      "logradouro",
      100,
      null,
      "Bairro",
      "Mogi-guaçu",
      "sp",
      "00100-000",
      "Commercial",
      expectedId
    );
    Contact aContact = Contact.create(
      "(12) 99720-4431",
      "john.doe@acme.com",
      anAddress,
      EntityId.from(expectedId)
    );
    aNaturalPerson.addContact(aContact);

    doNothing()
      .when(deleteNaturalPersonUseCase).execute(any());

    final var aRequest = delete("/natural_persons/{id}", expectedId)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(aRequest)
      .andDo(print());

    response.andExpect(status().isNoContent());

    verify(deleteNaturalPersonUseCase, times(1)).execute(eq(expectedId));
  }

  @Test
  @DisplayName("Delete Invalid Id - Returns Not Found Exc.")
  public void givenInvalidID_whenCallsDeleteNaturalPerson_thenItShouldReturnsNotFound() throws Exception {
    final var expectedMessage = "NaturalPerson with ID a7b128d7-fa53-4e23-ac70-cff8fd7f9a60 was not found";
    final var expectedId = EntityId.from("a7b128d7-fa53-4e23-ac70-cff8fd7f9a60");

    final var aRequest = delete("/natural_persons/{id}", expectedId.getValue())
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON);

    doThrow(NotFoundException.with(
      NaturalPerson.class, expectedId
    ))
      .when(deleteNaturalPersonUseCase).execute(any());

    final var response = this.mvc.perform(aRequest)
      .andDo(print());

    response.andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
  }

  @Test
  @DisplayName("Valid Query - Returns Natural Person Output")
  public void givenValidQuery_whenCallListNaturalPerson_thenReturnsNaturalPersonList() throws Exception {
    NaturalPerson aNaturalPerson = getNewCompleteNaturalPerson();
    List<NaturalPerson> naturalPeople = List.of(aNaturalPerson);

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedItemsCount = 1;
    final var expectedTotal = 1;

    final var aQuery =
      SearchQuery.from(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    final var expectedPagination =
      new Pagination<>(expectedPage, expectedPerPage, naturalPeople.size(), naturalPeople);
    when(listNaturalPersonUseCase.execute(any()))
      .thenReturn(expectedPagination.map(NaturalPersonListOutput::from));

    final var aRequest = get("/natural_persons")
      .queryParam("page", String.valueOf(expectedPage))
      .queryParam("perPage", String.valueOf(expectedPerPage))
      .queryParam("sort", expectedSort)
      .queryParam("dir", expectedDirection)
      .queryParam("search", expectedTerms)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(aRequest)
      .andDo(print());

    response.andExpect(status().isOk())
      .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
      .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
      .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
      .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
      .andExpect(jsonPath("$.items[0].id", equalTo(aNaturalPerson.getId().getValue())))
      .andExpect(jsonPath("$.items[0].name", equalTo(aNaturalPerson.getName())))
      .andExpect(jsonPath("$.items[0].cpf", equalTo(aNaturalPerson.getCpf().getValue())))
      .andExpect(jsonPath("$.items[0].created_at", equalTo(aNaturalPerson.getCreatedAt().toString())))
      .andExpect(jsonPath("$.items[0].updated_at", equalTo(aNaturalPerson.getUpdatedAt().toString())))
      .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aNaturalPerson.getDeletedAt())))
      .andExpect(jsonPath("$.items[0].contact.email", equalTo(aNaturalPerson.getContact().getEmail())))
      .andExpect(jsonPath("$.items[0].contact.phone_number", equalTo(aNaturalPerson.getContact().getPhoneNumber())))
      .andExpect(jsonPath("$.items[0].contact.addresses", hasSize(aNaturalPerson.getContact().getAddresses().size())));

    verify(listNaturalPersonUseCase, times(1)).execute(argThat(query ->
      Objects.equals(expectedPage, query.page())
      &&  Objects.equals(expectedPerPage, query.perPage())
      &&  Objects.equals(expectedTerms, query.terms())
      &&  Objects.equals(expectedSort, query.sort())
      &&  Objects.equals(expectedDirection, query.direction())
    ));

  }

  private static CreateNaturalPersonAPIRequest getCreateNaturalPersonAPIRequest(String expectedName, String expectedCpf) {
    final var aStreet = "Logradouro 1";
    final var aNumber = 100;
    final var aComplement = "";
    final var anArea = "Bairro 1";
    final var aCity = "Mogi Guaçu 1";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";
    final var addressRequest = new CreateAddressAPIRequest(
      aStreet,
      aNumber,
      aComplement,
      anArea,
      aCity,
      anState,
      aCep,
      anUnitType
    );
    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    List<CreateAddressAPIRequest> addressesReq = List.of(addressRequest);
    final var contactAPIRequest = new CreateContactAPIRequest(aPhoneNumber, anEmail, addressesReq);

	  return new CreateNaturalPersonAPIRequest(expectedName, expectedCpf, contactAPIRequest);
  }

  private static UpdateNaturalPersonAPIRequest getUpdateNaturalPersonAPIRequest(String expectedName, String expectedCpf) {
    final var customerId = UUID.randomUUID().toString();
    final var aStreet = "Logradouro 1";
    final var aNumber = 100;
    final var aComplement = "";
    final var anArea = "Bairro 1";
    final var aCity = "Mogi Guaçu 1";
    final var aCep = "00100-000";
    final var anState = "SP";
    final var anUnitType = "Residential";
    final var addressRequest = new UpdateAddressAPIRequest(
      UUID.randomUUID().toString(),
      aStreet,
      aNumber,
      aComplement,
      anArea,
      aCity,
      anState,
      aCep,
      anUnitType,
      customerId
    );
    final var aPhoneNumber = "(12) 99720-4431";
    final var anEmail = "john.doe@acme.com";
    List<UpdateAddressAPIRequest> addressesReq = List.of(addressRequest);
    final var contactAPIRequest = new UpdateContactAPIRequest(UUID.randomUUID().toString(), aPhoneNumber, anEmail, addressesReq, customerId);

    return new UpdateNaturalPersonAPIRequest(expectedName, expectedCpf, contactAPIRequest);
  }

  private static NaturalPerson getNewCompleteNaturalPerson() {
    final var aName = "John Doe";
    final var aCpf = "935.411.347-80";
    NaturalPerson aNaturalPerson = NaturalPerson.create(aName, aCpf);
    final var expectedId = aNaturalPerson.getId().getValue();

    Address anAddress = Address.create(
      "logradouro",
      100,
      null,
      "Bairro",
      "Mogi-guaçu",
      "sp",
      "00100-000",
      "Commercial",
      expectedId
    );
    Contact aContact = Contact.create(
      "(12) 99720-4431",
      "john.doe@acme.com",
      anAddress,
      EntityId.from(expectedId)
    );
    return aNaturalPerson.addContact(aContact);
  }
}
/*
 * TODO: Rotes for:
 *  [x] Crate;
 *  [x] Get By Id;
 *  [x] Update;
 *  [] Update - ex;
 *  [x] Delete;
 *  [x] List;
 * */
