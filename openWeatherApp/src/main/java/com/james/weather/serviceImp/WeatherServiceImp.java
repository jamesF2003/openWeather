package com.james.weather.serviceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.james.weather.config.EhCacheConfig;
import com.james.weather.model.Winds;
import com.james.weather.service.WeatherService;

@Service
public class WeatherServiceImp implements WeatherService {

  @Autowired
  EhCacheConfig cache;

  private static final String CACHENAME = "windCache";
  private static final String APPID = "c5a274e6e8957f1aa4f2457d0e624ae4";
  private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?zip=";
  private Logger logger = LoggerFactory.getLogger(WeatherService.class);

  @Override
  @Cacheable(value = CACHENAME, key = "#zipCode", sync = true)
  public Winds getWindDetail(String zipCode) {
    logger.info("WeatherService: processing weather request for area with zipcode: " + zipCode);
    HashMap<String, Object> windMap = getWeatherData(zipCode);
    return new Winds(windMap == null ? " " : windMap.get("deg").toString(),
        windMap == null ? " " : windMap.get("speed").toString());
  }

  private HashMap<String, Object> getWeatherData(String zipCode) {
    HashMap<String, Object> resultsMap = null;
    HashMap<String, Object> windMap = null;
    BufferedReader reader = null;
    try {
      URL url = new URL(WEATHER_URL + zipCode + "&APPID=" + APPID);
      URLConnection conn = url.openConnection();

      reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line = null;
      StringBuilder results = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        results.append(line);
      }
      resultsMap = jsonToMap(results.toString());
      windMap = jsonToMap(resultsMap.get("wind").toString());

    } catch (MalformedURLException e) {
      logger.error(e.getMessage());
    } catch (IOException e) {
      logger.error(e.getMessage());
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          logger.error(e.getMessage());
        }
      }
    }
    return windMap;
  }

  @Override
  public Winds invalidZip(String zipCode) {
    return new Winds("invalid zip: " + zipCode, "");
  }

  private HashMap<String, Object> jsonToMap(String json) {

    TypeToken<HashMap<String, Object>> token = new TypeToken<HashMap<String, Object>>() {};
    Gson gson = new Gson();
    HashMap<String, Object> map = gson.fromJson(json, token.getType());
    return map;
  }

  @CacheEvict(value = CACHENAME)
  public String evictCache() {

    CacheManager cacheManager = cache.cacheManager();
    cacheManager.getCache(CACHENAME).clear();
    boolean cleared = cacheManager.getCache(CACHENAME).get(CACHENAME) == null;
    String result = cleared ? "is cleared" : "not cleared";
    logger.info("WeatherService: cache of " + CACHENAME + " " + result);
    return result;
  }

}
