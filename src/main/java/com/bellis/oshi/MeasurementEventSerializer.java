package com.bellis.oshi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MeasurementEventSerializer implements Serializer {


    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Object event ) {
        byte[] bytes = null;

        ObjectMapper mapper = new ObjectMapper();
        try{
            bytes = mapper.writeValueAsBytes(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    @Override
    public void close() {

    }
}