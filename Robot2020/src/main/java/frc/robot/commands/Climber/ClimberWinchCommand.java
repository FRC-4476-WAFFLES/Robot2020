/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberSubsystem;
import edu.wpi.first.wpilibj.XboxController;

public class ClimberWinchCommand extends CommandBase {
  private final ClimberSubsystem climberSubsystem;
  private final XboxController operate;
  // TODO: get the encoder difference that is on the edge of legal.
  private static final int maxEncDiff_angle = 0;
  private int currentLeftPos;
  private int currentRightPos;
  private boolean waitingBtnRelease = false;

  /**
   * Creates a new ClimberClimberWinchCommand.
   */
  public ClimberWinchCommand(ClimberSubsystem climberSubsystem, XboxController operate) {
    this.climberSubsystem = climberSubsystem;
    this.operate = operate;
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
    if (!climberSubsystem.getIsClimbLocked()) {
      double adjust = (operate.getRawAxis(0) + operate.getRawAxis(4)) * 10;
      // TODO: make sure this threshold keeps us legal, instead of preventing us from
      // moving
      if (adjust >= 0 && currentLeftPos - currentRightPos < maxEncDiff_angle) {
        currentLeftPos = currentLeftPos + (int) adjust;
      } else if (adjust < 0 && currentRightPos - currentLeftPos < maxEncDiff_angle) {
        currentRightPos = currentRightPos + (int) adjust;
      }

      climberSubsystem.setLeftWinchSetpoint(currentLeftPos);
      climberSubsystem.setRightWinchSetpoint(currentRightPos);
      if (operate.getAButtonPressed()) {
        climberSubsystem.ToggleWinchLock();
        waitingBtnRelease = true;
      } else {
        if (!operate.getAButton()) {
          waitingBtnRelease = false;
        }
        if (operate.getAButton() && !waitingBtnRelease) {
          climberSubsystem.ToggleWinchLock();
        }
      }
    }
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
