package org.firstinspires.ftc.team26923.GalaxyRunner;
public class Pose2D {
    private double x;
    private double y;
    private double heading;

    public Pose2D(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public Pose2D(Pose2D pose) {
        this.x = pose.getX();
        this.y = pose.getY();
        this.heading = pose.getHeading();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

}
