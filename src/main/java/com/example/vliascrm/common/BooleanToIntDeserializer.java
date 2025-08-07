package com.example.vliascrm.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Integer/Boolean到Boolean的Jackson反序列化器
 * 支持接收：0/1（整数）、false/true（布尔值）
 * 转换为：Boolean对象
 */
public class BooleanToIntDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        if (node.isNull()) {
            return null;
        }
        
        if (node.isBoolean()) {
            return node.asBoolean();
        }
        
        if (node.isNumber()) {
            int value = node.asInt();
            return value != 0;
        }
        
        if (node.isTextual()) {
            String text = node.asText().toLowerCase();
            return "true".equals(text) || "1".equals(text);
        }
        
        return false;
    }
} 