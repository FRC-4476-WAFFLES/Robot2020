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
import java.lang.System;

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

    double joystickDiff = Math.abs(leftJoystick.getY() - rightJoystick.getY());
    // if within margin of error, adjust the output
    // if (joystickDiff < 0.07){
      // System.out.println("small diff: left = " + leftJoystick.getY() + " right = " + rightJoystick.getY());
      double lspd = leftJoystick.getY();
      double rspd = rightJoystick.getY();
      // value overwritten
      // Hand fasterStick = Hand.kLeft;
      double stick_avg = (leftJoystick.getY() + rightJoystick.getY())/2;
      // get which stick is faster than the other
      // if(Math.abs(rightJoystick.getY()) > Math.abs(leftJoystick.getY())){
      //   fasterStick = Hand.kRight;
      // }else{
      //   fasterStick = Hand.kLeft;
      // }

      double adjust = joystickDiff/Math.abs(stick_avg)/2;
      if(adjust < 1){
        lspd = leftJoystick.getY()*adjust + stick_avg*(1-adjust);
        rspd = rightJoystick.getY()*adjust + stick_avg*(1-adjust);
      }

      //figure out what speed the robot should aim for
      // if(joystickDiff < 0.02){
      //   // if within super margin of error, set the output to the same speed
      //   System.out.println("average");
      //   lspd = stick_avg;
      //   rspd = stick_avg;
      // }else{
      // System.out.println("square");
      // if within margin of error, but outside super margin
      // double coef = (leftJoystick.getY()/Math.abs(leftJoystick.getY()));
      // if(fasterStick == Hand.kLeft){
      //   lspd = coef*(Math.abs(stick_avg) - (joystickDiff*14.3)*(joystickDiff*14.3)/14.3);
      //   rspd = coef*(Math.abs(stick_avg));
      // }else{
      //   lspd = coef*(Math.abs(stick_avg));
      //   rspd = coef*(Math.abs(stick_avg) - (joystickDiff*14.3)*(joystickDiff*14.3)/14.3);
      //   // }
      // }


      driveSubsystem.drive(lspd, rspd);
    // }else{
    //   // System.out.println("large diff");
    //   driveSubsystem.drive(leftJoystick.getY(), rightJoystick.getY());
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
