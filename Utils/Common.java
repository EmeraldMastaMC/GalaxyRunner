package org.firstinspires.ftc.team26923.GalaxyRunner.Utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Common {
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
