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
import java.lang.Math;

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
    driveSubsystem.drive(leftJoystick.getY()*0.7, rightJoystick.getY()*0.7);
    if (leftJoystick.getRawButton(3) && rightJoystick.getRawButton(3)
        && (operate.getStickButtonPressed(Hand.kLeft) || operate.getStickButtonPressed(Hand.kRight))) {
      driveSubsystem.hyperspeed_happyface_ = !driveSubsystem.hyperspeed_happyface_;
    }

    double lspd = leftJoystick.getY();
    double rspd = rightJoystick.getY();

    double stick_diff = Math.abs(lspd - rspd);
    double stick_avg = (leftJoystick.getY() + rightJoystick.getY())/2;

    double adjust = stick_diff/Math.abs(stick_avg)/1.7;
    if(adjust < 1){
      lspd = leftJoystick.getY()*adjust + stick_avg*(1-adjust);
      rspd = rightJoystick.getY()*adjust + stick_avg*(1-adjust);
    }

    driveSubsystem.drive(lspd, rspd);
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
