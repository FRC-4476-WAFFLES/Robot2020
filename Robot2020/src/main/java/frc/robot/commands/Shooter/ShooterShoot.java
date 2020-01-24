/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.Preference;

public class ShooterShoot extends CommandBase {
  ShooterSubsystem shooterSubsystem;
  XboxController operate;
  /**
   * Creates a new ShooterShoot.
   */
  public ShooterShoot(ShooterSubsystem shooterSubsystem, XboxController operate) {
    this.shooterSubsystem = shooterSubsystem;
    this.operate = operate;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterSubsystem.setSpeed(Preference.getDouble("Shooter/PercentOutput", 0.1));
    shooterSubsystem.feed(true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.feed(false);
    new ShooterRun(shooterSubsystem, operate).schedule();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(!operate.getYButton()){
      return true;
    }else{
      return false;
    }
  }
}
