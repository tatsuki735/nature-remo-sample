package com.mizo0203.natureremoapisample.data;

/**
 * IR 信号
 */
public class IRSignal {

    /**
     * Freq
     * <p>
     * minimum: 30
     * maximum: 80
     * default: 38
     * <p>
     * IR sub carrier frequency.
     */
    public final int freq;

    /**
     * IR signal consists of on/off of sub carrier frequency.
     * When receiving IR, Remo measures on to off, off to on time intervals and records the time interval sequence.
     * When sending IR, Remo turns on/off the sub carrier frequency with the provided time interval sequence.
     */
    public final int[] data;

    /**
     * Format
     * <p>
     * default: us
     * <p>
     * Format and unit of values in the data array.
     * Fixed to us, which means each integer of data array is in microseconds.
     */
    public final String format;

    public IRSignal(int freq, int[] data, String format) {
        this.freq = freq;
        this.data = data;
        this.format = format;
    }

}
