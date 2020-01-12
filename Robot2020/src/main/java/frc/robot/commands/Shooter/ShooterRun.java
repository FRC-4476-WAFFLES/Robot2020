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

public class ShooterRun extends CommandBase {
  ShooterSubsystem shooter;
  XboxController operate;

  /**
   * Creates a new ShooterRun.
   */
  public ShooterRun(ShooterSubsystem shooter, XboxController opp) {
    this.shooter = shooter;
    this.operate = opp;
    addRequirements(shooter);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setSpeed(Preference.getDouble("Shooter/PercentOutput", 0.1));
    // double in = operate.getRawAxis(3);
    // double out = operate.getRawAxis(2);
    // shooter.setSpeed((double) ((in * in - 1 * out * out) * 0.9));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.setSpeed(0);
  }
}
