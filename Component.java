package org.firstinspires.ftc.teamcode.GalaxyRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// WARNING!!! User must make sure that the poller is thread safe, safety is not guaranteed.
public abstract class Component implements Runnable,Pollable {
    private static final Logger log = LoggerFactory.getLogger(Component.class);
    private boolean stopExecution = false;
    private Thread poller;
    private final Spinlock lock = new Spinlock();
    private boolean requiresThreadToInit = false;
    private boolean runningInitializationThread = false;

    @Override
    public abstract void poll();
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
        poller = new Thread(this);
        poller.start();
    }
    private void initializationStart() {
        double lockID = lock.acquireLock();
        runningInitializationThread = true;
        lock.releaseLock(lockID);
        poller = new Thread(this);
        poller.start();
        init();
    }
    private void join() {
        try {
            poller.join();
        } catch(Exception e) {
            log.error("Error: ", e);
        }
    }
    public void doesRequireThreadToInit() {
        requiresThreadToInit = true;
    }
    @Override
    public void stopPolling() {
        requestStop();
        join();
    }
    private boolean stopRequested() {
        return stopExecution;
    }
    private void requestStop() {
        stopExecution = true;
    }
    private void reset() {
        stopExecution = false;
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
    public void init() {
    }
    @Override
    public void deinit() {
    }
    @Override
    public void run() {
        while(!stopRequested()) {
            poll();
        }
        reset();
        if (!runningInitializationThread) {
            deinit();
        }
    }
    @Override
    public void preStart() {
    }
    @Override
    public void start() {
        preStart();
        startPolling();
    }

    @Override
    public void preStop() {
    }

    @Override
    public void stop() {
        preStop();
        stopPolling();
    }
}
