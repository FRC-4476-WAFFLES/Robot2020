/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class ShooterReady extends CommandBase {
  /**
   * Creates a new ShooterReady.
   */
  public ShooterReady() {
  }

  @Override
  public boolean isFinished() {
    return shooterSubsystem.canShoot();
  }
}
