package com.fourty_eight_dps.marclay.core.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public final class BitmapUtil {

  public static String encodeToString(Bitmap bitmap) {
    if (bitmap == null) { return ""; }
    return Base64.encodeToString(encodeToBytes(bitmap), Base64.DEFAULT);
  }

  public static byte[] encodeToBytes(Bitmap bitmap) {
    if (bitmap == null) { return null; }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static Bitmap decodeToBitmap(String encoded) {
    byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }
}
