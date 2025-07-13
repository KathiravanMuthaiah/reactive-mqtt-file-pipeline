package com.bauto.spring.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

public class CloseableUtils {

  public static <T extends Closeable> Consumer<T> safeCloser() {
    return t -> {
      try {
        t.close();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      } catch (Exception e) {
        throw new RuntimeException("SafeClosure failed", e);
      }
    };
  }


}
