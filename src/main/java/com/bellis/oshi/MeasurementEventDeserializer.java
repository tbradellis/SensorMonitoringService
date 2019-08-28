package com.bellis.oshi;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class MeasurementEventDeserializer implements Deserializer {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public MeasurementEvent deserialize(String topic, byte[] eventBytes) {

        ObjectMapper mapper = new ObjectMapper();
        MeasurementEvent event = null;

        try{
            event = mapper.readValue(eventBytes, MeasurementEvent.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return event;
    }

    @Override
    public void close() {

    }
}
