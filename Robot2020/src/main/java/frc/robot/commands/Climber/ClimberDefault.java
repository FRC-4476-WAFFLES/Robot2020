/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import java.lang.Math;
import frc.robot.commands.Climber.ClimberUndeploy;
import static frc.robot.RobotContainer.*;

public class ClimberDefault extends CommandBase {
  private double currentPos;
  //TODO: make this threshold a real number
  private final float compressionThreshold = 0;

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
    currentPos = (double)climberSubsystem.getDeployPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentPos = -10.0*(operate.getRawAxis(1)+operate.getRawAxis(5));
    climberSubsystem.setDeployWinchSetpoint(currentPos);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (climberSubsystem.isGoingToSetPoint && Math.abs(climberSubsystem.getDeployError()) <= compressionThreshold) {
      climberSubsystem.isGoingToSetPoint = false;
      return false;
    } else if (!climberSubsystem.isGoingToSetPoint
        && Math.abs(climberSubsystem.getDeployError()) >= compressionThreshold) {
      new ClimberUndeploy(climberSubsystem, operate).schedule(false);
      return true;
    } else {
      return false;
    }
  }
}
