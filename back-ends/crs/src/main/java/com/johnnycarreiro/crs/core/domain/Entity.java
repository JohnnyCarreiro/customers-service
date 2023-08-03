package com.johnnycarreiro.crs.core.domain;

import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> {

  protected final ID id;
  private final Instant createdAt;
  private  Instant updatedAt;

  private Instant deletedAt;

  protected Entity(final ID id, Instant createdAt, Instant updatedAt, Instant deletedAt){
    Objects.requireNonNull(id, "`ID` could not be null");
    Objects.requireNonNull(createdAt, "A Creation Date must be provided");
    Objects.requireNonNull(updatedAt, "A Update Date must be provided");
    this.deletedAt = deletedAt;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.id = id;
  }

  public abstract void validate(ValidationHandler handler);

  public ID getId() {
    return id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  protected void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setDeletedAt(Instant deletedAt) {
    this.deletedAt = deletedAt;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Entity<?> entity = (Entity<?>) o;
    return Objects.equals(getId(), entity.getId())
        && Objects.equals(getCreatedAt(), entity.getCreatedAt())
        && Objects.equals(getUpdatedAt(), entity.getUpdatedAt());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCreatedAt(), getUpdatedAt());
  }

  @Override
  public String toString() {
    return
        "id=" + id.getValue() +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt;
  }
}
