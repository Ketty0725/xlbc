package com.ketty.gifloadinglibrary;

import android.graphics.Bitmap;


public class BitmapUtil {

  public static int getPixColor(Bitmap src) {
    int pixelColor;
    pixelColor = src.getPixel(5, 5);
    return pixelColor;
  }
}