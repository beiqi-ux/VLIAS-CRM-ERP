package com.example.vliascrm.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Boolean到Integer的Jackson序列化器
 * 将Boolean值序列化为Integer：false -> 0, true -> 1, null -> 0
 */
public class BooleanToIntSerializer extends JsonSerializer<Boolean> {

    @Override
    public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNumber(0);  // null值序列化为0，而不是null
        } else {
            gen.writeNumber(value ? 1 : 0);
        }
    }
} 