package com.fourty_eight_dps.marclay.core.util;

public final class StringUtil {

  public static String nullToEmpty(CharSequence original) {
    return original == null ? "" : original.toString();
  }
}
