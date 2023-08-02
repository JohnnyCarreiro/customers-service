package com.johnnycarreiro.crs.core.domain;

import java.time.Instant;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {
  protected AggregateRoot(final ID id, final Instant createAt,final Instant updatedAt, Instant deletedAt) {
    super(id, createAt, updatedAt, deletedAt);
  }
}
