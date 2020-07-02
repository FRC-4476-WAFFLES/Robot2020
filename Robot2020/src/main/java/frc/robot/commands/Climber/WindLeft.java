/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class WindLeft extends CommandBase {
  /**
   * Creates a new windLeft.
   */
  public WindLeft() {
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    climberSubsystem.windL(-0.4);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climberSubsystem.windL(0);
  }
}
