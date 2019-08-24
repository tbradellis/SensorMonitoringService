package com.bellis.oshi;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;

public class MeasurementEventSerializerTest {

    @Test
    public void testSerialization(){
        int[] fanSpeeds = {1100, 2000};
        MeasurementEvent event =  new MeasurementEvent(12, 10, fanSpeeds);
        MeasurementEventSerializer serializer = new MeasurementEventSerializer();
        byte[] original = serializer.serialize("test_event", event);
        //you'll need your the MeasurementEventDeserializer here.
        Serializable copy = SerializationUtils.clone(original);
        assertEquals(original, copy);
    }

}
