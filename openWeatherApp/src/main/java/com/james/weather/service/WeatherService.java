package com.james.weather.service;


import org.springframework.stereotype.Service;
import com.james.weather.model.Winds;


@Service
public interface WeatherService {

  Winds getWindDetail(String zipCode);

  Winds invalidZip(String zipCode);

  String evictCache();

}
