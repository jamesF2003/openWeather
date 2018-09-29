package com.james.weather.controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.james.weather.model.Winds;
import com.james.weather.service.WeatherService;

@RestController
@RequestMapping("/api/v1")
public class WeatherController {

  @Autowired
  WeatherService service;

  public WeatherController(WeatherService weatherService) {
    this.service = weatherService;
  }

  @RequestMapping(value = "/wind/{zipCode}", method = RequestMethod.GET)
  public Winds geWindDetailFmZip(@PathVariable("zipCode") String zipCode) {
    if (!zipValidate(zipCode)) {
      return service.invalidZip(zipCode);

    }
    return service.getWindDetail(zipCode);
  }

  private boolean zipValidate(String zip) {
    String regex = "^[0-9]{5}(?:-[0-9]{4})?$";

    Pattern pattern = Pattern.compile(regex);

    Matcher matcher = pattern.matcher(zip);

    return matcher.matches();
  }

  @RequestMapping("/clearCache")
  public String evictCache() {
    return service.evictCache();
  }

}
