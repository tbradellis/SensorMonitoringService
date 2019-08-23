package com.bellis.oshi;

public class MeasurementEvent {

    private float cpuTemp;
    private float cpuVoltage;
    private float[] fanSpeed;


    public MeasurementEvent(final float cpuTemp,
            final float cpuVoltage, final float[] fanSpeed){
        this.cpuTemp = cpuTemp;
        this.cpuVoltage = cpuVoltage;
        this.fanSpeed = fanSpeed;
    }

    public float[] getFanSpeed() {
        return fanSpeed;
    }

    public float getCpuVoltage() {
        return cpuVoltage;
    }

    public float getCpuTemp() {
        return cpuTemp;
    }
}
