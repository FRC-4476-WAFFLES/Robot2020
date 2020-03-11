/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import java.util.List;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.commands.Drive.ResetPose;
import frc.robot.commands.Drive.TrajectoryFollow;
import frc.robot.commands.Intake.IntakeRetract;
import frc.robot.commands.Intake.IntakeRun;
import frc.robot.commands.Shooter.ShooterRun;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TrenchPickup extends SequentialCommandGroup {
  static final Trajectory firstTrajectory = TrajectoryGenerator.generateTrajectory(
        List.of(Constants.START_CENTER, Constants.BEFORE_TRENCH, Constants.START_TRENCH, Constants.END_TRENCH),
        DriveConstants.getConfig());
  static final Trajectory secondTrajectory = TrajectoryGenerator.generateTrajectory(
        List.of(Constants.END_TRENCH, Constants.START_CENTER),
        DriveConstants.getConfig().setReversed(true));

  /**
   * Creates a new TrenchPickup.
   */
  public TrenchPickup() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
      new ResetPose(firstTrajectory.getInitialPose()),
      new IntakeRetract(),
      new AimAndShoot(),
      new TrajectoryFollow(firstTrajectory)
        .deadlineWith(new IntakeRun()),
      new TrajectoryFollow(secondTrajectory)
        .deadlineWith(new ShooterRun()),
      new AimAndShoot()
      );
  }
}
