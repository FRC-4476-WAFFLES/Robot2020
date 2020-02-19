/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drive;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class OperatorTankDrive extends CommandBase {
  /**
   * Creates a new OperatorTankDrive.
   */
  public OperatorTankDrive() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.drive(leftJoystick.getY(), rightJoystick.getY());
    // if (leftJoystick.getRawButton(3) && rightJoystick.getRawButton(3)
    //     && (operate.getStickButtonPressed(Hand.kLeft) || operate.getStickButtonPressed(Hand.kRight))) {
    //   driveSubsystem.hyperspeed_happyface_ = !driveSubsystem.hyperspeed_happyface_;
    // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.drive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
