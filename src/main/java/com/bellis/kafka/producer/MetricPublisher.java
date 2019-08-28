package com.bellis.kafka.producer;

import com.bellis.SystemAbstraction;
import com.bellis.kafka.KafkaProperties;
import com.bellis.oshi.MeasurementEvent;
import com.newrelic.api.agent.Trace;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static com.bellis.kafka.KafkaProperties.MEASUREMENT_EVENT_SERIALIZER;

public class MetricPublisher implements Runnable {

    private final KafkaProducer<String, MeasurementEvent> producer;
    private SystemAbstraction systemAbstraction;
    private String topic;

    public MetricPublisher(String topic, Boolean isAsync){
        this.topic = topic;

        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KafkaProperties.KAFKA_SERVER_URL+ ":" + KafkaProperties.KAFKA_SERVER_PORT);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                MEASUREMENT_EVENT_SERIALIZER);
        producer = new KafkaProducer<>(kafkaProps);
        systemAbstraction = initSystemAbstraction();

    }

    @Override
    public void run() {
        systemAbstraction.printComputerSystem();



        while(true){
            publish();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
    @Trace(dispatcher=true)
    public void publish(){
        takeMeasurements();
    }
    //TODO change the publisher methods so that they add metrics to a MeasurementEvent
    @Trace
    public void takeMeasurements(){
        try{

            final float cpuTemp = (float)systemAbstraction.getSensors().getCpuTemperature();
            final float cpuVoltage = (float)systemAbstraction.getSensors().getCpuVoltage();
            final int[] fanSpeeds = systemAbstraction.getSensors().getFanSpeeds();

            MeasurementEvent event = new MeasurementEvent(cpuTemp, cpuVoltage, fanSpeeds);
            ProducerRecord<String, MeasurementEvent> record = new ProducerRecord<>(topic, event );

            RecordMetadata metadata = producer.send(record).get();
            System.out.println(metadata.topic() + " " + metadata.offset() + " " + metadata.timestamp());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private SystemAbstraction initSystemAbstraction(){
        if(systemAbstraction == null){
            this.systemAbstraction = SystemAbstraction.initAndGetSystemAbstraction();
        }
        return systemAbstraction;
    }
}
