package com.johnnycarreiro.crs.core.domain;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;

import java.util.Objects;
import java.util.UUID;

public class EntityId extends Identifier {
  private final String value;

  private  EntityId(final String value) {
    this.value = value;
  }

  public static EntityId from(final String id){
    try {
      return new EntityId(UUID.fromString(id).toString().toLowerCase());
    } catch (Exception ex) {
      return new EntityId(null);
    }
  }

  public static EntityId from(UUID id){
    try {
      return new EntityId(id.toString().toLowerCase());
    } catch (Exception ex) {
      return new EntityId(null);
    }
  }

  public static EntityId create(){
    return new EntityId(UUID.randomUUID().toString());
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public void validate(ValidationHandler aHandler) {
    new EntityIdValidator(this, aHandler).validate();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EntityId entityId = (EntityId) o;
    return Objects.equals(getValue(), entityId.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }

  @Override
  public String toString() {
    return
        "value='" + value + '\'';
  }
}
