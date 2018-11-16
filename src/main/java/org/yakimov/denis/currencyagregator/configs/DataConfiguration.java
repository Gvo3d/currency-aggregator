package org.yakimov.denis.currencyagregator.configs;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ComponentScan({ "org.yakimov.denis.currencyagregator.*" })
public class DataConfiguration {
}
