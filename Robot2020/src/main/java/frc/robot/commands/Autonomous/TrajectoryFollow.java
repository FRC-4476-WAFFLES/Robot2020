/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class TrajectoryFollow extends RamseteCommand {
  //TODO: double check these are the correct numbers
  private static final double kRamseteB = 2;
  private static final double kRamseteZeta = 0.7;
  private static DifferentialDriveKinematics kinematics_thing = new DifferentialDriveKinematics(0.6);
  public static final TrajectoryConfig config =
  new TrajectoryConfig(Constants.kMaxSpeedMetersPerSecond,
                       Constants.kMaxAccelerationMetersPerSecondSquared)
      // Add kinematics to ensure max speed is actually obeyed
      //TODO: measure track widths
      .setKinematics(kinematics_thing);
  /**
   * Creates a new TrajectoryFollow.
   */
  public TrajectoryFollow(TrajectoryFollow traj) {
    // Use addRequirements() here to declare subsystem dependencies.

    super(traj, 
      RobotContainer.driveSubsystem::getPose, 
      new RamseteController(kRamseteB, kRamseteZeta), 
      kinematics_thing, RobotContainer.driveSubsystem::TankDriveVelocity, RobotContainer.driveSubsystem);
  }
  @Override
  public void end(boolean interrupted) {
    // TODO Auto-generated method stub
    super.end(interrupted);
    RobotContainer.driveSubsystem.drive(0,0);
  }
}
