package com.johnnycarreiro.crs.modules.customer.integration.application;

import com.johnnycarreiro.crs.modules.customer.IntegrationTest;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {

  @Autowired
  private CreateNaturalPersonUseCase createNaturalPersonUseCase;

  @Autowired
  private NaturalPersonGateway naturalPersonGateway;

  @Test
  public void instantiationTest() {
    Assertions.assertNotNull(createNaturalPersonUseCase);
    Assertions.assertNotNull(naturalPersonGateway);
  }
}
