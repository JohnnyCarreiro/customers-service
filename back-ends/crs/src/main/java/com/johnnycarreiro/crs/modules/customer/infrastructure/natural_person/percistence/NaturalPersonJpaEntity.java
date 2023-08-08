package com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.percistence;

import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence.ContactJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "NaturalPerson")
@Table(name = "NaturalPerson")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NaturalPersonJpaEntity {

  @Id
  @Column(name = "Id", columnDefinition = "BINARY(16)", nullable = false)
  private UUID id;

  @Column(name = "Name", nullable = false)
  private String name;

  @Column(name = "CPF", nullable = false)
  private String cpf;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinTable(name = "NaturalPersonContact",
    joinColumns =
      { @JoinColumn(name = "CustomerId", referencedColumnName = "Id") },
    inverseJoinColumns =
      { @JoinColumn(name = "ContactID", referencedColumnName = "Id") })
  private ContactJpaEntity contact;

  @Column(name = "CreatedAt", columnDefinition = "DATETIME2", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Instant createdAt;

  @Column(name = "UpdatedAt", columnDefinition = "DATETIME2", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Instant updatedAt;

  @Column(name = "DeletedAt", columnDefinition = "DATETIME2")
  @Temporal(TemporalType.TIMESTAMP)
  private Instant deletedAt;

  public static NaturalPersonJpaEntity from(NaturalPerson aPerson) {
    return new NaturalPersonJpaEntity(
       UUID.fromString(aPerson.getId().getValue()),
        aPerson.getName(),
        aPerson.getCpf().getValue(),
        ContactJpaEntity.from(aPerson.getContact()),
        aPerson.getCreatedAt(),
        aPerson.getUpdatedAt(),
        aPerson.getDeletedAt()
    );
  }

  public NaturalPerson toAggregate() {
    return NaturalPerson.with(
        getId().toString(),
        getName(),
        getCpf(),
        getContact().toEntity(),
        getCreatedAt(),
        getUpdatedAt(),
        getDeletedAt()
    );
  }
}
