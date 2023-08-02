package com.johnnycarreiro.crs.modules.customer.domain.entities.address;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;

public enum State {
  AMAZONAS("Amazonas", "AM"),
  ALAGOAS("Alagoas", "AL"),
  ACRE("Acre", "AC"),
  AMAPA("Amapá", "AP"),
  BAHIA("Bahia", "BA"),
  PARA("Pará", "PA"),
  MATO_GROSSO("Mato Grosso", "MT"),
  MINAS_GERAIS("Minas Gerais", "MG"),
  MATO_GROSSO_DO_SUL("Mato Grosso do Sul", "MS"),
  GOIAS("Goiás", "GO"),
  MARANHAO("Maranhão", "MA"),
  RIO_GRANDE_DO_SUL("Rio Grande do Sul", "RS"),
  TOCANTINS("Tocantins", "TO"),
  PIAUI("Piauí", "PI"),
  SAO_PAULO("São Paulo", "SP"),
  RONDONIA("Rondônia", "RO"),
  RORAIMA("Roraima", "RR"),
  PARANA("Paraná", "PR"),
  CEARA("Ceará", "CE"),
  PERNAMBUCO("Pernambuco", "PE"),
  SANTA_CATARINA("Santa Catarina", "SC"),
  PARAIBA("Paraíba", "PB"),
  RIO_GRANDE_DO_NORTE("Rio Grande do Norte", "RN"),
  ESPIRITO_SANTO("Espírito Santo", "ES"),
  RIO_DE_JANEIRO("Rio de Janeiro", "RJ"),
  SERGIPE("Sergipe", "SE"),
  DISTRITO_FEDERAL("Distrito Federal", "DF"),
  HAS_ERROR;

  private final String stateName;
  private final String acronym;

  State(final String stateName, final String acronym) {
    this.stateName = stateName;
    this.acronym = acronym;
  }

  State() {
    this.stateName = null;
    this.acronym = null;
  }

  public static State from(final String value) {
    if(State.isNullOrEmpty(value)) return State.HAS_ERROR;
    var length = value.length();
    if(length == 2) {
     return State.fromAcronym(value.toUpperCase());
    }
    return State.fromState(value);
  }


  public static State fromState(final String stateName) {
    if(isNullOrEmpty(stateName)) return State.HAS_ERROR;
    for (final State state : State.values()) {
      if (state.stateName.equalsIgnoreCase(stateName)) {
        return state;
      }
    }
    return State.HAS_ERROR;
//    throw new IllegalArgumentException(stateName);
  }

  public static State fromAcronym(final String acronym) {
    if(isNullOrEmpty(acronym)) return State.HAS_ERROR;
    for (final State state : State.values()) {
      if (state.acronym.equalsIgnoreCase(acronym)) {
        return state;
      }
    }
    return State.HAS_ERROR;
//    throw new IllegalArgumentException(acronym);
  }

  private static boolean isNullOrEmpty(String value) {
    if(value == null) return true;
    return value.isBlank();
  }


  public String getAcronym() {
    return this.acronym;
  }

  public String getStateName() {
    return this.stateName;
  }

  public void validate(ValidationHandler aHandler) {
    new StateValidator(this, aHandler).validate();
  }

  @Override
  public String toString() {
    return "State{" +
           "name='" + stateName + '\'' +
           ", acronym='" + acronym + '\'' +
           '}';
  }
}
