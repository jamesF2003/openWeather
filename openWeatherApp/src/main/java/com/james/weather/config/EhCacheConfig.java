package com.james.weather.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties.EhCache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;



@Configuration
@EnableCaching
public class EhCacheConfig {

  @Bean
  public CacheManager cacheManager() {
    return new EhCacheCacheManager(cacheManagerFatory().getObject());
  }

  @Bean
  public EhCacheManagerFactoryBean cacheManagerFatory() {
    EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
    factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
    factoryBean.setShared(true);
    return factoryBean;

  }

  public void clearCache() {
    CacheManager cacheManager = cacheManager();
    EhCache cache = (EhCache) cacheManager.getCache("");

  }
}
