/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class ShooterShoot extends CommandBase {
  Timer t = new Timer();

  /**
   * Creates a new ShooterShoot.
   */
  public ShooterShoot() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeSubsystem);
  }

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    t.reset();
    t.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // shooterSubsystem.feed(shooterSubsystem.canShoot());
    // shooterSubsystem.feed(true);

    // if(shooterSubsystem.canShoot()) {
    if (t.get() > 0.2) {
      intakeSubsystem.run();
    } else {
      intakeSubsystem.unrun(-1);
      if (t.get() < 0.1) {
        shooterSubsystem.unfeed();
      } else {
        shooterSubsystem.feed(true);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.feed(false);
    intakeSubsystem.stop();
  }
}
