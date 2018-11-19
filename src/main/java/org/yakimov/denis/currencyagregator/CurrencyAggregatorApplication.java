package org.yakimov.denis.currencyagregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CurrencyAggregatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyAggregatorApplication.class, args);
    }
}
