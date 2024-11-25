package org.firstinspires.ftc.teamcode.GalaxyRunner;

public class NanoClock {
    private long startTime;

    public NanoClock() {
        startTime = System.nanoTime();
    }

    public double seconds() {
        return (System.nanoTime() - startTime) / 1_000_000_000.0;
    }

    public double milliseconds() {
        return (System.nanoTime() - startTime) / 1_000_000.0;
    }

    public double microseconds() {
        return (System.nanoTime() - startTime) / 1_000.0;
    }

    public double nanoseconds() {
        return (double) (System.nanoTime() - startTime);
    }

    public void reset() {
        startTime = System.nanoTime();
    }
}
