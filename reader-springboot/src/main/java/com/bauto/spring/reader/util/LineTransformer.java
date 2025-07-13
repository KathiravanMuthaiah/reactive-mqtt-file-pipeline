package com.bauto.spring.reader.util;

import java.util.Arrays;

public class LineTransformer {

  /**
   * Takes a comma-separated string of numeric values, computes the sum, and appends it at the end.
   *
   * Example: "4,5,6" → "4,5,6,15"
   */
  public static String appendSum(String line) {
    if (line == null || line.trim().isEmpty()) {
      throw new IllegalArgumentException("Input line is empty");
    }

    String[] parts = line.split(",");
    int sum = 0;

    try {
      sum = Arrays.stream(parts).map(String::trim).mapToInt(Integer::parseInt).sum();
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Line contains non-numeric values: " + line, e);
    }

    return line + "," + sum;
  }
}
