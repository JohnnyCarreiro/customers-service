package com.johnnycarreiro.crs.core;

import com.johnnycarreiro.crs.core.domain.validation.Error;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {

  public static boolean patternMatches(String valueToVerify, String regexPattern) {
    return Pattern.compile(regexPattern)
      .matcher(valueToVerify)
      .matches();
  }
  public static boolean isBlocked(String number) {
    var firstDigit = String.valueOf(number.charAt(0));
    return Arrays.stream(number.split("")).allMatch(dig -> Objects.equals(dig, firstDigit));
  }

  public static  String validateStringConstraints(String value, String valueName, int min, int max) {
    if(isNull(value)) return String.format("`%s` shouldn't be Null", valueName);
    if(isBlank(value)) return String.format("`%s` shouldn't be Empty", valueName);
    if(isInvalidLength(value, min, max)) return String.format("`%s` must have at least %2$d and less than %3$d characters", valueName, min, max);
    return null;
  }


  private static boolean isNull(Object value) {
    return Objects.isNull(value);
  }
  private static boolean isBlank(String value) {
    return value.trim().isBlank();
  }

  private static boolean isInvalidLength(String value, int min, int max) {
    var length = value.trim().length();
    return (length < min || length > max);
  }
}
