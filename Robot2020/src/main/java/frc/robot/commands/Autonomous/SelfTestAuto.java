/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Drive.TestDrive;
import frc.robot.commands.Intake.IntakeExtend;
import frc.robot.commands.Intake.IntakeRetract;
import frc.robot.commands.Intake.TestIntake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class SelfTestAuto extends SequentialCommandGroup {
  /**
   * Creates a new SelfTestAuto.
   */
  public SelfTestAuto() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new TestDrive().withTimeout(5), 
    new IntakeRetract(), new IntakeExtend(),
    new TestIntake().withTimeout(5),
    new IntakeRetract());
  }
}
