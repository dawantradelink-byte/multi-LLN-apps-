package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun layoutThemeOption_parsingAndFallback() {
    val glass = com.example.data.model.LayoutThemeOption.fromId("framer_glass")
    assertEquals(com.example.data.model.LayoutThemeOption.FRAMER_GLASS, glass)

    val aurora = com.example.data.model.LayoutThemeOption.fromId("motionsight_aurora")
    assertEquals(com.example.data.model.LayoutThemeOption.MOTIONSIGHT_AURORA, aurora)

    val editorial = com.example.data.model.LayoutThemeOption.fromId("framer_editorial")
    assertEquals(com.example.data.model.LayoutThemeOption.FRAMER_EDITORIAL, editorial)

    val fallback = com.example.data.model.LayoutThemeOption.fromId("unknown_xyz")
    assertEquals(com.example.data.model.LayoutThemeOption.MOTIONSIGHT_AURORA, fallback)
  }
}
