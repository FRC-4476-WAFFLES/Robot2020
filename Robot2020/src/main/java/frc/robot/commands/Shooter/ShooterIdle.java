/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterIdle extends CommandBase {
  ShooterSubsystem shooter;


  /**
   * Creates a new ShooterIdle.
   */
  public ShooterIdle(ShooterSubsystem shooter) {
    this.shooter = shooter;
    addRequirements(shooter);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.stop();
  }
  @Override
  public boolean isFinished(){
    return false;
  }
}
