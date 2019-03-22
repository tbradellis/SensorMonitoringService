package com.bellis.oshi;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;

public class ApplicationCore {
    private static final String CPU_TMP_FAHRENHEIT = "Custom/CPU_Temp/Fahrenheit";
    private static final String CPU_TMP_CELSIUS = "Custom/CPU_Temp/Celsius";
    private static final String FAN_SPD_FAN1 = "Custom/fan_spd/fan1";
    private static final String FAN_SPD_FAN2 = "Custom/fan_spd/fan2";



    public static void main(String[] args){
        System.out.println("Initializing...");
        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        System.out.println("Checking local system...");
        printComputerSystem(hal.getComputerSystem());

        while(true){
            System.out.println("Checking Sensors... ");
            printSensorInfo(hal.getSensors());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private static void printComputerSystem(final ComputerSystem cs){
        System.out.println("Manufacturer: " + cs.getManufacturer());
        System.out.println("model: " + cs.getModel());
        System.out.println("serialnumber: " + cs.getSerialNumber());
    }
    @Trace(dispatcher = true)
    private static void printSensorInfo(Sensors sensors){
        float fahrenheit = (float)(((9.0/5.0) * sensors.getCpuTemperature()) + 32);
        float celsius = (float)sensors.getCpuTemperature();
        NewRelic.recordMetric(CPU_TMP_CELSIUS,celsius );
        NewRelic.recordMetric(CPU_TMP_FAHRENHEIT,fahrenheit );
        System.out.println("Sensors:");
        System.out.format(" CPU Temperature: %.1f°C%n", sensors.getCpuTemperature());
        System.out.format( " CPU Temperature: %.1f°F%n", fahrenheit );
        int[] fans = sensors.getFanSpeeds();
        int fan1 = fans[0];
        int fan2 = fans[1];
        NewRelic.recordMetric(FAN_SPD_FAN1, fan1);
        NewRelic.recordMetric(FAN_SPD_FAN2, fan2);
        System.out.println(" Fan Speeds: " + Arrays.toString(sensors.getFanSpeeds()));
        System.out.format(" CPU Voltage: %.6fV%n", sensors.getCpuVoltage());
    }


}
