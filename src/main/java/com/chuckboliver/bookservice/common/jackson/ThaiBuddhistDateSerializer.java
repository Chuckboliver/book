package com.chuckboliver.bookservice.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;

public class ThaiBuddhistDateSerializer extends JsonSerializer<ThaiBuddhistDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void serialize(ThaiBuddhistDate thaiBuddhistDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String formattedDate = thaiBuddhistDate.format(FORMATTER);
        jsonGenerator.writeString(formattedDate);
    }
}
