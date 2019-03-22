package com.bellis.oshi;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ApplicationCore {
    private static final Logger LOGGER = Logger.getLogger("SensorLogger");
    private static final String CPU_TMP_FAHRENHEIT = "Custom/CPU_Temp/Fahrenheit";
    private static final String CPU_TMP_CELSIUS = "Custom/CPU_Temp/Celsius";
    private static final String RPM_SPD_FAN1 = "Custom/fan_spd/fan1";
    private static final String RPM_SPD_FAN2 = "Custom/fan_spd/fan2";
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

        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        LOGGER.log(Level.INFO,"Checking local system");
        printComputerSystem(hal.getComputerSystem());

        while(true){
            sendSensorInfo(hal.getSensors());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "sleep interrupted", e);
            }
        }


    }

    private static void printComputerSystem(final ComputerSystem cs){
        LOGGER.log(Level.INFO, "Manufacturer: " + cs.getManufacturer());
        LOGGER.log(Level.INFO,"model: " + cs.getModel());
        LOGGER.log(Level.INFO,"serialnumber: " + cs.getSerialNumber());
    }
    //TODO separate possibly separate each metric type into different methods to track time of each call
    @Trace(dispatcher = true)
    private static void sendSensorInfo(Sensors sensors){
        LOGGER.log(Level.INFO, "Sending metrics");
        float celsius = (float)sensors.getCpuTemperature();
        float fahrenheit = (float)(((9.0/5.0) * celsius) + 32);
        NewRelic.recordMetric(CPU_TMP_CELSIUS,celsius );
        NewRelic.recordMetric(CPU_TMP_FAHRENHEIT,fahrenheit );
        LOGGER.log(Level.FINEST,"Checking Sensors:");
        LOGGER.log(Level.FINEST, "CPU Temperature: %1f°C%n", celsius );
        LOGGER.log(Level.FINEST, "CPU Temperature: %1f°F%n", fahrenheit );

        //TODO this should be implemented to handle different #s of fans
        //Possible use a case statement for 0-4
        int[] fans = sensors.getFanSpeeds();
        int fan1 = fans[0];
        int fan2 = fans[1];
        NewRelic.recordMetric(RPM_SPD_FAN1, fan1);
        NewRelic.recordMetric(RPM_SPD_FAN2, fan2);
        LOGGER.log(Level.FINEST,"Fan Speeds: ", Arrays.toString(fans));
    }
}
