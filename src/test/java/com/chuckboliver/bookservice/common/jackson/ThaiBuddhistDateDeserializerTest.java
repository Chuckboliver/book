package com.chuckboliver.bookservice.common.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.chrono.ThaiBuddhistDate;

import static org.assertj.core.api.Assertions.assertThat;

class ThaiBuddhistDateDeserializerTest {

    @Test
    @DisplayName("should deserialize ThaiBuddhistDate")
    void shouldDeserializeThaiBuddhistDate() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ThaiBuddhistDate.class, new ThaiBuddhistDateDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);

        String dateString = "2544-06-07";

        ThaiBuddhistDate expectedThaiBuddhistDate = ThaiBuddhistDate.of(2544, 6, 7);

        ThaiBuddhistDate actualThaiBuddhistDate = objectMapper.convertValue(dateString, ThaiBuddhistDate.class);

        assertThat(actualThaiBuddhistDate)
                .isNotNull()
                .isEqualTo(expectedThaiBuddhistDate);
    }
}