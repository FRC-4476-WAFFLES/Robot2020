/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShooterEmpty extends CommandBase {
  /**
   * Creates a new ShooterEmpty.
   */
  public ShooterEmpty() {
    // Use addRequirements() here to declare subsystem dependencies.
  }
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
    //TODO: finish when all balls are shot
  }
}
