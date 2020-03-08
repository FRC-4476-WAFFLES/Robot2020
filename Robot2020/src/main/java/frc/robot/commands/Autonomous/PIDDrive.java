/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class PIDDrive extends CommandBase {
  double startAngle;
  double startPos;
  double distance;
  double angle;
  double epsilon;
  double speed_max;
  boolean timed;
  /**
   * Creates a new PIDDrive.
   */
  public PIDDrive(double distance, double angle, double epsilon, double speed_max, boolean timed) {
    // Distances should be in meters.
    // Distance and angle are relative to current position 
    this.distance = driveSubsystem.mToNative(distance);
    this.angle = angle;
    this.epsilon = driveSubsystem.mToNative(epsilon);
    this.speed_max = speed_max;
    this.timed = timed;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startPos = (driveSubsystem.getLeftPos() + driveSubsystem.getRightPos())/2;
    startAngle = driveSubsystem.getHeading();
    driveSubsystem.auto_line.setSetpoint(startPos + distance);
    driveSubsystem.auto_turn.setSetpoint(startAngle + angle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.auto_line.setTolerance(epsilon);
    driveSubsystem.auto_turn.setTolerance(5);
    double out = driveSubsystem.auto_line.calculate((driveSubsystem.getLeftPos() + driveSubsystem.getRightPos())/2);
    // double out = 0;
    double turn = driveSubsystem.auto_turn.calculate(driveSubsystem.getHeading());
    out = driveSubsystem.clamp(out, -speed_max, speed_max);
    turn = driveSubsystem.clamp(turn, -speed_max, speed_max);
    System.out.println(out);
    driveSubsystem.tankDrivePercent(out - turn, out + turn);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.tankDrivePercent(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return driveSubsystem.auto_line.atSetpoint() && driveSubsystem.auto_turn.atSetpoint();
  }
}
