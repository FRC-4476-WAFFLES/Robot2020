/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Drive.CameraAim;
import frc.robot.commands.Intake.IntakeExtend;
import frc.robot.commands.Shooter.ShooterEmpty;
import frc.robot.commands.Shooter.ShooterReady;
import frc.robot.commands.Shooter.ShooterRun;
import frc.robot.commands.Shooter.ShooterShoot;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AimAndShoot extends SequentialCommandGroup {
  /**
   * Creates a new AimAndShoot.
   */
  public AimAndShoot() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
      new IntakeExtend()
        .alongWith(new CameraAim())
        .deadlineWith(new ShooterRun())
        .withTimeout(1),
      new ShooterReady()
        .deadlineWith(new ShooterRun())
        .withTimeout(2),
      new ShooterEmpty()
        .deadlineWith(new ShooterShoot(), new ShooterRun())
        .withTimeout(2)
    );
  }
}
