package org.firstinspires.ftc.team26923.GalaxyRunner;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.Arrays;

public class Encoder {
    private final static int cpsStep = 0x10000;
    private final static int velocityEstimateSize = 100;
    private final DcMotorEx motor;
    private final NanoClock clock;

    private int lastPosition;
    private int changeInPosition = 0;
    private int velocityEstimateIndex;
    private final double[] velocityEstimates;

    public Encoder(DcMotorEx motor) {
        this.motor = motor;
        this.clock = new NanoClock();

        this.lastPosition = 0;
        this.velocityEstimateIndex = 0;
        this.velocityEstimates = new double[velocityEstimateSize];
    }


    public void startTimer() {
        this.clock.reset();
    }


    private int getMultiplier() {
        return motor.getDirection() == DcMotorEx.Direction.FORWARD ? 1 : -1;
    }

    public int getCurrentPosition() {
        int multiplier = getMultiplier();
        int currentPosition = motor.getCurrentPosition() * multiplier;
        if (currentPosition != lastPosition) {
            poll(currentPosition);
        }
        return currentPosition;
    }

    public int getChangeInPosition() {
        return changeInPosition;
    }

    public void poll(int currentPosition) {
        double dt = clock.seconds();
        changeInPosition = currentPosition - lastPosition;
        velocityEstimates[velocityEstimateIndex] = changeInPosition / dt;
        velocityEstimateIndex = (velocityEstimateIndex + 1) % velocityEstimateSize;
        lastPosition = currentPosition;
        clock.reset();
    }

    public void poll() {
        int multiplier = getMultiplier();
        poll(getCurrentPosition() * multiplier);
    }

    // Gets the velocity reported from the motor itself, recommended if you aren't polling velocity
    public double getRawVelocity() {
        int multiplier = getMultiplier();
        return motor.getVelocity() * multiplier;
    }

    public double getCorrectedVelocity() {
        double median = median(velocityEstimates.clone());

        // 16 bits
        int real = (int) getRawVelocity() & 0xFFFF;

        real += ((real % 20) / 4) * cpsStep;
        real += Math.round((median - real) / (5 * cpsStep)) * 5 * cpsStep;
        return real;
    }

    private double median(double[] list) {
        Arrays.sort(list);
        return list[list.length / 2];
    }
}
