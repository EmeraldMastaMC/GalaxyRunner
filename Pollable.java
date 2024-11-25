package org.firstinspires.ftc.teamcode.GalaxyRunner;

public interface Pollable {
    void startPolling();

    void stopPolling();

    void poll();

    void init();

    void deinit();

    void preStart();

    void start();

    void preStop();

    void stop();

    void initialize();
}