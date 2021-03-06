package com.bellis.kafka;

public class KafkaProperties {
//TODO this should all be moved into a config file

    public static final String TOPIC = "sensor-data";
    public static final String KAFKA_SERVER_URL = "localhost";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String HARDWARE_GROUP = "nr-group";
    public static final String MEASUREMENT_EVENT_SERIALIZER = "com.bellis.oshi.MeasurementEventSerializer";


    private KafkaProperties() {}

}
