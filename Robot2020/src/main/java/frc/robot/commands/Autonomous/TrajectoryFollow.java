/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import static frc.robot.Constants.DriveConstants.*;
import static frc.robot.RobotContainer.*;

public class TrajectoryFollow extends RamseteCommand {
  // TODO: double check these are the correct numbers
  private static final double kRamseteB = 2;
  private static final double kRamseteZeta = 0.7;
  private static PIDController leftController = new PIDController(kP, kI, kD);
  private static PIDController rightController = new PIDController(kP, kI, kD);

  /**
   * Creates a new TrajectoryFollow.
   */
  public TrajectoryFollow(Trajectory traj) {
    // Use addRequirements() here to declare subsystem dependencies.

    super(traj,
        driveSubsystem::getPose,
        new RamseteController(kRamseteB, kRamseteZeta),
        kFeedforward,
        kDriveKinematics,
        driveSubsystem::getWheelSpeeds,
        leftController,
        rightController,
        driveSubsystem::tankDriveVoltage,
        driveSubsystem);
  }

  @Override
  public void initialize() {
    super.initialize();
    leftController.reset();
    rightController.reset();
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    driveSubsystem.drive(0, 0);
  }
}
