package com.bellis;

import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

public class SystemAbstraction {


    private static SystemAbstraction sSystemAbstraction;
    private static SystemInfo si;
    private static HardwareAbstractionLayer hal;
    private static OperatingSystem os;
    private static Sensors sensors;
    private static ComputerSystem cs;
    private SystemAbstraction(){

    }

    public static SystemAbstraction initAndGetSystemAbstraction(){
        if(sSystemAbstraction == null){
            sSystemAbstraction = new SystemAbstraction();
            si = new SystemInfo();
            hal = si.getHardware();
            os = si.getOperatingSystem();
            sensors = hal.getSensors();
            cs = hal.getComputerSystem();
        }
        return sSystemAbstraction;
    }
    public static SystemInfo getSystemInfo() {
        return si;
    }

    public static HardwareAbstractionLayer getHardwareAbstraction() {
        return hal;
    }

    public static OperatingSystem getOS() {
        return os;
    }

    public static Sensors getSensors() {
        return sensors;
    }
    public ComputerSystem getComputerSystem(){
        return this.cs;
    }

    public static void printComputerSystem(){
        System.out.println("Manufacturer: " + cs.getManufacturer());
        System.out.println("model: " + cs.getModel());
        System.out.println("serial number: " + cs.getSerialNumber());

    }

}
