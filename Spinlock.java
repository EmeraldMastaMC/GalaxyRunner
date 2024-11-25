package org.firstinspires.ftc.teamcode.GalaxyRunner;

import static org.firstinspires.ftc.teamcode.GalaxyRunner.Utils.sleep;

import java.util.concurrent.atomic.AtomicBoolean;

public class Spinlock {
    private final AtomicBoolean lock = new AtomicBoolean(false);
    private double owner;

    public double acquireLock() {
        while (!lock.compareAndSet(false, true)) {
            sleep(10);
        }
        owner = Math.random();
        return owner;
    }

    public void releaseLock(double owner) {
        if (this.owner == owner) {
            lock.set(false);
        }
    }
}
