package com.johnnycarreiro.crs.configurations.use_cases;

import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.CreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.create.DefaultCreateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.delete.DefaultDeleteNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.delete.DeleteNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.DefaultGetNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.get.GetNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list.DefaultListNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list.ListNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.DefaultUpdateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.update.UpdateNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NaturalPersonUseCaseConfig {

  private final NaturalPersonGateway personGateway;

  public NaturalPersonUseCaseConfig(final NaturalPersonGateway personGateway) {
    this.personGateway = personGateway;
  }

  @Bean
  public CreateNaturalPersonUseCase createNaturalPersonUseCase() {
    return new DefaultCreateNaturalPersonUseCase(personGateway);
  }

  @Bean
  public UpdateNaturalPersonUseCase updateNaturalPersonUseCase() {
    return new DefaultUpdateNaturalPersonUseCase(personGateway);
  }

  @Bean
  public GetNaturalPersonUseCase getNaturalPersonUseCase() {
    return new DefaultGetNaturalPersonUseCase(personGateway);
  }

  @Bean
  public ListNaturalPersonUseCase listNaturalPersonUseCase() {
    return new DefaultListNaturalPersonUseCase(personGateway);
  }

  @Bean
  public DeleteNaturalPersonUseCase deleteNaturalPersonUseCase() {
    return new DefaultDeleteNaturalPersonUseCase(personGateway);
  }
}
