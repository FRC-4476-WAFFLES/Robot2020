/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class ClimberDefault extends CommandBase {
  /**
   * Creates a new ClimberDefault.
   */
  public ClimberDefault() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climberSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double change = -10.0 * (operate.getRawAxis(1) + operate.getRawAxis(5));
    climberSubsystem.deployFudge(change);
    // climberSubsystem.MoveWinchDumb(operate.getRawAxis(1));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }
}
