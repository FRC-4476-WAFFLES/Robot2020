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
import frc.robot.commands.Intake.IntakeRetract;
import frc.robot.commands.Intake.IntakeRun;




// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ShootDriveForward extends SequentialCommandGroup {
  /**
   * Creates a new ShootDriveForward.
   */

  static final Trajectory traj = TrajectoryGenerator.generateTrajectory(
    List.of(Constants.START_CENTER, Constants.DRIVE_FORWARD),
    DriveConstants.getConfig());

  public ShootDriveForward() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    // super(new AimAndShoot(), new TrajectoryFollow(traj));
    super(new IntakeRetract(),
      new AimAndShoot(),
      new PIDDrive(-1.25, 0, 0.1, 0.2, true).alongWith(new IntakeRun()).withTimeout(6));
    
  }
}
