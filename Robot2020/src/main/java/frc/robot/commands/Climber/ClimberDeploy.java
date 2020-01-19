/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.Climber.ClimberRetract;

public class ClimberDeploy extends CommandBase {
  private static ClimberSubsystem climberSubsystem;
  private static XboxController operate;
  private Timer heldTime = new Timer();
  private float curentPos = ClimberSubsystem.DEFAULT_DEPLOY_POSTION;
  /**
   * Creates a new ClimberDeploy.
   */
  public ClimberDeploy(ClimberSubsystem climberSubsystem, XboxController operate) {
    this.climberSubsystem = climberSubsystem;
    this.operate = operate;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    climberSubsystem.setDeployWinchSetpoint(ClimberSubsystem.DEFAULT_DEPLOY_POSTION);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    int fudgeButton = operate.getPOV();
    if(heldTime.get()>1){
      curentPos = curentPos -1;
    }else if(heldTime.get()<1 && heldTime.get()>0 && fudgeButton != 180){
      new ClimberRetract(climberSubsystem).schedule();
      // this.cancel();
    }else if(fudgeButton != 180){
      heldTime.stop();
      heldTime.reset();
    }
    
    if(fudgeButton == 0) {
      curentPos = curentPos +1;
    }else if(fudgeButton == 180 && heldTime.get() == 0) {
      heldTime.start();
    }
    climberSubsystem.setDeployWinchSetpoint(curentPos);
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
