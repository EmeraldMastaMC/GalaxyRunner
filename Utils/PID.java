package org.firstinspires.ftc.team26923.GalaxyRunner.Utils;

public class PID {
    private double integralSum = 0;
    private final NanoClock timer = new NanoClock();
    private double lastError = 0.0;

    private final double kP;
    private final double kI;
    private final double kD;

    public PID(double p, double i, double d) {
        kP = p;
        kI = i;
        kD = d;
    }

    public double control(double reference, double current) {
        double error = reference - current;
        double dt = timer.seconds();
        integralSum += error * dt;
        double derivative = (error - lastError) / dt;
        lastError = error;

        timer.reset();

        return (error / 10 * kP) + (derivative * kD) + (integralSum * kI);
    }
}
