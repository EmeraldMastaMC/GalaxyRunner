package org.firstinspires.ftc.team26923.GalaxyRunner;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Odometry extends Component {
    /* CHANGE THESE VALUES FOR YOUR ROBOT */
    private final double TICKS_PER_REVOLUTION = 8192.0;
    private final double WHEEL_RADIUS = 0.94488189 * 25.4;
    private final double GEAR_RATIO = 1.0;
    private final double LATERAL_DISTANCE = 7.796 * 25.4; // mm; distance between one of the wheels (left or right) and the center of rotation;
    private final double FRONT_DISTANCE = 7.796 * 25.4; // mm; distance between the lateral wheel, and the center of rotation of the robot
    private final double HEADING_MULTIPLIER = 1.0;
    private final double LATERAL_MULTIPLIER = 1.0;
    private final Direction LEFT_ENCODER_DIRECTION = Direction.FORWARD;
    private final Direction RIGHT_ENCODER_DIRECTION = Direction.FORWARD;
    private final Direction FRONT_ENCODER_DIRECTION = Direction.FORWARD;

    private void setEncoders(HardwareMap hardwareMap) {
        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "left_front_drive"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "right_front_drive"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "right_back_drive"));
    }

    private final NanoClock timer = new NanoClock();
    private double xPosition = 0.0;
    private double yPosition = 0.0;
    private double speed = 0.0;
    private double angularSpeed = 0.0;
    private final Spinlock encoderPollLock = new Spinlock();
    private Spinlock poseCalculationLock = new Spinlock();
    private double heading = 0.0; // Radians
    private double prevHeading = heading;
    private Encoder leftEncoder, rightEncoder, frontEncoder;
    private final EncoderPool encoders = new EncoderPool(leftEncoder, rightEncoder, frontEncoder, encoderPollLock);

    // CHANGE THESE VALUES


    public EncoderPool getEncoders() {
        return encoders;
    }

    public Odometry(HardwareMap hardwareMap) {
        setEncoders(hardwareMap);
    }

    public Odometry(HardwareMap hardwareMap, Spinlock poseCalculationLock) {
        setEncoders(hardwareMap);
        this.poseCalculationLock = poseCalculationLock;
    }

    public Odometry(HardwareMap hardwareMap, Pose2D initialPose) {
        setEncoders(hardwareMap);
        this.xPosition = initialPose.getX();
        this.yPosition = initialPose.getY();
        this.heading = initialPose.getHeading();
        this.prevHeading = this.heading;
    }

    public Odometry(HardwareMap hardwareMap, Pose2D initialPose, Spinlock poseCalculationLock) {
        setEncoders(hardwareMap);
        this.poseCalculationLock = poseCalculationLock;
        this.xPosition = initialPose.getX();
        this.yPosition = initialPose.getY();
        this.heading = initialPose.getHeading();
        this.prevHeading = this.heading;
    }


    private void startEncoderPolling() {
        encoders.start();
    }

    private void stopEncoderPolling() {
        encoders.stop();
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngularSpeed() {
        return angularSpeed;

    }

    public Pose2D getPose() {
        return new Pose2D(xPosition, yPosition, heading);
    }

    // http://thepilons.ca/wp-content/uploads/2018/10/Tracking.pdf
    // Pose Calculation
    private void poseCalculation() {
        double dt = timer.seconds();
        timer.reset();
        double encoderPollLockID = encoderPollLock.acquireLock();
        // Change in left
        double dl = (leftEncoder.getChangeInPosition() / TICKS_PER_REVOLUTION) * (2 * Math.PI * WHEEL_RADIUS * GEAR_RATIO) * HEADING_MULTIPLIER * LEFT_ENCODER_DIRECTION.getMultiplier();
        // Change in right
        double dr = (rightEncoder.getChangeInPosition() / TICKS_PER_REVOLUTION) * (2 * Math.PI * WHEEL_RADIUS * GEAR_RATIO) * HEADING_MULTIPLIER * RIGHT_ENCODER_DIRECTION.getMultiplier();
        // Change in front
        double df = (frontEncoder.getChangeInPosition() / TICKS_PER_REVOLUTION) * (2 * Math.PI * WHEEL_RADIUS * GEAR_RATIO) * LATERAL_MULTIPLIER * FRONT_ENCODER_DIRECTION.getMultiplier();
        // Change in heading
        encoderPollLock.releaseLock(encoderPollLockID);

        heading = prevHeading + (dl - dr) / (LATERAL_DISTANCE * 2);
        double dh = heading - prevHeading;


        double xLocalOffset;
        double yLocalOffset;
        if (dh == 0) {
            xLocalOffset = df;
            yLocalOffset = dr;
        } else {
            xLocalOffset = (2.0 * Math.sin(dh / 2.0)) * ((df / dh) + FRONT_DISTANCE);
            yLocalOffset = (2.0 * Math.sin(dh / 2.0)) * ((dr / dh) + LATERAL_DISTANCE);
        }

        double averageOrientation = prevHeading - (dh / 2.0);
        double magnitude = Math.sqrt(Math.pow(xLocalOffset, 2) + Math.pow(yLocalOffset, 2));
        double angle = Math.atan(yLocalOffset / xLocalOffset) - averageOrientation;
        double xGlobalOffset = magnitude * Math.cos(angle);
        double yGlobalOffset = magnitude * Math.sin(angle);
        prevHeading = heading;
        xPosition += xGlobalOffset;
        yPosition += yGlobalOffset;
        speed = magnitude / dt;
        angularSpeed = Math.abs(dh / dt);
    }

    @Override
    public void preStart() {
        timer.reset();
        startEncoderPolling();
    }

    @Override
    public void deinit() {
        stopEncoderPolling();
    }

    @Override
    public void poll() {
        double poseCalculationLockID = poseCalculationLock.acquireLock();
        poseCalculation();
        poseCalculationLock.releaseLock(poseCalculationLockID);
    }
}
