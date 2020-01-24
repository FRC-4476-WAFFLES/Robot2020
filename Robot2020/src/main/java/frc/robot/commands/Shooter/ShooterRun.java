/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.Preference;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;

public class ShooterRun extends CommandBase {
  ShooterSubsystem shooter;
  XboxController operate;
  Timer t;
  

  /**
   * Creates a new ShooterRun.
   */
  public ShooterRun(ShooterSubsystem shooter, XboxController opp) {
    this.shooter = shooter;
    this.operate = opp;
    addRequirements(shooter);
    t.reset();
    t.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setSpeed(Preference.getDouble("Shooter/PercentOutput", 0.1));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stop();
    t.stop();
  }
  @Override
  public boolean isFinished(){
    if(t.get()>1 && operate.getXButton()){
      return true;
    }else{
      return false;
    }
  }
}
