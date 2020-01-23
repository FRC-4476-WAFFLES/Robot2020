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
import java.lang.Math;
import frc.robot.commands.Climber.ClimberUndeploy;

public class ClimberDefault extends CommandBase {
  private final ClimberSubsystem climberSubsystem;
  private final XboxController operate;
  private int currentPos;
  private final float compressionThreshold = 0;
  /**
   * Creates a new ClimberDefault.
   */
  public ClimberDefault(ClimberSubsystem climberSubsystem, XboxController operate) {
    this.climberSubsystem = climberSubsystem;
    this.operate = operate;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentPos = (int)climberSubsystem.getDeployPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentPos = -10*((int)operate.getRawAxis(1)+(int)operate.getRawAxis(5));
    climberSubsystem.setDeployWinchSetpoint(currentPos);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(climberSubsystem.isGoingToSetPoint && Math.abs(climberSubsystem.getDeployError()) <= compressionThreshold){
      climberSubsystem.isGoingToSetPoint = false;
      return false;
    }else if(!climberSubsystem.isGoingToSetPoint && Math.abs(climberSubsystem.getDeployError()) >= compressionThreshold){
      new ClimberUndeploy(climberSubsystem, operate).schedule(false);
      return true;
    }else{
      return false;
    }
  }
}
