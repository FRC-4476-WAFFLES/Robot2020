/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class ClimberWinchCommand extends CommandBase {
  // TODO: get the encoder difference that is on the edge of legal.
  private static final int maxEncDiff_angle = 0;
  private double currentLeftPos;
  private double currentRightPos;

  /**
   * Creates a new ClimberClimberWinchCommand.
   */
  public ClimberWinchCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climberSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentLeftPos = climberSubsystem.getLeftWinchPosition();
    currentRightPos = climberSubsystem.getRigthWinchPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double adjust = (operate.getRawAxis(0) + operate.getRawAxis(4)) * 10;
    // TODO: make sure this threshold keeps us legal, instead of preventing us from
    // moving
    if (adjust >= 0 && currentLeftPos - currentRightPos < maxEncDiff_angle) {
      currentLeftPos = currentLeftPos + adjust;
    } else if (adjust < 0 && currentRightPos - currentLeftPos < maxEncDiff_angle) {
      currentRightPos = currentRightPos + adjust;
    }

    climberSubsystem.setLeftWinchSetpoint(currentLeftPos);
    climberSubsystem.setRightWinchSetpoint(currentRightPos);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
