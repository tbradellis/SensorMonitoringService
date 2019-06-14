package com.bellis;

import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

public class SystemAbstraction {


    private static SystemAbstraction sSystemAbstraction;
    public static SystemInfo si;
    public static HardwareAbstractionLayer hal;
    public static OperatingSystem os;
    public static Sensors sensors;
    public static ComputerSystem cs;
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

}
