package ru.practicum.ewmservice.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ru.practicum.statsservice.client"})
public class StatsClientConfig {
}
