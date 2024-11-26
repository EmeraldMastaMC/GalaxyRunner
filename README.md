# Galaxy Runner

An easy to use FTC library for:
- Mecanum Drive Robots.
- Creating basic, course correcting autonomous.
- Easy component creation and orchestration.
- Focusing on the things that matter.

Galaxy Runner is made for use on top of [FtcRobotController](https://github.com/FIRST-Tech-Challenge/FtcRobotController.)  
Galaxy runner was created and is maintained by FTC Team 26923 (Keller Fusion Blue).  
Named after Nana Asteria https://www.youtube.com/@nanaasteria  
![KCAL Robotics](https://github.com/user-attachments/assets/12541adb-e12c-409c-84d4-b301a22364e0)
![Nana Asteria](https://github.com/user-attachments/assets/8dbcb499-1f72-4af1-9df4-d2bf8235c117)
## WARNING!!!

THIS LIBRARY HAS NOT BEEN TESTED (yet) AND PROBABLY DOESN'T WORK. I have not gotten the opportunity
to test it
yet. I will not give instructions for how to use this library and install it, until I confirm that
this project actually works.

## Features
Galaxy Runner has these features built in:
- Plug and play mecanum support.
- Easy configuration.
- 3-wheel odometry builtin (YOU MUST CONFIGURE THIS).
- Basic autonomous movement.
- Easily control and develop components.
- Thread management.
- Easily define controls for components.
- Common utilities including:
  - A PID controller class
  - Pose2D class (x, y and heading)
  - Nanosecond Clock

## Mission Statement
Galaxy Runner strives to help you focus on developing robot specific code, and make it fun.

## Not Features
Galaxy Runner does NOT have these features (yet):
- Anything related to cameras, color sensors, distance sensors, limit switches, etc.
  - This means no AprilTag support
- Helper classes for DcMotor, CRServo, Servo etc.
  - I am not 100% sure if I should make this a feature or not,   
    because I feel like these classes are already easy enough to use,   
    but if I notice in the future that I have problems with the provided classes,   
    or someone reaches out to me and lets me know that   
    they are struggling with them, I will write wrappers for these.
- Tank Drive
  - I will probably never make this a feature. Sorry tank drive teams :\(  
    (If you REALLY want tank drive, write the code yourself, and submit a pull request)
- Complex autonomous movements
  - If you want a full featured autonomous, I recommend [Road Runner](https://github.com/acmerobotics/road-runner).  
    Galaxy Runner does not try to even come close to the feature set of Road Runner.  
    However, galaxy runner does come with basic autonomous features. If you want to  
    use Road Runner along side Galaxy Runner, you should disable Galaxy Runners autonomous  
    functionality. (However, in my opinion, I feel that Galaxy Runner's autonomous is easier  
    to use and setup than Road Runner).

## Roadmap
In order of priority:
- Easy to use preset class (Make it easy to develop a sequence of motor movements with a button press)
- Limit switches
- Maybe wrapper classes for DcMotorEx, Servo, and CRServo (Read Not Features)
