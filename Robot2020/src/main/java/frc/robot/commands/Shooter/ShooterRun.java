/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.Preference;
import edu.wpi.first.wpilibj.Timer;
import static frc.robot.RobotContainer.*;

public class ShooterRun extends CommandBase {
  Timer t = new Timer();

  /**
   * Creates a new ShooterRun.
   */
  public ShooterRun() {
    addRequirements(shooterSubsystem);
  }

  @Override
  public void initialize() {
    t.reset();
    t.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterSubsystem.setSpeed(Preference.getDouble("Shooter/PercentOutput", 0.1));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.stop();
    t.stop();
  }

  @Override
  public boolean isFinished() {
    if (t.get() > 1 && operate.getXButton()) {
      return true;
    } else {
      return false;
    }
  }
}
