package org.firstinspires.ftc.teamcode.GalaxyRunner;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TeleOpComponent extends Component {
    private static final Logger log = LoggerFactory.getLogger(TeleOpComponent.class);
    private Gamepad gamepad;
    private boolean stopExecution = false;
    private boolean controlsEnabled = false;
    private boolean automaticallyEnableControls = true;
    private Thread poller;
    private final Spinlock lock = new Spinlock();
    private boolean requiresThreadToInit = false;
    private boolean runningInitializationThread = false;

    public abstract void poll();
    public abstract void controls();
    public abstract void nullifyControls();

    private void requestStop() {
        stopExecution = true;
    }
    private boolean stopRequested() {
        return stopExecution;
    }

    private void reset() {
        stopExecution = false;
        controlsEnabled = false;
    }
    @Override
    public void doesRequireThreadToInit() {
        requiresThreadToInit = true;
    }
    public Gamepad getGamepad() {
        return gamepad;
    }

    public void setGamepad(Gamepad gamepad) {
        this.gamepad = gamepad;
    }
    public void disableAutoEnableControlsOnStart() {
        automaticallyEnableControls = false;
    }

    private void enableControls() {
        controlsEnabled = true;
    }
    private void disableControls() {
        controlsEnabled = false;
    }
    private void join() {
        try {
            poller.join();
        } catch(Exception e) {
            log.error("e: ", e);
        }
    }
    @Override
    public void startPolling() {
        double lockID = lock.acquireLock();
        boolean isRunningInitializationThread = runningInitializationThread;
        lock.releaseLock(lockID);

        if(isRunningInitializationThread) {
            stopPolling();

            lockID = lock.acquireLock();
            runningInitializationThread = false;
            lock.releaseLock(lockID);
        }
        lockID = lock.acquireLock();
        boolean automaticallyEnableControls = this.automaticallyEnableControls;
        lock.releaseLock(lockID);

        if (automaticallyEnableControls) {
            enableControls();
        }
        poller = new Thread(this);
        poller.start();
    }
    private void initializationStart() {
        double lockID = lock.acquireLock();
        runningInitializationThread = true;
        lock.releaseLock(lockID);

        disableControls();
        poller = new Thread(this);
        poller.start();
        init();
    }
    @Override
    public void stopPolling() {
        requestStop();
        join();
    }
    @Override
    public void initialize() {
        if (requiresThreadToInit) {
            initializationStart();
        } else {
            init();
        }
    }

    @Override
    public void run() {
        while(!stopRequested()) {
            if (controlsEnabled) {
                controls();
            } else {
                nullifyControls();
            }
            poll();
        }
        reset();
        if (!runningInitializationThread) {
            deinit();
        }
    }
    public boolean teleOpEnabled() {
        return controlsEnabled;
    }

}
