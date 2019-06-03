package com.bellis.kafka.producer;

import com.bellis.SystemAbstraction;
import com.bellis.kafka.KafkaProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class MetricPublisher implements Runnable {

    private final KafkaProducer<String, Integer> producer;
    public SystemAbstraction sysAb;
    public String topic;

    public MetricPublisher(String topic, Boolean isAsync){
        this.topic = topic;

        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KafkaProperties.KAFKA_SERVER_URL+ ":" + KafkaProperties.KAFKA_SERVER_PORT);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                IntegerSerializer.class.getName());
        producer = new KafkaProducer<String, Integer>(kafkaProps);
        sysAb = initSystemAbstraction();

    }

    @Override
    public void run() {
        while(true){
            publish();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    public void publish(){
        producer.send(new ProducerRecord<String, Integer>(topic,"Custom/CPU_Temp/Celsius",
                (int)sysAb.sensors.getCpuTemperature()));
    }


    private SystemAbstraction initSystemAbstraction(){
        return SystemAbstraction.initAndGetSystemAbstraction();
    }
}
