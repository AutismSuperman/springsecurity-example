package com.fulinlin.exception.oauth2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.Map;

public class MyOAuthExceptionJacksonSerializer extends StdSerializer<MyOAuth2Exception> {

    protected MyOAuthExceptionJacksonSerializer() {
        super(MyOAuth2Exception.class);
    }

    @Override
    public void serialize(MyOAuth2Exception value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("code", "401");
        String errorMessage = value.getOAuth2ErrorCode();
        if (errorMessage != null) {
            errorMessage = HtmlUtils.htmlEscape(errorMessage);
        }
        jgen.writeStringField("message", "token无效");
        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jgen.writeStringField(key, add);
            }
        }
        jgen.writeEndObject();
    }
}
