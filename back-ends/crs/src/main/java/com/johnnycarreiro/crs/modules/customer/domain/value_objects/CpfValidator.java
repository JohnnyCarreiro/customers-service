package com.johnnycarreiro.crs.modules.customer.domain.value_objects;

import com.johnnycarreiro.crs.core.domain.validation.Error;
import com.johnnycarreiro.crs.core.domain.validation.ValidationHandler;
import com.johnnycarreiro.crs.core.domain.validation.Validator;

import java.util.Arrays;
import java.util.Objects;

public class CpfValidator extends Validator {

  private final Cpf cpf;

  public CpfValidator(final Cpf cpf, final ValidationHandler aHandler) {
    super(aHandler);
    this.cpf = cpf;
  }

  @Override
  public void validate() {
    var rawCpf = cpf.getValue();
    if(Objects.isNull(rawCpf)) {
      this.validationHandler().append(new Error(("Invalid CPF")));
      return;
    }
    if (!isValidCpf(rawCpf)) {
      this.validationHandler().append(new Error(("Invalid CPF")));
    }
  }

  private boolean isValidCpf(String cpf) {
    var rawCpf = cleanCpf(cpf);
    if(!hasCorrectLength(rawCpf)) return false;
    if (isBlocked(rawCpf)) return false;
    var digit1 = calculateDigit(rawCpf, 10);
    var digit2 = calculateDigit(rawCpf, 11);
    var calculatedDigits = String.format("%1$d%2$d", digit1, digit2);
//        replaceAll("[\\s]", "");
    var digitsToVerify = rawCpf.substring(9);
    return Objects.equals(calculatedDigits, digitsToVerify);
  }

  private int calculateDigit(String rawCpf, int factor) {
    var total = 0;
    for (String digit : rawCpf.split("")) {
      if (factor > 1) total += Integer.parseInt(digit) * factor--;
    }
    var rest = total % 11;
    return (rest < 2) ? 0 : 11 - rest;

  }

  private boolean isBlocked(String rawCpf) {
    var firstDigit = String.valueOf(rawCpf.charAt(0));
    return Arrays.stream(rawCpf.split("")).allMatch(dig -> Objects.equals(dig, firstDigit));
  }

  private boolean hasCorrectLength(String rawCpf) {
    return rawCpf.length() == 11;
  }

  private String cleanCpf(String rawCpf) {
    return rawCpf.replaceAll("[\\D.]", "");
  }
}
