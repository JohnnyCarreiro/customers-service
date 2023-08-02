package com.johnnycarreiro.crs.modules.customer.domain.entities.address;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum UnitType {
  RESIDENTIAL(1, "Residential"),
  COMMERCIAL(2, "Commercial"),
  HAS_ERROR;

  private static final Map<String, UnitType> BY_LABEL = new HashMap<>();

  static {
    for (UnitType u : values()) {
      BY_LABEL.put(u.label, u);
    }
  }

  private static final Map<Integer, UnitType> BY_VALUE = new HashMap<>();

  static {
    for (UnitType u : values()) {
      BY_VALUE.put(u.value, u);
    }
  }

  private final Integer value;
  private final String label;

  UnitType(int value, String label) {
    this.value = value;
    this.label = label;
  }

  UnitType() {
    this.value = null;
    this.label = null;
  }

  public static UnitType from(Object value) {
    if(value == null) return UnitType.HAS_ERROR;
    if(value instanceof Integer) {
      return UnitType.fromValue((Integer) value);
    }
    if(value instanceof String) {
      return UnitType.fromLabel((String) value);
    }
    return UnitType.HAS_ERROR;
  }

  public static UnitType fromLabel(String label) {
    if(isNullOrEmpty(label)) return UnitType.HAS_ERROR;
    for(UnitType unit : UnitType.values()) {
      if(unit.label.equalsIgnoreCase(label)) {
        return unit;
      }
    }
    return UnitType.HAS_ERROR;
  }

  public static UnitType fromValue(Integer value) {
    if(value == null) return UnitType.HAS_ERROR;
    for(UnitType unit : UnitType.values()) {
      if(Objects.equals(unit.value, value)) {
        return unit;
      }
    }
    return UnitType.HAS_ERROR;
  }

  public static UnitType getByLabel(String label) {
    if(isNullOrEmpty(label)) return UnitType.HAS_ERROR;
    return BY_LABEL.get(label);
  }

  public static UnitType getByValue(Integer value) {
    if(value == null) return UnitType.HAS_ERROR;
    return BY_VALUE.get(value);
  }

  public void validate(ValidationHandler aHandler) {
    new UnitTypeValidator(this, aHandler).validate();
  }

  private static boolean isNullOrEmpty(String value) {
    if(value == null) return true;
    return value.isBlank();
  }

  public String getLabel() {
    return this.label;
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "UnitType{" +
           "value=" + value +
           ", label='" + label + '\'' +
           '}';
  }
}
