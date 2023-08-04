package com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person;

import com.johnnycarreiro.crs.core.domain.AggregateRoot;
import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.domain.value_objects.Cpf;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
public class NaturalPerson extends AggregateRoot<EntityId> {
  private String name;
  private Cpf cpf;
  private Contact contact;
  private NaturalPerson(final String name, final String cpf, final Contact contact) {
    super(EntityId.create(), Instant.now(), Instant.now(), null);
    this.name = name;
    this.cpf = Cpf.create(cpf);
    this.contact = contact;
  }

  private NaturalPerson(
      final String id,
      final String name,
      final String cpf,
      final Contact contact,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    super(EntityId.from(id), createdAt, updatedAt, deletedAt);
    this.name = name;
    this.cpf = Cpf.create(cpf);
    this.contact = contact;
  }
  private NaturalPerson(
    final String id,
    final String name,
    final String cpf,
    final Instant createdAt,
    final Instant updatedAt,
    final Instant deletedAt
  ) {
    super(EntityId.from(id), createdAt, updatedAt, deletedAt);
    this.name = name;
    this.cpf = Cpf.create(cpf);
    this.contact = null;
  }

  private NaturalPerson(NaturalPerson aPerson) {
    super(aPerson.getId(), aPerson.getCreatedAt(), aPerson.getUpdatedAt(), aPerson.getDeletedAt());
    this.name = aPerson.getName();
    this.cpf = aPerson.getCpf();
    this.contact = aPerson.getContact();
  }

  public static NaturalPerson create(final String name, final String cpf) {
    return new NaturalPerson(name, cpf, null);
  }

  public static NaturalPerson create(final String name, final String cpf, final Contact contact) {
    return new NaturalPerson(name, cpf, contact);
  }

  public static NaturalPerson with(
      final String anId,
      final String aName,
      final String aCpf,
      final Instant aCreationDate,
      final Instant anUpdateDate,
      final Instant aDeletionDate
  ) {
    return new NaturalPerson(anId, aName, aCpf, aCreationDate, anUpdateDate, aDeletionDate);
  }
  public static NaturalPerson with(
    final String anId,
    final String aName,
    final String aCpf,
    final Contact contact,
    final Instant aCreationDate,
    final Instant anUpdateDate,
    final Instant aDeletionDate
  ) {
    return new NaturalPerson(anId, aName, aCpf, contact, aCreationDate, anUpdateDate, aDeletionDate);
  }

  public static NaturalPerson with(NaturalPerson aPerson) {
    return new NaturalPerson(aPerson);
  }

  public void addContact(Contact aContact) {
    this.contact = aContact;
  }

  public NaturalPerson update(String name, String cpf) {
    this.name = name;
    this.cpf = Cpf.create(cpf);
    setUpdatedAt(Instant.now());
    return this;
  }

  public NaturalPerson update(final String aName, final String aCpf, final Contact aContact) {
    this.name = aName;
    this.cpf = Cpf.create(aCpf);
    this.contact = aContact;
    setUpdatedAt(Instant.now());
    return this;
  }

  public NaturalPerson delete() {
    this.setDeletedAt(Instant.now());
    this.setUpdatedAt(Instant.now());
    return this;
  }

  public NaturalPerson restore() {
    this.setDeletedAt(null);
    this.setUpdatedAt(Instant.now());
    return this;
  }

  @Override
  public void validate(final ValidationHandler aHandler) {
    new NaturalPersonValidator(this, aHandler).validate();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    NaturalPerson that = (NaturalPerson) o;
    return Objects.equals(getName(), that.getName()) && Objects.equals(getCpf(), that.getCpf());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getName(), getCpf());
  }

  @Override
  public String toString() {
    return "NaturalPerson{" +
        super.toString() +
        ", name='" + name + '\'' +
        ", cpf=" + cpf.toString() +
        ", contact=" + contact.toString() +
        '}';
  }
}

/*
 * TODO: Add constructor for address and factory method for address,
 * don't forget to add customerId to the address.
 * Or create new Customer entity in Use Case and then create Address with created Entity Id and then add address.
 * Maybe Validation Handler may be instantiate on execution time by dependency injection;
 * */
