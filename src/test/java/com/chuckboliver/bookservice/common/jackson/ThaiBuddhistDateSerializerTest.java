package com.chuckboliver.bookservice.common.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.chrono.ThaiBuddhistDate;

import static org.assertj.core.api.Assertions.assertThat;

class ThaiBuddhistDateSerializerTest {

    @Test
    @DisplayName("should serialize ThaiBuddhistDate")
    void shouldSerializeThaiBuddhistDate() throws JsonProcessingException {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(ThaiBuddhistDate.class, new ThaiBuddhistDateSerializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);

        ThaiBuddhistDate thaiBuddhistDate = ThaiBuddhistDate.of(2544, 6, 7);
        String expectedJson = "\"2544-06-07\"";

        String actualJson = objectMapper.writeValueAsString(thaiBuddhistDate);

        assertThat(actualJson)
                .isNotNull()
                .isEqualTo(expectedJson);
    }

}
