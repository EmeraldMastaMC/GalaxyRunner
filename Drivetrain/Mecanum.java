package org.firstinspires.ftc.team26923.GalaxyRunner.Drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.team26923.GalaxyRunner.TeleOpComponent;
import org.firstinspires.ftc.team26923.GalaxyRunner.Utils.Direction;
import org.firstinspires.ftc.team26923.GalaxyRunner.Utils.Spinlock;

public class Mecanum extends TeleOpComponent {
    private static final double POWER_MULTIPLIER = 1.0;
    private static final Direction AXIAL_DIRECTION = Direction.FORWARD;
    private static final Direction LATERAL_DIRECTION = Direction.RIGHT;
    private static final Direction YAW_DIRECTION = Direction.COUNTERCLOCKWISE;
    private static final String LEFT_FRONT_DRIVE_NAME = "left_front_drive";
    private static final String LEFT_REAR_DRIVE_NAME = "left_back_drive";
    private static final String RIGHT_FRONT_DRIVE_NAME = "right_front_drive";
    private static final String RIGHT_REAR_DRIVE_NAME = "right_back_drive";
    private double axialControl;
    private double lateralControl;
    private double yawControl;

    public void controls() {
        axialControl = -getGamepad().left_stick_y;
        lateralControl = -getGamepad().left_stick_x;
        yawControl = getGamepad().right_stick_x;
    }

    public void nullifyControls() {
        axialControl = 0.0;
        lateralControl = 0.0;
        yawControl = 0.0;
    }

    public Mecanum(HardwareMap hardwareMap) {
        // Change these values to suit your code
        leftFront = hardwareMap.get(DcMotorEx.class, LEFT_FRONT_DRIVE_NAME);

        leftRear = hardwareMap.get(DcMotorEx.class, LEFT_REAR_DRIVE_NAME);

        rightFront = hardwareMap.get(DcMotorEx.class, RIGHT_FRONT_DRIVE_NAME);

        rightRear = hardwareMap.get(DcMotorEx.class, RIGHT_REAR_DRIVE_NAME);
        disableAutoEnableControlsOnStart();
    }

    public Mecanum(HardwareMap hardwareMap, Gamepad gamepad) {
        // Change these values to suit your code
        leftFront = hardwareMap.get(DcMotorEx.class, LEFT_FRONT_DRIVE_NAME);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftRear = hardwareMap.get(DcMotorEx.class, LEFT_REAR_DRIVE_NAME);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightFront = hardwareMap.get(DcMotorEx.class, RIGHT_FRONT_DRIVE_NAME);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        rightRear = hardwareMap.get(DcMotorEx.class, RIGHT_REAR_DRIVE_NAME);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        setGamepad(gamepad);
    }

    private final DcMotorEx leftFront;
    private final DcMotorEx leftRear;
    private final DcMotorEx rightFront;
    private final DcMotorEx rightRear;

    private final Spinlock lock = new Spinlock();
    private double axialTarget = 0;
    private double lateralTarget = 0;
    private double yawTarget = 0;


    private void power(double lfp, double lrp, double rfp, double rrp) {
        leftFront.setPower(lfp);
        leftRear.setPower(lrp);
        rightFront.setPower(rfp);
        rightRear.setPower(rrp);
    }

    private void power(double axial, double lateral, double yaw) {
        double max;
        double adjustedAxial = axial * AXIAL_DIRECTION.getMultiplier();
        double adjustedLateral = lateral * LATERAL_DIRECTION.getMultiplier();
        double adjustedYaw = yaw * YAW_DIRECTION.getMultiplier();

        double leftFrontPower = (adjustedAxial + adjustedLateral + adjustedYaw) * POWER_MULTIPLIER;
        double rightFrontPower = (adjustedAxial - adjustedLateral - adjustedYaw) * POWER_MULTIPLIER;
        double leftRearPower = (adjustedAxial - adjustedLateral + adjustedYaw) * POWER_MULTIPLIER;
        double rightRearPower = (adjustedAxial + adjustedLateral - adjustedYaw) * POWER_MULTIPLIER;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftRearPower));
        max = Math.max(max, Math.abs(rightRearPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftRearPower /= max;
            rightRearPower /= max;
        }

        power(leftFrontPower, leftRearPower, rightFrontPower, rightRearPower);
    }

    public void setTarget(double axial, double lateral, double yaw) {

        double lockID = lock.acquireLock();
        axialTarget = axial;
        lateralTarget = lateral;
        yawTarget = yaw;
        lock.releaseLock(lockID);
    }


    @Override
    public void poll() {
        if (teleOpEnabled()) {
            double lockID = lock.acquireLock();
            power(axialControl, lateralControl, yawControl);
            lock.releaseLock(lockID);
        } else {
            double lockID = lock.acquireLock();
            power(axialTarget, lateralTarget, yawTarget);
            lock.releaseLock(lockID);
        }
    }
}
