package com.bellis.oshi;

import com.bellis.kafka.KafkaProperties;
import com.bellis.kafka.producer.MetricPublisher;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
//TODO strip java util logging and use sl4j. That's what OSHI uses and it's a dependency anyway
//TODO also there are other dependencies. This fails when packaged as a jar
//https://stackoverflow.com/questions/34061103/import-library-oshi
/* INFO: Initializing SystemInfo
Exception in thread "main" java.lang.NoClassDefFoundError: oshi/SystemInfo
        at com.bellis.oshi.ApplicationCore.main(ApplicationCore.java:41)
Caused by: java.lang.ClassNotFoundException: oshi.SystemInfo
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:582)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)
        ... 1 more
*/
public class ApplicationCore {
    private static final Logger LOGGER = Logger.getLogger("SensorLogger");
    private static String log_location = "logs/sensor.log";
    //TODO add a shutdown hook
    public static void main(String[] args){
        if(args.length > 0){
            log_location = args[0];
        }
        try {
            Handler fileHandler = new FileHandler(log_location);
            LOGGER.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException from Handler", e);
        }

        LOGGER.log(Level.INFO, "Initializing SystemInfo");

        LOGGER.log(Level.INFO,"Checking local system");
        MetricPublisher publisher = new MetricPublisher(KafkaProperties.TOPIC, true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(publisher);

    }

}
