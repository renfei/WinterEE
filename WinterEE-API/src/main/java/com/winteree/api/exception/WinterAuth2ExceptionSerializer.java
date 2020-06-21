package com.winteree.api.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;
import net.renfei.sdk.entity.APIResult;

/**
 * @author RenFei
 */
public class WinterAuth2ExceptionSerializer extends StdSerializer<WinterAuth2Exception> {
    public WinterAuth2ExceptionSerializer() {
        super(WinterAuth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(WinterAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeObject(APIResult.builder()
                .code(value.getStateCode())
                .message(value.getMessage())
                .build());
    }
}
