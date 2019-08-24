package com.bellis.oshi;

public class MeasurementEvent {

    private float cpuTemp;
    private float cpuVoltage;
    private int[] fanSpeed;


    public MeasurementEvent(final float cpuTemp,
            final float cpuVoltage, final int[] fanSpeed){
        this.cpuTemp = cpuTemp;
        this.cpuVoltage = cpuVoltage;
        this.fanSpeed = fanSpeed;
    }

    public int[] getFanSpeed() {
        return fanSpeed;
    }

    public float getCpuVoltage() {
        return cpuVoltage;
    }

    public float getCpuTemp() {
        return cpuTemp;
    }
}
