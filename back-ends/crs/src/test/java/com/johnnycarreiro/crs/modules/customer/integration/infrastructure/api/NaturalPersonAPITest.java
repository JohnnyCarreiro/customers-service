package com.johnnycarreiro.crs.modules.customer.integration.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.exceptions.DomainException;
import com.johnnycarreiro.crs.core.domain.exceptions.NotFoundException;
import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.StackValidationHandler;
import com.johnnycarreiro.crs.modules.customer.ControllerTest;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonOutput;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.infrastructure.api.NaturalPersonAPI;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateAddressAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateContactAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateNaturalPersonAPIRequest;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

  private static CreateNaturalPersonAPIRequest getCreateNaturalPersonAPIRequest(String expectedName, String expectedCpf) {
    final var aStreet = "Logradouro 1";
    final var aNumber = 100;
    final String aComplement = null;
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

}
/*
 * TODO: Rotes for:
 *  [x] Crate;
 *  [x] Get By Id;
 *  [] Update;
 *  [] Delete;
 *  [] List;
 * */
