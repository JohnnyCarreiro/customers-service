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
  private EntityId customerId;
  private String phoneNumber;
  private String email;
  private List<Address> addresses;

  private Contact(
    final EntityId customerId,
    final String phoneNumber,
    final String email
  ) {
    super(EntityId.create(), Instant.now(), Instant.now(), null);
    this.customerId = customerId;
    this.phoneNumber = phoneNumber == null ? null : phoneNumber.replaceAll("[\\D.]", "");
    this.email = email;
    this.addresses = new ArrayList<>();
  }
  private Contact(
    final EntityId customerId,
    final String phoneNumber,
    final String email,
    final List<Address> addresses
  ) {
    super(EntityId.create(), Instant.now(), Instant.now(), null);
    this.customerId = customerId;
    this.phoneNumber = phoneNumber == null ? null : phoneNumber.replaceAll("[\\D.]", "");
    this.email = email;
    this.addresses = addresses;
  }
  private Contact(
    final EntityId id,
    final EntityId customerId,
    final String phoneNumber,
    final String email,
    final List<Address> addresses,
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
    final EntityId aCustomerId,
    final String aPhoneNumber,
    final String anEmail,
    final List<Address> anAddresses
  ) {
    return new Contact(
      aCustomerId,
      aPhoneNumber,
      anEmail,
      anAddresses
    );
  }

  public static Contact create(
    final EntityId aCustomerId,
    final String aPhoneNumber,
    final String anEmail,
    final Address anAddress
  ) {
    return new Contact(
      aCustomerId,
      aPhoneNumber,
      anEmail,
      List.of(anAddress)
    );
  }

  public static Contact create(
    final EntityId aCustomerId,
    final String aPhoneNumber,
    final String anEmail
  ) {
    return new Contact(
      aCustomerId,
      aPhoneNumber,
      anEmail
    );
  }

  public static Contact with(
    final EntityId anId,
    final EntityId aCustomerId,
    final String aPhoneNumber,
    final String anEmail,
    final List<Address> anAddresses,
    final Instant aCreationDate,
    final Instant anUpdateDate,
    final Instant aDeletionDate
  ) {
    return new Contact(
      anId,
      aCustomerId,
      aPhoneNumber,
      anEmail,
      anAddresses,
      aCreationDate,
      anUpdateDate,
      aDeletionDate
    );
  }

  public Contact update(
    final EntityId aCustomerId,
    final String aPhoneNumber,
    final String anEmail,
    final List<Address> anAddresses
  ) {
    this.customerId = aCustomerId;
    this.phoneNumber = aPhoneNumber.replaceAll("[\\D.]", "");
    this.email = anEmail;
    this.addresses= anAddresses;
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
