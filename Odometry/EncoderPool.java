package org.firstinspires.ftc.team26923.GalaxyRunner.Odometry;

import org.firstinspires.ftc.team26923.GalaxyRunner.Component;
import org.firstinspires.ftc.team26923.GalaxyRunner.Utils.Spinlock;

import java.util.ArrayList;
import java.util.Arrays;

public class EncoderPool extends Component {
    private final ArrayList<Encoder> encoders = new ArrayList<>();
    private Spinlock lock;

    public EncoderPool(Encoder e1, Encoder e2) {
        encoders.add(e1);
        encoders.add(e2);
    }

    public EncoderPool(Encoder e1, Encoder e2, Encoder e3) {
        encoders.add(e1);
        encoders.add(e2);
        encoders.add(e3);
    }

    public EncoderPool(Encoder[] encoders) {
        this.encoders.addAll(Arrays.asList(encoders));
    }

    public EncoderPool(ArrayList<Encoder> encoders) {
        this.encoders.addAll(encoders);
    }

    // Use a spinlock if you need to synchronize access to the encoders
    public EncoderPool(Encoder e1, Encoder e2, Spinlock lock) {
        encoders.add(e1);
        encoders.add(e2);
        this.lock = lock;
    }

    public EncoderPool(Encoder e1, Encoder e2, Encoder e3, Spinlock lock) {
        encoders.add(e1);
        encoders.add(e2);
        encoders.add(e3);
        this.lock = lock;
    }

    public EncoderPool(Encoder[] encoders, Spinlock lock) {
        this.encoders.addAll(Arrays.asList(encoders));
        this.lock = lock;
    }

    public EncoderPool(ArrayList<Encoder> encoders, Spinlock lock) {
        this.encoders.addAll(encoders);
        this.lock = lock;
    }

    public ArrayList<Double> getEncoderPositions() {
        double lockID = lock.acquireLock();
        ArrayList<Double> positions = new ArrayList<>();
        for (Encoder encoder : encoders) {
            positions.add((double) encoder.getCurrentPosition());
        }
        lock.releaseLock(lockID);
        return positions;
    }

    public ArrayList<Double> getEncoderVelocities() {
        double lockID = lock.acquireLock();
        ArrayList<Double> speeds = new ArrayList<>();
        for (Encoder encoder : encoders) {
            speeds.add(encoder.getCorrectedVelocity());
        }
        lock.releaseLock(lockID);
        return speeds;
    }

    public void preStart() {
        for (Encoder encoder : encoders) {
            encoder.startTimer();
        }
    }

    @Override
    public void poll() {
        double lockID = lock.acquireLock();
        for (Encoder encoder : encoders) {
            encoder.poll();
        }
        lock.releaseLock(lockID);
    }
}
