package com.johnnycarreiro.crs.modules.customer.integration.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnnycarreiro.crs.modules.customer.ControllerTest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.api.NaturalPersonAPI;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.CreateAddressAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.CreateContactAPIRequest;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.CreateNaturalPersonAPIRequest;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Right;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = NaturalPersonAPI.class)
public class NaturalPersonAPITest {

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private CreateNaturalPersonUseCase createNaturalPersonUseCase;

  @Autowired
  private MockMvc mvc;

  @Test()
  @DisplayName("Valid Command - Create new Natural Person")
  public void givenValidCommand_whenCallsCreate_thenCreateANewNaturalPerson() throws Exception {
    final var expectedName = "John Doe";
    final var expectedCpf = "935.411.347-80";

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

    final var anInput = new CreateNaturalPersonAPIRequest(expectedName, expectedCpf, contactAPIRequest);

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
  }

}
