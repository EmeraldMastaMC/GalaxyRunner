# Galaxy Runner
An easy to use FTC library developed by the Keller Fusion teams for autonomous and teleop component orchestration. 
Named after Nana Asteria https://www.youtube.com/@nanaasteria 

## Features
Plug and play mecanum support
3-wheel odometry builtin (YOU MUST CONFIGURE THIS)
Basic autonomous movement
A framework for easily integrating and developing "components"
A component is a part of your robot that you program, for example, a claw. This claw can have its own
independent controls, gamepad, and it may need a thread to run in the background for it to work. GalaxyRunner
manages multithreading for you, as well as making it easy to change what the controls are for the component (if it is used for teleop).
This allows you to focus on what's important for your robot instead of having to worry about implementation details.

Common utilities are included for developing components
- PID
- Pose2D (x, y and heading)
- Nanosecond Clock

# WARNING!!!
THIS LIBRARY HAS NOT BEEN TESTED (yet) and probably doesn't work, I have not gotten the opportunity to test it
yet. I will not give instructions for how to use this library and install it, until I confirm that this project actually works.
