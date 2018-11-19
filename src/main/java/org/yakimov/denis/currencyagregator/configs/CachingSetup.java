package org.yakimov.denis.currencyagregator.configs;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.stereotype.Component;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.util.concurrent.TimeUnit;

@Component
public class CachingSetup implements JCacheManagerCustomizer {
    @Override
    public void customize(javax.cache.CacheManager cacheManager) {
        cacheManager.createCache("values", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 5))).setStoreByValue(false).setStatisticsEnabled(false));
    }
}
