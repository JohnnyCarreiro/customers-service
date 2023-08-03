package com.johnnycarreiro.crs.modules.customer.domain.pagination;

public record SearchQuery(
    Integer page,
    Integer perPage,
    String terms,
    String sort,
    String direction
) {
//  public SearchQuery(Integer page, Integer perPage, String terms, String sort, String direction) {
//    this.page =  page;
//    this.perPage = perPage;
//    this.terms = terms;
//    this.sort = sort;
//    this.direction = direction;
//  }
  public static SearchQuery from(Integer page, Integer perPage, String terms, String sort, String direction) {
    return new SearchQuery(
        (page == null) ? 0 : page,
        (perPage == null) ? 10 : perPage,
        (terms == null) ? "" : terms,
        (sort == null) ? "createdAt" : sort,
        (direction == null) ? "asc" : direction
    );
  }
}
