package com.johnnycarreiro.crs.modules.customer.domain.entities.address;

import com.johnnycarreiro.crs.core.domain.Entity;
import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class Address extends Entity<EntityId> {

  private String street;
  private Integer number;
  private String complement;
  private String area;
  private String city;
  private State state;
  private String cep;
  private UnitType unitType;
  private EntityId customerId;
  private Address(
      final String id,
      final String street,
      final Integer number,
      final String complement,
      final String area,
      final String city,
      final String state,
      final String cep,
      final String unitType,
      final String customerId,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    super(EntityId.from(id), createdAt, updatedAt, deletedAt);
    this.street = street;
    this.number = number;
    this.complement = complement;
    this.area = area;
    this.city = city;
    this.state = State.from(state);
    this.cep = cep;
    this.unitType = UnitType.getByLabel(unitType);
    this.customerId = EntityId.from(customerId);
  }

  public static Address create(
      final String street,
      final Integer number,
      final String complement,
      final String area,
      final String city,
      final String state,
      final String cep,
      final String unitType,
      final String customerId
  ) {
    return new Address(
        EntityId.create().getValue(),
        street,
        number,
        complement,
        area,
        city,
        state,
        cep,
        unitType,
        customerId,
        Instant.now(),
        Instant.now(),
        null
    );
  }

  public static Address with(
      final String anId,
      final String aStreet,
      final Integer aNumber,
      final String aComplement,
      final String anArea,
      final String aCity,
      final String aState,
      final String aCep,
      final String anUnitType,
      final String aCustomerId,
      final Instant aCreationDate,
      final Instant anUpdateDate,
      final Instant aDeletionDate
  ) {
    return new Address(
        anId,
        aStreet,
        aNumber,
        aComplement,
        anArea,
        aCity,
        aState,
        aCep,
        anUnitType,
        aCustomerId,
        aCreationDate,
        anUpdateDate,
        aDeletionDate
    );
  }

  public Address update(
      final String aStreet,
      final Integer aNumber,
      final String aComplement,
      final String anArea,
      final String aCity,
      final String aState,
      final String aCep,
      final String anUnitType,
      final String aCustomerId
  ) {
    this.street = aStreet;
    this.number = aNumber;
    this.complement = aComplement;
    this.area = anArea;
    this.city = aCity;
    this.state = State.from(aState);
    this.cep = aCep;
    this.unitType = UnitType.getByLabel(anUnitType);
    this.customerId = EntityId.from(aCustomerId);
    setUpdatedAt(Instant.now());
    return this;
  }

  public Address update(final Address anAddress ) {
    this.street = anAddress.getStreet();
    this.number = anAddress.getNumber();
    this.complement = anAddress.getComplement();
    this.area = anAddress.getArea();
    this.city = anAddress.getCity();
    this.state = anAddress.getState();
    this.cep = anAddress.getCep();
    this.unitType = anAddress.getUnitType();
    this.customerId = anAddress.getCustomerId();
    setUpdatedAt(Instant.now());
    return this;
  }
  public Address delete() {
    this.setDeletedAt(Instant.now());
    this.setUpdatedAt(Instant.now());
    return this;
  }

  public Address restore() {
    this.setDeletedAt(null);
    this.setUpdatedAt(Instant.now());
    return this;
  }

  @Override
  public void validate(ValidationHandler handler) {
    new AddressValidator(this, handler).validate();
    customerId.validate(handler);
    state.validate(handler);
    unitType.validate(handler);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Address address = (Address) o;
    return Objects.equals(getStreet(), address.getStreet())
           && Objects.equals(getNumber(), address.getNumber())
           && Objects.equals(getComplement(), address.getComplement())
           && Objects.equals(getArea(), address.getArea())
           && Objects.equals(getCity(), address.getCity())
           && getState() == address.getState()
           && Objects.equals(getCep(), address.getCep())
           && getUnitType() == address.getUnitType()
           && Objects.equals(getCustomerId(), address.getCustomerId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      super.hashCode(),
      getStreet(),
      getNumber(),
      getComplement(),
      getArea(),
      getCity(),
      getState(),
      getCep(),
      getUnitType(),
      getCustomerId()
    );
  }

  @Override
  public String toString() {
    return "Address{" +
           super.toString() +
           ", street='" + street + '\'' +
           ", number='" + number + '\'' +
           ", complement='" + complement + '\'' +
           ", area='" + area + '\'' +
           ", city='" + city + '\'' +
           ", state='" + state.toString() + '\'' +
           ", cep='" + cep + '\'' +
           ", unitType=" + unitType +
           '}';
  }
}
