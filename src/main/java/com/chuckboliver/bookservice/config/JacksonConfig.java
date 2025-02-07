package com.chuckboliver.bookservice.config;

import com.chuckboliver.bookservice.common.jackson.ThaiBuddhistDateDeserializer;
import com.chuckboliver.bookservice.common.jackson.ThaiBuddhistDateSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.chrono.ThaiBuddhistDate;

@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule simpleModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ThaiBuddhistDate.class, new ThaiBuddhistDateSerializer());
        module.addDeserializer(ThaiBuddhistDate.class, new ThaiBuddhistDateDeserializer());
        return module;
    }

}
