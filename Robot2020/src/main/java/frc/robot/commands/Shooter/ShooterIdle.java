/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import static frc.robot.RobotContainer.*;

public class ShooterIdle extends CommandBase {
  /**
   * Creates a new ShooterIdle.
   */
  public ShooterIdle() {
    addRequirements(shooterSubsystem);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    RobotContainer.shooterSubsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
