package com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list;
import com.johnnycarreiro.crs.core.application.UseCase;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;

public abstract class ListNaturalPersonUseCase
    extends UseCase<SearchQuery, Pagination<NaturalPersonListOutput>> {
}
