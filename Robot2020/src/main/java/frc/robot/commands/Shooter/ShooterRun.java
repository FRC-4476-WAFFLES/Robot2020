/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.PreferenceManager;
import static frc.robot.RobotContainer.*;

public class ShooterRun extends CommandBase {
  /**
   * Creates a new ShooterRun.
   */
  public ShooterRun() {
    addRequirements(shooterSubsystem);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // shooterSubsystem.setSpeed(PreferenceManager.getDouble("Shooter/RPM", 6000));

    // if(PreferenceManager.getDouble("Shooter/RPM", 6000) > 5000){
    //   shooterSubsystem.moveHood(true);
    // }else{
    //   shooterSubsystem.moveHood(false);
    // }

    double area = shooterSubsystem.savedConsistentArea;
    if(area == 0){
      shooterSubsystem.setSpeed(PreferenceManager.getDouble("Shooter/Close Init RPM", 4500));
      shooterSubsystem.moveHood(false);
      System.out.println("very close");
    }else if(area > 2.5){
      shooterSubsystem.setSpeed(PreferenceManager.getDouble("Shooter/Close Init RPM", 4500));
      shooterSubsystem.moveHood(false);
      System.out.println("very close");
    }else if(area > 1){
      shooterSubsystem.setSpeed(PreferenceManager.getDouble("Shooter/Far Init RPM", 4500));
      shooterSubsystem.moveHood(false);
      System.out.println("initiation line");
    }else if(area > 0.5){
      shooterSubsystem.setSpeed(PreferenceManager.getDouble("Shooter/Close Trench RPM", 5250));
      shooterSubsystem.moveHood(true);
      System.out.println("close trench");
    }else{
      shooterSubsystem.setSpeed(PreferenceManager.getDouble("Shooter/Far Trench RPM", 6000));
      shooterSubsystem.moveHood(true);
      System.out.println("far trench");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.stop();
  }
}
