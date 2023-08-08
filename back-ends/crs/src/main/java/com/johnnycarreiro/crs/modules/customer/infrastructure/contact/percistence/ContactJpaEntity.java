package com.johnnycarreiro.crs.modules.customer.infrastructure.contact.percistence;

import com.johnnycarreiro.crs.core.domain.EntityId;
import com.johnnycarreiro.crs.modules.customer.domain.entities.contact.Contact;
import com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence.AddressJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Contact")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactJpaEntity {
  @Id
  @Column(name = "Id", columnDefinition = "BINARY(16)", nullable = false)
  private UUID id;

  @Column(name = "PhoneNumber", nullable = false)
  private String phoneNumber;

  @Column(name = "Email", nullable = false)
  private String email;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "ContactAddress",
    joinColumns = @JoinColumn(name = "ContactId", referencedColumnName = "Id"),
    inverseJoinColumns = @JoinColumn(name = "AddressId",
      referencedColumnName = "Id")
  )
  private List<AddressJpaEntity> addresses;

  @JoinColumn(name = "CustomerId", columnDefinition = "BINARY(16)", nullable = false)
  private UUID customerId;

  @Column(name = "CreatedAt", columnDefinition = "DATETIME2", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Instant createdAt;

  @Column(name = "UpdatedAt", columnDefinition = "DATETIME2", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Instant updatedAt;

  @Column(name = "DeletedAt", columnDefinition = "DATETIME2")
  @Temporal(TemporalType.TIMESTAMP)
  private Instant deletedAt;

  public static ContactJpaEntity from(Contact aContact) {
    return new ContactJpaEntity(
      UUID.fromString(aContact.getId().getValue()),
      aContact.getPhoneNumber(),
      aContact.getEmail(),
      aContact.getAddresses().stream().map(AddressJpaEntity::from).toList(),
      UUID.fromString(aContact.getCustomerId().getValue()),
      aContact.getCreatedAt(),
      aContact.getUpdatedAt(),
      aContact.getDeletedAt()
    );
  }
  public  Contact toEntity() {
    return Contact.with(
      EntityId.from(getId()),
      getPhoneNumber(),
      getEmail(),
      getAddresses().stream().map(AddressJpaEntity::toEntity).toList(),
      EntityId.from(getCustomerId()),
      getCreatedAt(),
      getUpdatedAt(),
      getDeletedAt()
    );
  }
}
