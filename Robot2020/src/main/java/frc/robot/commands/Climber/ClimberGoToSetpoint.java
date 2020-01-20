/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberSubsystem;

public class ClimberGoToSetpoint extends CommandBase {
  private final ClimberSubsystem climberSubsystem;
  private final int povAngle; 
  /**
   * Creates a new ClimberGoToSetpoint.
   */
  public ClimberGoToSetpoint(ClimberSubsystem climberSubsystem, int angle) {
    this.climberSubsystem = climberSubsystem; 
    povAngle = angle;
    addRequirements(climberSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    climberSubsystem.isGoingToSetPoint = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    float climbEncoder = climberSubsystem.getDeployPosition();
    if(climbEncoder <= ClimberSubsystem.DEPLOY_CENTER_LOW){
      if(povAngle == 0){
        climberSubsystem.setDeployWinchSetpoint(ClimberSubsystem.DEPLOY_CENTER_LOW);
      }else if(povAngle == 180){
        climberSubsystem.setDeployWinchSetpoint(ClimberSubsystem.DEFAULT_DEPLOY_POSTION);
      }
      
    }else if(climbEncoder <= ClimberSubsystem.DEPLOY_CENTER){
      if(povAngle == 0){
        climberSubsystem.setDeployWinchSetpoint(ClimberSubsystem.DEPLOY_CENTER_HIGH);
      }else if(povAngle == 180){
        climberSubsystem.setDeployWinchSetpoint(ClimberSubsystem.DEPLOY_CENTER_LOW);
      }

    }else if(climbEncoder <= ClimberSubsystem.DEPLOY_CENTER_HIGH){
      if(povAngle == 0){
        climberSubsystem.setDeployWinchSetpoint(ClimberSubsystem.DEPLOY_CENTER_HIGH);
      }else if(povAngle == 180){
        climberSubsystem.setDeployWinchSetpoint(ClimberSubsystem.DEPLOY_CENTER);
      }

    }else{
      climberSubsystem.setDeployWinchSetpoint(ClimberSubsystem.DEPLOY_CENTER_HIGH);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
