/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Camera;

import static frc.robot.RobotContainer.*;

public class CameraAim extends CommandBase {
  // TODO: make this a real threshold
  final static double closeFarsplit = 0.5;
  final static double angle = 1;

  boolean aimed = false;

  /**
   * Creates a new CameraAim.
   */
  public CameraAim() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    aimed = false;
    vision.setLEDMode(Camera.CameraLEDMode.On);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (vision.getActivePipeline()) {
    case Search:
      if (vision.getHasTarget()) {
        shooterSubsystem.savedConsistentArea = vision.getArea();
        if (vision.getArea() > closeFarsplit) {
          vision.setPipeline(Camera.Pipeline.Close);
          // shooterSubsystem.moveHood(true);
        } else {
          vision.setPipeline(Camera.Pipeline.Far);
          // shooterSubsystem.moveHood(false);
        }
        driveSubsystem.aimTowards(vision.getHorizontal());
      }else{
        driveSubsystem.aimTowards(0);
      }
      break;

    case Close:
      if (vision.getHasTarget()) {
        driveSubsystem.aimTowards(vision.getHorizontal());
      } else {
        vision.setPipeline(Camera.Pipeline.Search);
      }
      break;

    case Far:
      if (vision.getHasTarget()) {
        driveSubsystem.aimTowards(vision.getHorizontal());
      } else {
        vision.setPipeline(Camera.Pipeline.Search);
      }
      break;
    }
    aimed = vision.getHasTarget() && Math.abs(vision.getHorizontal()) < angle;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    vision.setPipeline(Camera.Pipeline.Search);
    vision.setLEDMode(Camera.CameraLEDMode.Off);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return aimed;
  }
}
