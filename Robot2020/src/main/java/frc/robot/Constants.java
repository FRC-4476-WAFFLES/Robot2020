/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
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
    public static final int INTAKE_EXTEND = 4;
    public static final int INTAKE_RETRACT = 3;
    public static final int COLOUR_WHEEL = 2;
    public static final int HOOD = 0;

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
    public static final int FUNNEL = 12; // SRX
    public static final int CONVEYOR = 13; // SRX
    public static final int COLOUR_WHEEL_MANIPULATOR = 14; // SRX
    // public static final int PCM = 0;// PCM

    // otherContsants

    // analog input

    // digital input
    public static final int HIGH_IR = 0;//ir
    public static final int LOW_IR = 2;//ir
    public static final int MID_IR = 1;///ir

    // path nodes
    public static final Pose2d START_CENTER = new Pose2d(new Translation2d(Units.feetToMeters(10), 0),
            new Rotation2d(0));
    public static final Pose2d BEFORE_TRENCH = new Pose2d(new Translation2d(Units.feetToMeters(13), Units.feetToMeters(1.86)),
            new Rotation2d(57.3));
    public static final Pose2d START_TRENCH = new Pose2d(new Translation2d(Units.feetToMeters(16.95), Units.feetToMeters(5.2)),
            new Rotation2d(0));
    public static final Pose2d END_TRENCH = new Pose2d(new Translation2d(Units.feetToMeters(25.25), Units.feetToMeters(5.2)),
            new Rotation2d(0));
    public static final Pose2d DRIVE_FORWARD = new Pose2d(new Translation2d(Units.feetToMeters(20), 0),
            new Rotation2d(0));

    public static final class DriveConstants {
        private static final double kS = 0.168;
        private static final double kV = 2.18;
        private static final double kA = 0.293;
        private static final double kTrackWidth = 0.72;

        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidth);
        public static final SimpleMotorFeedforward kFeedforward = new SimpleMotorFeedforward(kS, kV, kA);
        public static final DifferentialDriveVoltageConstraint kVoltageConstraint = new DifferentialDriveVoltageConstraint(kFeedforward, kDriveKinematics, 12);

        // private static final double kMaxSpeed = 2.5;
        // private static final double kMaxAccel = 3.0;
        private static final double kMaxSpeed = 2.5;
        private static final double kMaxAccel = 1.5;

        public static TrajectoryConfig getConfig() {
            return getConfig(kMaxSpeed);
        }

        public static TrajectoryConfig getConfig(double maxSpeed) {
            return getConfig(maxSpeed, kMaxAccel);
        }

        public static TrajectoryConfig getConfig(double maxSpeed, double maxAccel) {
            return new TrajectoryConfig(kMaxSpeed, maxAccel)
                .setKinematics(kDriveKinematics)
                .addConstraint(kVoltageConstraint);
        }

        // public static final double kP = 11.5;
        // public static final double kP = 7.8;
        public static final double kP = 2.0;
        public static final double kI = 0.0;
        // public static final double kD = 0.013;
        public static final double kD = 0.0;
    }
}
