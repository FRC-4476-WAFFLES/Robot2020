/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Drive.CameraAim;
import frc.robot.commands.Intake.IntakeExtend;
import frc.robot.commands.Intake.IntakeRetract;
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
      new IntakeRetract(),
      new IntakeExtend()
        .alongWith(new CameraAim())
        .withTimeout(2)
        .deadlineWith(new ShooterRun()),
      new WaitCommand(2)
        .deadlineWith(new ShooterRun()),
      new ShooterShoot()
        .alongWith(new ShooterRun())
        .withTimeout(4)
    );
  }
}
