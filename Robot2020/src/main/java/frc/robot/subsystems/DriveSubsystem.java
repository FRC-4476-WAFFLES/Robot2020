/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Victor;
import frc.robot.Constants;

public class DriveSubsystem extends SubsystemBase {
  Victor driveLeft1 = new Victor(Constants.DRIVE_LEFT_1);
  Victor driveLeft2 = new Victor(Constants.DRIVE_LEFT_2);
  Victor driveLeft3 = new Victor(Constants.DRIVE_LEFT_3);
  Victor driveRight1 = new Victor(Constants.DRIVE_RIGHT_1);
  Victor driveRight2 = new Victor(Constants.DRIVE_RIGHT_2);
  Victor driveRight3 = new Victor(Constants.DRIVE_RIGHT_3);


  /**
   * Creates a new DriveSubsystem.
   */
  public DriveSubsystem() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void drive(double left, double right) {
    driveLeft1.set(-left);
    driveLeft2.set(-left);
    driveLeft3.set(-left);
    driveRight1.set(right);
    driveRight2.set(right);
    driveRight3.set(right);
  }
}