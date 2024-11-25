package org.firstinspires.ftc.teamcode.GalaxyRunner;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Utils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {
        }
    }

    public static void waitUntilStopRequested(LinearOpMode opMode) {
        while (opMode.opModeIsActive()) {
            opMode.sleep(10);
        }
    }
}
