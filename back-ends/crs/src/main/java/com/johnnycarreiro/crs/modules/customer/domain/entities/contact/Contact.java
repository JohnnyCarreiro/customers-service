package com.johnnycarreiro.crs.modules.customer.domain.entities.contact;

import com.johnnycarreiro.crs.core.domain.Entity;
import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Contact extends Entity<EntityId> {
  private String phoneNumber;
  private String email;
  private List<Address> addresses;
  private EntityId customerId;

  private Contact(
    final String phoneNumber,
    final String email,
    final EntityId customerId
  ) {
    super(EntityId.create(), Instant.now(), Instant.now(), null);
    this.customerId = customerId;
    this.phoneNumber = phoneNumber == null ? null : phoneNumber.replaceAll("[\\D.]", "");
    this.email = email;
    this.addresses = new ArrayList<>();
  }
  private Contact(
    final String phoneNumber,
    final String email,
    final List<Address> addresses,
    final EntityId customerId
  ) {
    super(EntityId.create(), Instant.now(), Instant.now(), null);
    this.customerId = customerId;
    this.phoneNumber = phoneNumber == null ? null : phoneNumber.replaceAll("[\\D.]", "");
    this.email = email;
    this.addresses = addresses;
  }
  private Contact(
    final EntityId id,
    final String phoneNumber,
    final String email,
    final List<Address> addresses,
    final EntityId customerId,
    final Instant createAt,
    final Instant updatedAt,
    final Instant deletedAt
  ) {
    super(id, createAt, updatedAt, deletedAt);
    this.customerId = customerId;
    this.phoneNumber = phoneNumber == null ? null : phoneNumber.replaceAll("[\\D.]", "");
    this.email = email;
    this.addresses = addresses;
  }

  public static Contact create(
    final String aPhoneNumber,
    final String anEmail,
    final List<Address> anAddresses,
    final EntityId aCustomerId
  ) {
    return new Contact(
      aPhoneNumber,
      anEmail,
      anAddresses,
      aCustomerId
    );
  }

  public static Contact create(
    final String aPhoneNumber,
    final String anEmail,
    final Address anAddress,
    final EntityId aCustomerId
  ) {
    return new Contact(
      aPhoneNumber,
      anEmail,
      List.of(anAddress),
      aCustomerId
    );
  }

  public static Contact create(
    final String aPhoneNumber,
    final String anEmail,
    final EntityId aCustomerId
  ) {
    return new Contact(
      aPhoneNumber,
      anEmail,
      aCustomerId
    );
  }

  public static Contact with(
    final EntityId anId,
    final String aPhoneNumber,
    final String anEmail,
    final List<Address> anAddresses,
    final EntityId aCustomerId,
    final Instant aCreationDate,
    final Instant anUpdateDate,
    final Instant aDeletionDate
  ) {
    return new Contact(
      anId,
      aPhoneNumber,
      anEmail,
      anAddresses,
      aCustomerId,
      aCreationDate,
      anUpdateDate,
      aDeletionDate
    );
  }

  public Contact update(
    final String aPhoneNumber,
    final String anEmail,
    final List<Address> anAddresses,
    final EntityId aCustomerId
  ) {
    this.phoneNumber = aPhoneNumber.replaceAll("[\\D.]", "");
    this.email = anEmail;
    this.addresses= anAddresses;
    this.customerId = aCustomerId;
    this.setUpdatedAt(Instant.now());
    return this;
  }

  public Contact addAddress(List<Address> addresses) {
    this.addresses.addAll(addresses);
    this.setUpdatedAt(Instant.now());
    return this;
  }

  public Contact addAddress(Address anAddress) {
    this.addresses.add(anAddress);
    setUpdatedAt(Instant.now());
    return this;
  }
  @Override
  public void validate(ValidationHandler aHandler) {
    new ContactValidator(this, aHandler).validate();
  }

  public EntityId getCustomerId() {
    return customerId;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public List<Address> getAddresses() {
    return addresses;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Contact contact = (Contact) o;
    return Objects.equals(getCustomerId(), contact.getCustomerId())
           && Objects.equals(getPhoneNumber(), contact.getPhoneNumber())
           && Objects.equals(getEmail(), contact.getEmail())
           && Objects.equals(getAddresses(), contact.getAddresses());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      super.hashCode(),
      getCustomerId(),
      getPhoneNumber(),
      getEmail(),
      getAddresses());
  }

  @Override
  public String toString() {
    return "Contact{" +
           super.toString() +
           "customerId=" + customerId +
           ", phoneNumber='" + phoneNumber + '\'' +
           ", email='" + email + '\'' +
           ", addresses=" + addresses +
           '}';
  }
}
