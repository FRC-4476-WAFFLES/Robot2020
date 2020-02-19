/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    // pwm

    // solenoids
    public static final int INTAKE_EXTEND = 0;
    public static final int INTAKE_RETRACT = 1;
    public static final int CLIMBER_LOCK = 2;
    public static final int CLIMBER_UNLOCK = 3;
    public static final int COLOUR_WHEEL_DEPLOY = 4;
    public static final int COLOUR_WHEEL_RECALL = 5;
    public static final int HOOD_EXTEND = 6;
    public static final int HOOD_RETRACT = 7;

    // canbus
    public static final int DRIVE_LEFT_1 = 1; // FX
    public static final int DRIVE_LEFT_2 = 2; // FX
    public static final int DRIVE_RIGHT_1 = 3; // FX
    public static final int DRIVE_RIGHT_2 = 4; // FX
    public static final int CLIMBER_DEPLOY = 5; // SRX
    public static final int CLIMBER_LEFT_WINCH = 6; // SPARKMAX
    public static final int CLIMBER_RIGHT_WINCH = 7; // SPARKMAX
    public static final int SHOOTER_MASTER = 8; // SPARKMAX
    public static final int SHOOTER_FOLLOWER = 9; // SPARKMAX
    public static final int SHOOTER_PREP = 10; // SRX
    public static final int INTAKE_ROLLER = 11; // SRX
    public static final int FUNNEL = 12; // SPX
    public static final int CONVEYOR = 13; // SPX
    public static final int COLOUR_WHEEL_MANIPULATOR = 14; // SRX
    public static final int PCM = 0;// PCM

    // otherContsants
    public static final double ulrasonicValueToInches = 0.125 * 2.4;
    public static final double climberRel = 1;
    public static final double DRIVE_ENCODERS_TO_METERS = 0;
    public static final double kMaxSpeedMetersPerSecond = 1;
    public static final double kMaxAccelerationMetersPerSecondSquared = 1;
    public static final double kDriveKinematics = 0;
    public static final int kTimeoutMs = 10;
    public static double kShooterUnitsPer100MsToRPM = (60.0 * 10.0) / (256.0 * 4.0);

    // analog input
    public static final int FRONT_ULTRASONIC = 0; // MB1013

    // path nodes
    public static final Pose2d START_CENTER = new Pose2d(new Translation2d(Units.feetToMeters(10), 0),
            new Rotation2d(0));
    public static final Pose2d DRIVE_FORWARD = new Pose2d(new Translation2d(Units.feetToMeters(20), 0),
            new Rotation2d(0));
}
