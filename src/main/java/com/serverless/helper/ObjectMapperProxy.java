package com.serverless.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProxy {
    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
