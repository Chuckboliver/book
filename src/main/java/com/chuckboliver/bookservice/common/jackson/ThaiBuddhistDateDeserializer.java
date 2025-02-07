package com.chuckboliver.bookservice.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.chrono.ThaiBuddhistDate;

public class ThaiBuddhistDateDeserializer extends JsonDeserializer<ThaiBuddhistDate> {
    @Override
    public ThaiBuddhistDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateStr = jsonParser.getText();

        String[] dateParts = dateStr.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        return ThaiBuddhistDate.of(year, month, day);
    }
}
