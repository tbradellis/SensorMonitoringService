package com.bellis.oshi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class MeasurementEventSerializerTest {

    @Test
    public void testSerialization(){
        int[] fanSpeeds = {1100, 2000};
        MeasurementEvent event =  new MeasurementEvent(12, 10, fanSpeeds);
        MeasurementEventSerializer serializer = new MeasurementEventSerializer();
        byte[] original = serializer.serialize("test_event", event);
        MeasurementEventDeserializer deserializer = new MeasurementEventDeserializer();
        MeasurementEvent copy = deserializer.deserialize("test_event", original);
        //you'll need your the MeasurementEventDeserializer here.
        assertEquals(event, copy);
        assertNotSame(event, copy);
    }

}
