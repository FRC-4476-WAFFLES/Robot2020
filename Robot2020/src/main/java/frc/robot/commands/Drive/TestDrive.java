/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TestDrive extends CommandBase {
  double percent = 0.5;
  double initGyroPos;
  double gyroDrift;
  double totalLeftVelocity = 0;
  double totalLeftCurrent = 0;
  double totalRightVelocity = 0;
  double totalRightCurrent = 0;
  int loops = 0;
  public TestDrive() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveSubsystem.drive(percent, percent);
    initGyroPos = driveSubsystem.getHeading();
  }

  @Override
  public void execute() {
    totalLeftCurrent += driveSubsystem.getLeftCurrent();
    totalLeftVelocity += driveSubsystem.getLeftVelocity();
    totalRightCurrent += driveSubsystem.getRightCurrent();
    totalRightVelocity += driveSubsystem.getRightVelocity();
    loops++;
    
  }

  @Override
  public void end(boolean interrupted) {
    driveSubsystem.drive(0, 0);
    gyroDrift = Math.abs(driveSubsystem.getHeading() - initGyroPos);
  }
}
