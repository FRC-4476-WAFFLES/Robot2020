/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.RobotContainer.*;

public class MoveClimber extends CommandBase {
  int direction;
  /**
   * Creates a new MoveClimber.
   */
  public MoveClimber(int direction) {
    addRequirements(climberSubsystem);
    this.direction = direction;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    climberSubsystem.setDeploySetpoint(direction, true);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !climberSubsystem.getIsTravelling();
  }
}
