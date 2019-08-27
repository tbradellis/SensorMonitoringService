package com.bellis;

import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

public class SystemAbstraction {


    private volatile static SystemAbstraction sSystemAbstraction;
    private static SystemInfo si;
    private static HardwareAbstractionLayer hal;
    private static OperatingSystem os;
    private static Sensors sensors;
    private static ComputerSystem cs;
    private SystemAbstraction(){

    }
    //Use double checked locking to ensure that
    //Interleaving with multiple threads is handled correctly
    //This might be overkill as I could have just synchronized the entire
    //block, but double checked locking reduces overhead and
    //ensures we only attempt to lock on the first time
    //Another option would have been to use eager initialization.

    public static synchronized SystemAbstraction initAndGetSystemAbstraction(){
        if(sSystemAbstraction == null){
            synchronized (SystemAbstraction.class){
                if(sSystemAbstraction == null){
                    sSystemAbstraction = new SystemAbstraction();
                    si = new SystemInfo();
                    hal = si.getHardware();
                    os = si.getOperatingSystem();
                    sensors = hal.getSensors();
                    cs = hal.getComputerSystem();
                }
            }
        }
        return sSystemAbstraction;
    }
    public SystemInfo getSystemInfo() {
        return this.si;
    }

    public HardwareAbstractionLayer getHardwareAbstraction() {
        return this.hal;
    }

    public OperatingSystem getOS() {
        return this.os;
    }

    public Sensors getSensors() {
        return this.sensors;
    }
    public ComputerSystem getComputerSystem(){
        return this.cs;
    }

    public void printComputerSystem(){
        System.out.println("Manufacturer: " + this.cs.getManufacturer());
        System.out.println("model: " + this.cs.getModel());
        System.out.println("serial number: " + this.cs.getSerialNumber());

    }

}
