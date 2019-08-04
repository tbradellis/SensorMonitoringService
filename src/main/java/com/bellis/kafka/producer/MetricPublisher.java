package com.bellis.kafka.producer;

import com.bellis.SystemAbstraction;
import com.bellis.kafka.KafkaProperties;
import com.newrelic.api.agent.Trace;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.FloatSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class MetricPublisher implements Runnable {

    private final KafkaProducer<String, Float> producer;
    public static SystemAbstraction systemAbstraction;
    private String topic;

    public MetricPublisher(String topic, Boolean isAsync){
        this.topic = topic;

        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KafkaProperties.KAFKA_SERVER_URL+ ":" + KafkaProperties.KAFKA_SERVER_PORT);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                FloatSerializer.class.getName());
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
        publishCPUTemp();
        publishFanSpeeds();
    }

    @Trace
    public void publishCPUTemp(){
        try{

            ProducerRecord<String, Float> record = new ProducerRecord<>(topic,"Custom/CPU_Temp/Celsius",
                    (float)systemAbstraction.getSensors().getCpuTemperature());
            RecordMetadata metadata = producer.send(record).get();
            System.out.println(metadata.topic() + " " + metadata.offset() + " " + metadata.timestamp());

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Trace
    public void publishFanSpeeds(){

       try{
           int[] fans = systemAbstraction.getSensors().getFanSpeeds();
           for( int i = 0; i < fans.length; i++){

               ProducerRecord<String, Float> record = new ProducerRecord<>(topic,"Custom/fan_spd/fan" + i,
                       (float) fans[i] );
               RecordMetadata metadata = producer.send(record).get();
               System.out.println(metadata.topic() + " " + metadata.offset() + " " + metadata.timestamp());
           }

        }catch(Exception e){
           e.printStackTrace();
       }

    }
    private static SystemAbstraction initSystemAbstraction(){
        if(systemAbstraction == null){
            SystemAbstraction.initAndGetSystemAbstraction();
        }
        return systemAbstraction;
    }
}
