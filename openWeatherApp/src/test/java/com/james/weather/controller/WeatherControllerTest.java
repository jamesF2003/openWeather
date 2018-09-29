package com.james.weather.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import com.james.weather.config.EhCacheConfig;
import com.james.weather.model.Winds;
import com.james.weather.service.WeatherService;

public class WeatherControllerTest {

  @Autowired
  EhCacheConfig cache;
  @Mock
  WeatherService weatheService;

  WeatherController controller;
  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @Before
  public void setup() {
    controller = new WeatherController(weatheService);
  }

  @Test
  public void geWindDetailFmZipTest_InvalidZipCode() {
    String zipCode = "abcdefg";
    Winds invalid = new Winds("invalid zip: " + zipCode, "");
    when(weatheService.invalidZip(zipCode)).thenReturn(new Winds("invalid zip: " + zipCode, ""));
    assertEquals(invalid.toString(), controller.geWindDetailFmZip(zipCode).toString());
    verify(weatheService).invalidZip(zipCode);
  }

  @Test
  public void geWindDetailFmZipTest_ValidZipCode() {
    String zipCode = "77477";
    Winds invalid = new Winds("110.0", "2.1");
    when(weatheService.getWindDetail(zipCode)).thenReturn(new Winds("110.0", "2.1"));
    assertEquals(invalid.toString(), controller.geWindDetailFmZip(zipCode).toString());
    verify(weatheService).getWindDetail(zipCode);

  }

  @Test
  public void evictCacheTest() {

    when(weatheService.evictCache()).thenReturn("is cleared");
    assertEquals("is cleared", controller.evictCache());
    verify(weatheService).evictCache();

  }
}
