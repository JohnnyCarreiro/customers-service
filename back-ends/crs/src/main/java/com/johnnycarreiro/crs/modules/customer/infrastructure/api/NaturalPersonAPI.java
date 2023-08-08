package com.johnnycarreiro.crs.modules.customer.infrastructure.api;

import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.create.CreateNaturalPersonAPIRequest;
import com.johnnycarreiro.crs.modules.customer.domain.pagination.Pagination;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.NaturalPersonAPIResponse;
import com.johnnycarreiro.crs.modules.customer.infrastructure.natural_person.models.update.UpdateNaturalPersonAPIRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("natural_persons")
public interface NaturalPersonAPI {
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  ResponseEntity<?> createNaturalPerson(@RequestBody CreateNaturalPersonAPIRequest anInput);

  @GetMapping
  Pagination<?> listNaturalPerson(
      @RequestParam(name = "search", required = false, defaultValue = "") final String search,
      @RequestParam(name = "page", required = false, defaultValue = "0") final Integer page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") final Integer perPage,
      @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
      @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
  );

  @GetMapping(
    value = "{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  NaturalPersonAPIResponse getById(@PathVariable(name = "id") final String id);

  @PutMapping(
    value = "{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  ResponseEntity<?> updateById(
    @PathVariable(name = "id") final String id,
    @RequestBody final UpdateNaturalPersonAPIRequest anInput);
}
