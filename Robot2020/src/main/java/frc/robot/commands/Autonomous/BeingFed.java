/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Intake.IntakeExtend;
import frc.robot.commands.Intake.IntakeRetract;
import frc.robot.commands.Shooter.ShooterRun;
import frc.robot.commands.Shooter.ShooterShoot;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class BeingFed extends SequentialCommandGroup {
  /**
   * Creates a new BeingFed.
   */
  public BeingFed() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
    new IntakeRetract(),
    new WaitCommand(0.5),
    new PIDDrive(0.2, 0, 0.05, 0.2, false)
      .withTimeout(3),
    new PIDDrive(-0.55, 0, 0.05, 0.2, false)
      .withTimeout(3),
    new IntakeExtend()
      .deadlineWith(new ShooterRun()),
    new ShooterRun().withTimeout(2),
    new ShooterShoot()
      .alongWith(new ShooterRun())
    );
  }
}
