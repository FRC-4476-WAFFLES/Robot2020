/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;
import edu.wpi.first.wpilibj.Timer;
public class ClimberClimb extends CommandBase {
  int climberState;
  double current_threshold = 0.1;
  Timer t = new Timer();
  /**
   * Creates a new ClimberClimb.
   */
  public ClimberClimb() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    climberState = climberSubsystem.climbState;
    t.reset();
    t.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(climberState == 0){
      if(climberSubsystem.getLWinchCurrent() < current_threshold){
        climberSubsystem.setLeftWinchSetpoint(climberSubsystem.getWinchLSetpoint() - 1);
      }
      if(climberSubsystem.getRWinchCurrent() < current_threshold){
        climberSubsystem.setRightWinchSetpoint(climberSubsystem.getWinchRSetpoint() - 1);
      }
    }else if(climberState == 1){
      climberSubsystem.climb();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climberSubsystem.climbState = climberState + 1;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(climberState == 0
    && climberSubsystem.getLWinchCurrent() > current_threshold 
    && climberSubsystem.getRWinchCurrent() > current_threshold){
      return true;
    }else if(climberState >0){
      return true;
    }else{
      return false;
    }
  }
}
