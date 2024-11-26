package org.firstinspires.ftc.team26923.GalaxyRunner;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;


public class Galaxy extends Component {
    // How close to the target you want to be before the moveTo function returns
    private static final double TARGET_TOLERANCE = 1.0; // 1mm
    // Below what speed you want to be traveling before the moveTo function returns
    private static final double SPEED_TOLERANCE = 5.0; // 5mm/s
    // How close to the target heading you want to be before the moveTo function returns
    private static final double TARGET_HEADING_TOLERANCE = ((Math.PI * 2) / 360) * (1.0 / 2.0); // Half a degree
    // Below what angular speed you want to be traveling before the moveTo function returns
    private static final double ROTATIONAL_SPEED_TOLERANCE = ((Math.PI * 2) / 360) * 3.0; // Three degrees/s
    private static final boolean ODOMETRY_ENABLED = true;
    private final boolean showTelemetry;
    private final Mecanum mecanum;
    private final Odometry odometry;
    private ArrayList<Pollable> components;
    private final PID lateralPID = new PID(0.01, 0.0, 0.0);
    private final PID axialPID = new PID(0.01, 0.0, 0.0);
    private final PID yawPID = new PID(0.01, 0.0, 0.0);
    private final Spinlock poseCalculationLock = new Spinlock();
    private final Spinlock targetLock = new Spinlock();
    private Telemetry telemetry;
    private final NanoClock runtime = new NanoClock();
    private boolean autonDrivetrain = true;
    private final Spinlock autonDrivetrainLock = new Spinlock();
    private final Pose2D target = new Pose2D(0, 0, 0);

    public Galaxy(HardwareMap hardwareMap) {
        mecanum = new Mecanum(hardwareMap);
        odometry = new Odometry(hardwareMap, poseCalculationLock);
        this.showTelemetry = false;
    }

    public Galaxy(HardwareMap hardwareMap, ArrayList<Pollable> components) {
        mecanum = new Mecanum(hardwareMap);
        odometry = new Odometry(hardwareMap, poseCalculationLock);
        this.showTelemetry = false;
        this.components = components;
    }

    public Galaxy(HardwareMap hardwareMap, Pose2D initialPose) {
        mecanum = new Mecanum(hardwareMap);
        odometry = new Odometry(hardwareMap, initialPose, poseCalculationLock);
        this.target.setX(initialPose.getX());
        this.target.setY(initialPose.getY());
        this.target.setHeading(initialPose.getHeading());
        this.showTelemetry = false;
    }

    public Galaxy(HardwareMap hardwareMap, Pose2D initialPose, ArrayList<Pollable> components) {
        mecanum = new Mecanum(hardwareMap);
        odometry = new Odometry(hardwareMap, initialPose, poseCalculationLock);
        this.target.setX(initialPose.getX());
        this.target.setY(initialPose.getY());
        this.target.setHeading(initialPose.getHeading());
        this.showTelemetry = false;
        this.components = components;
    }

    public Galaxy(HardwareMap hardwareMap, Telemetry telemetry) {
        mecanum = new Mecanum(hardwareMap);
        odometry = new Odometry(hardwareMap, poseCalculationLock);
        this.showTelemetry = true;
        this.telemetry = telemetry;
    }

    public Galaxy(HardwareMap hardwareMap, Telemetry telemetry, ArrayList<Pollable> components) {
        mecanum = new Mecanum(hardwareMap);
        odometry = new Odometry(hardwareMap, poseCalculationLock);
        this.showTelemetry = true;
        this.telemetry = telemetry;
        this.components = components;
    }

    public Galaxy(HardwareMap hardwareMap, Pose2D initialPose, Telemetry telemetry) {
        mecanum = new Mecanum(hardwareMap);
        odometry = new Odometry(hardwareMap, initialPose, poseCalculationLock);
        this.target.setX(initialPose.getX());
        this.target.setY(initialPose.getY());
        this.target.setHeading(initialPose.getHeading());
        this.showTelemetry = true;
        this.telemetry = telemetry;
    }

    public Galaxy(HardwareMap hardwareMap, Pose2D initialPose, Telemetry telemetry, ArrayList<Pollable> components) {
        mecanum = new Mecanum(hardwareMap);
        odometry = new Odometry(hardwareMap, initialPose, poseCalculationLock);
        this.target.setX(initialPose.getX());
        this.target.setY(initialPose.getY());
        this.target.setHeading(initialPose.getHeading());
        this.showTelemetry = true;
        this.telemetry = telemetry;
        this.components = components;
    }

    public Galaxy(HardwareMap hardwareMap, Gamepad gamepad) {
        mecanum = new Mecanum(hardwareMap, gamepad);
        odometry = new Odometry(hardwareMap, poseCalculationLock);
        this.showTelemetry = false;
        disableMecanumAuton();
    }

    public Galaxy(HardwareMap hardwareMap, Gamepad gamepad, ArrayList<Pollable> components) {
        mecanum = new Mecanum(hardwareMap, gamepad);
        odometry = new Odometry(hardwareMap, poseCalculationLock);
        this.showTelemetry = false;
        this.components = components;
        disableMecanumAuton();
    }

    public Galaxy(HardwareMap hardwareMap, Gamepad gamepad, Pose2D initialPose) {
        mecanum = new Mecanum(hardwareMap, gamepad);
        odometry = new Odometry(hardwareMap, initialPose, poseCalculationLock);
        this.target.setX(initialPose.getX());
        this.target.setY(initialPose.getY());
        this.target.setHeading(initialPose.getHeading());
        this.showTelemetry = false;
        disableMecanumAuton();
    }

    public Galaxy(HardwareMap hardwareMap, Gamepad gamepad, Pose2D initialPose, ArrayList<Pollable> components) {
        mecanum = new Mecanum(hardwareMap, gamepad);
        odometry = new Odometry(hardwareMap, initialPose, poseCalculationLock);
        this.target.setX(initialPose.getX());
        this.target.setY(initialPose.getY());
        this.target.setHeading(initialPose.getHeading());
        this.showTelemetry = false;
        this.components = components;
        disableMecanumAuton();
    }

    public Galaxy(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry) {
        mecanum = new Mecanum(hardwareMap, gamepad);
        odometry = new Odometry(hardwareMap, poseCalculationLock);
        this.showTelemetry = true;
        this.telemetry = telemetry;
        disableMecanumAuton();
    }

    public Galaxy(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry, ArrayList<Pollable> components) {
        mecanum = new Mecanum(hardwareMap, gamepad);
        odometry = new Odometry(hardwareMap, poseCalculationLock);
        this.showTelemetry = true;
        this.telemetry = telemetry;
        this.components = components;
        disableMecanumAuton();
    }

    public Galaxy(HardwareMap hardwareMap, Gamepad gamepad, Pose2D initialPose, Telemetry telemetry) {
        mecanum = new Mecanum(hardwareMap, gamepad);
        odometry = new Odometry(hardwareMap, initialPose, poseCalculationLock);
        this.target.setX(initialPose.getX());
        this.target.setY(initialPose.getY());
        this.target.setHeading(initialPose.getHeading());
        this.showTelemetry = true;
        this.telemetry = telemetry;
        disableMecanumAuton();
    }

    public Galaxy(HardwareMap hardwareMap, Gamepad gamepad, Pose2D initialPose, Telemetry telemetry, ArrayList<Pollable> components) {
        mecanum = new Mecanum(hardwareMap, gamepad);
        odometry = new Odometry(hardwareMap, initialPose, poseCalculationLock);
        this.target.setX(initialPose.getX());
        this.target.setY(initialPose.getY());
        this.target.setHeading(initialPose.getHeading());
        this.showTelemetry = true;
        this.telemetry = telemetry;
        this.components = components;
        disableMecanumAuton();
    }


    public void disableMecanumAuton() {
        double lockID = autonDrivetrainLock.acquireLock();
        autonDrivetrain = false;
        autonDrivetrainLock.releaseLock(lockID);
    }

    private boolean mecanumAuton() {
        double lockID = autonDrivetrainLock.acquireLock();
        boolean ret = autonDrivetrain;
        autonDrivetrainLock.releaseLock(lockID);
        return ret;
    }

    public void strafeLeft(double distance) {
        moveTo(new Pose2D(target.getX() - distance, target.getY(), target.getHeading()));
    }

    public void strafeRight(double distance) {
        moveTo(new Pose2D(target.getX() + distance, target.getY(), target.getHeading()));
    }

    public void forward(double distance) {
        moveTo(new Pose2D(target.getX(), target.getY() + distance, target.getHeading()));
    }

    public void backward(double distance) {
        moveTo(new Pose2D(target.getX(), target.getY() - distance, target.getHeading()));
    }

    public void rotateCounterClockwise(double rad) {
        moveTo(new Pose2D(target.getX(), target.getY(), target.getHeading() + rad));
    }

    public void rotateClockwise(double rad) {
        moveTo(new Pose2D(target.getX(), target.getY(), target.getHeading() - rad));
    }

    public void moveTo(Pose2D target) {
        if (ODOMETRY_ENABLED) {
            double targetLockID = targetLock.acquireLock();
            this.target.setX(target.getX());
            this.target.setY(target.getY());
            this.target.setHeading(target.getHeading());
            targetLock.releaseLock(targetLockID);

            double poseCalculationLockID = poseCalculationLock.acquireLock();
            double xCurrent = odometry.getPose().getX();
            double yCurrent = odometry.getPose().getY();
            double headingCurrent = odometry.getPose().getHeading();
            double speed = odometry.getSpeed();
            double angularSpeed = odometry.getAngularSpeed();
            poseCalculationLock.releaseLock(poseCalculationLockID);

            double distanceFromTarget = Math.sqrt(Math.pow(xCurrent - target.getX(), 2) + Math.pow(yCurrent - target.getY(), 2));
            double headingDifference = Math.abs(headingCurrent - target.getHeading());

            while ((distanceFromTarget > TARGET_TOLERANCE) && (headingDifference > TARGET_HEADING_TOLERANCE) && (speed > SPEED_TOLERANCE) && (angularSpeed > ROTATIONAL_SPEED_TOLERANCE)) {
                poseCalculationLockID = poseCalculationLock.acquireLock();
                xCurrent = odometry.getPose().getX();
                yCurrent = odometry.getPose().getY();
                headingCurrent = odometry.getPose().getHeading();
                speed = odometry.getSpeed();
                angularSpeed = odometry.getAngularSpeed();
                poseCalculationLock.releaseLock(poseCalculationLockID);

                distanceFromTarget = Math.sqrt(Math.pow(xCurrent - target.getX(), 2) + Math.pow(yCurrent - target.getY(), 2));
                headingDifference = Math.abs(headingCurrent - target.getHeading());
            }
        }
    }

    private void initComponents() {
        for (Pollable component : components) {
            component.initialize();
        }
    }

    @Override
    public void init() {
        initComponents();
    }

    @Override
    public void deinit() {
        mecanum.stop();
        if (ODOMETRY_ENABLED) {
            odometry.stop();
        }
        for (Pollable component : components) {
            component.stop();
        }
    }

    @Override
    public void preStart() {
        mecanum.start();
        if (ODOMETRY_ENABLED) {
            odometry.start();
        }
        for (Pollable component : components) {
            component.startPolling();
        }
        runtime.reset();
    }

    @Override
    public void poll() {
        if (ODOMETRY_ENABLED) {
            double targetLockID = targetLock.acquireLock();
            double xTarget = target.getX();
            double yTarget = target.getY();
            double headingTarget = target.getHeading();
            targetLock.releaseLock(targetLockID);

            double poseCalculationLockID = poseCalculationLock.acquireLock();
            double xCurrent = odometry.getPose().getX();
            double yCurrent = odometry.getPose().getY();
            double headingCurrent = odometry.getPose().getHeading();
            double speed = odometry.getSpeed();
            poseCalculationLock.releaseLock(poseCalculationLockID);

            double lateralPower = lateralPID.control(xTarget, xCurrent);
            double axialPower = axialPID.control(yTarget, yCurrent);
            double yawPower = yawPID.control(headingTarget, headingCurrent);

            if (mecanumAuton()) {
                mecanum.setTarget(axialPower, lateralPower, yawPower);
            } else {
                double lockID = targetLock.acquireLock();
                target.setX(xCurrent);
                target.setY(yCurrent);
                target.setHeading(headingCurrent);
                targetLock.releaseLock(lockID);
            }

            EncoderPool encoders = odometry.getEncoders();
            ArrayList<Double> encoderVelocities = encoders.getEncoderVelocities();
            ArrayList<Double> encoderPositions = encoders.getEncoderPositions();

            if (showTelemetry) {
                telemetry.addData("X", xCurrent);
                telemetry.addData("Y", yCurrent);
                telemetry.addData("Heading", headingCurrent);
                telemetry.addData("Speed", speed);
                telemetry.addLine();
                telemetry.addData("Target X", xTarget);
                telemetry.addData("Target Y", yTarget);
                telemetry.addData("Target Heading", headingTarget);
                telemetry.addLine();
                telemetry.addData("Lateral Power", lateralPower);
                telemetry.addData("Axial Power", axialPower);
                telemetry.addData("Yaw Power", yawPower);
                telemetry.addLine();
                telemetry.addData("Left Encoder Position", encoderPositions.get(0));
                telemetry.addData("Right Encoder Position", encoderPositions.get(1));
                telemetry.addData("Front Encoder Position", encoderPositions.get(2));
                telemetry.addLine();
                telemetry.addData("Left Encoder Velocity", encoderVelocities.get(0));
                telemetry.addData("Right Encoder Velocity", encoderVelocities.get(1));
                telemetry.addData("Front Encoder Velocity", encoderVelocities.get(2));
                telemetry.update();
            }
        }
    }
}
