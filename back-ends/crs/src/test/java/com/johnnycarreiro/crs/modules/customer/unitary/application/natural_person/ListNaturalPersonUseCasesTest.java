package com.johnnycarreiro.crs.modules.customer.unitary.application.natural_person;

import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list.DefaultListNaturalPersonUseCase;
import com.johnnycarreiro.crs.modules.customer.application.natural_person.retrieve.list.NaturalPersonListOutput;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPerson;
import com.johnnycarreiro.crs.modules.customer.domain.entities.natural_person.NaturalPersonGateway;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.SearchQuery;
import com.johnnycarreiro.crs.modules.customer.unitary.application.UseCaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("List NaturalPerson UC Tests Suite")
public class ListNaturalPersonUseCasesTest extends UseCaseTest {
  @InjectMocks
  private DefaultListNaturalPersonUseCase useCase;

  @Mock
  private NaturalPersonGateway personGateway;
  @Override
  protected List<Object> getMocks() {
    return List.of(personGateway);
  }

  @Test
  @DisplayName("Valid Query - Returns Natural Person Output")
  public void givenValidQuery_whenCallListNaturalPerson_thenReturnsNaturalPersonList() {
    List<NaturalPerson> naturalPeople = List.of(NaturalPerson.create("John Doe", "935.411.347-80"));

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
        SearchQuery.from(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    final var expectedPagination =
        new Pagination<>(expectedPage, expectedPerPage, naturalPeople.size(), naturalPeople);

    final var expectedItemsCount = 1;
    final var expectedResult = expectedPagination.map(NaturalPersonListOutput::from);

    when(personGateway.findAll(eq(aQuery)))
        .thenReturn(expectedPagination);
    final var sut = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, sut.items().size());
    Assertions.assertEquals(expectedResult, sut);
    Assertions.assertEquals(expectedPage, sut.currentPage());
    Assertions.assertEquals(expectedPerPage, sut.perPage());
    Assertions.assertEquals(naturalPeople.size(), sut.total());
  }

  @Test
  @DisplayName("Empty Query - Returns Natural Person Output")
  public void givenEmptyQuery_whenCallListNaturalPerson_thenReturnsNaturalPersonList() {
    List<NaturalPerson> naturalPeople = List.of(NaturalPerson.create("John Doe", "935.411.347-80"));

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery =
        SearchQuery.from(null, null, null, null, null);
    final var expectedPagination =
        new Pagination<>(expectedPage, expectedPerPage, naturalPeople.size(), naturalPeople);

    final var expectedItemsCount = 1;
    final var expectedResult = expectedPagination.map(NaturalPersonListOutput::from);

    when(personGateway.findAll(eq(aQuery)))
        .thenReturn(expectedPagination);
    final var sut = useCase.execute(aQuery);

    Assertions.assertEquals(expectedTerms, aQuery.terms());
    Assertions.assertEquals(expectedSort, aQuery.sort());
    Assertions.assertEquals(expectedDirection, aQuery.direction());

    Assertions.assertEquals(expectedItemsCount, sut.items().size());
    Assertions.assertEquals(expectedResult, sut);
    Assertions.assertEquals(expectedPage, sut.currentPage());
    Assertions.assertEquals(expectedPerPage, sut.perPage());
    Assertions.assertEquals(naturalPeople.size(), sut.total());
  }
}
