/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

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
    public static final int DRIVE_LEFT_1 = 0; // SRX
    public static final int DRIVE_LEFT_2 = 1; // SPX
    public static final int DRIVE_LEFT_3 = 2; // SPX
    // public static int INTAKE_MOTOR = 3;

    public static final int DRIVE_RIGHT_1 = 7; // SRX
    public static final int DRIVE_RIGHT_2 = 8; // SPX
    public static final int DRIVE_RIGHT_3 = 9; // SPX

    // solenoids
    public static final int INTAKE_EXTEND = 0;
    public static final int INTAKE_RETRACT = 1;

    // canbus
    public static final int CLIMBER_DEPLOY = 0; // SRX
    public static final int CLIMBER_LEFT_WINCH = 0; // SRX
    public static final int CLIMBER_RIGHT_WINCH = 0; // SRX
    public static final int SHOOTER_MASTER = 1; // SRX
    public static final int SHOOTER_FOLLOWER = 0; // SPX
    public static final int SHOOTER_FEEDER_1 = 0; // SPX
    // public static final int SHOOTER_FEEDER_N = 0; // SPX
    public static final int INTAKE_ROLLER = 0; // SPX
    public static final int COLOUR_WHEEL_MANIPULATOR = 0; // SRX
    public static final int PCM = 0;//PCM

    // otherContsants
    public static final double ulrasonicValueToInches = 0.125 * 2.4;
    public static final double climberRel = 1;

    // analog input
    public static final int FRONT_ULTRASONIC = 0; // MB1013

    public static final int kTimeoutMs = 10;
    public static double kShooterUnitsPer100MsToRPM = (60.0 * 10.0) / (256.0 * 4.0);

    //PCM
    public static final int CLIMBER_LOCK = 0;
    public static final int CLIMBER_UNLOCK = 0;
    public static final int COLOUR_WHEEL_DEPLOY = 0;
    public static final int COLOUR_WHEEL_RECALL = 0;
}
