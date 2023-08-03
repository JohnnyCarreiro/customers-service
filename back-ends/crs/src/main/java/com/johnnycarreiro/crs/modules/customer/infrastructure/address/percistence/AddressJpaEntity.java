package com.johnnycarreiro.crs.modules.customer.infrastructure.address.percistence;

import com.johnnycarreiro.crs.modules.customer.domain.entities.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "Address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressJpaEntity {
  @Id
  @Column(name = "Id", columnDefinition = "BINARY(16)", nullable = false)
  private UUID id;

  @Column(name = "Street", nullable = false)
  private String street;

  @Column(name = "Number", nullable = false)
  private Integer number;

  @Column(name = "Complement")
  private String complement;

  @Column(name = "Area")
  private String area;

  @Column(name = "City", nullable = false)
  private String city;

  @Column(name = "State", nullable = false)
  private String state;

  @Column(name = "CEP", nullable = false)
  private String cep;

  @Column(name = "UnitType", nullable = false)
  private String unitType;

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

  public static AddressJpaEntity from(final Address anAddress) {
    return new AddressJpaEntity(
        UUID.fromString(anAddress.getId().getValue()),
        anAddress.getStreet(),
        anAddress.getNumber(),
        anAddress.getComplement(),
        anAddress.getArea(),
        anAddress.getCity(),
        anAddress.getState().getAcronym(),
        anAddress.getCep(),
        anAddress.getUnitType().getLabel(),
        UUID.fromString(anAddress.getCustomerId().getValue()),
        anAddress.getCreatedAt(),
        anAddress.getUpdatedAt(),
        anAddress.getDeletedAt()
    );
  }

  public Address toEntity() {
    return Address.with(
        getId().toString(),
        getStreet(),
        getNumber(),
        getComplement(),
        getArea(),
        getCity(),
        getState(),
        getCep(),
        getUnitType(),
        getCustomerId().toString(),
        getCreatedAt(),
        getUpdatedAt(),
        getDeletedAt()
    );
  }
}
