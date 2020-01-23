/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class DriveSubsystem extends SubsystemBase {
  private TalonSRX driveLeft1 = new TalonSRX(Constants.DRIVE_LEFT_1);
  private VictorSPX driveLeft2 = new VictorSPX(Constants.DRIVE_LEFT_2);
  private VictorSPX driveLeft3 = new VictorSPX(Constants.DRIVE_LEFT_3);
  private TalonSRX driveRight1 = new TalonSRX(Constants.DRIVE_RIGHT_1);
  private VictorSPX driveRight2 = new VictorSPX(Constants.DRIVE_RIGHT_2);
  private VictorSPX driveRight3 = new VictorSPX(Constants.DRIVE_RIGHT_3);
  private AnalogInput frontUltrasonic = new AnalogInput(Constants.FRONT_ULTRASONIC);
  public boolean hyperspeed_happyface_ = false;
  private boolean has_disabled_hyperspeed_sadface_ = true;


  /**
   * Creates a new DriveSubsystem.
   */
  public DriveSubsystem() {
    //make the secondary motors copy exactly what the master talons do.
    driveLeft2.follow(driveLeft1);
    driveLeft2.setInverted(InvertType.FollowMaster);
    driveLeft3.follow(driveLeft1);
    driveLeft3.setInverted(InvertType.FollowMaster);
    driveRight2.follow(driveRight1);
    driveRight2.setInverted(InvertType.FollowMaster);
    driveRight3.follow(driveRight1);
    driveRight3.setInverted(InvertType.FollowMaster);

    //sensors are quadratic (greyhills)
    driveLeft1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
    driveRight1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
    //TODO: make sure these sensor phases are correct
    driveRight1.setSensorPhase(false);
    driveLeft1.setSensorPhase(true);
    driveRight1.setSelectedSensorPosition(0);
    driveLeft1.setSelectedSensorPosition(0);
    //current limiting
    //TODO: get the correct current limiting values
    driveLeft1.configPeakCurrentDuration(30,10);
    driveLeft1.configPeakCurrentLimit(22, 10);
    driveLeft1.configContinuousCurrentLimit(22,10);
    driveLeft1.enableCurrentLimit(true);

    driveRight1.configPeakCurrentDuration(30,10);
    driveRight1.configPeakCurrentLimit(22, 10);
    driveRight1.configContinuousCurrentLimit(22,10);
    driveRight1.enableCurrentLimit(true);
  }

  @Override
  public void periodic() {
    double length = frontUltrasonic.getValue()*Constants.ulrasonicValueToInches;
    SmartDashboard.putNumber("Drive/ultarsonic value", frontUltrasonic.getValue());
    SmartDashboard.putNumber("Drive/length ultras", length);
    // This method will be called once per scheduler run
  }

  public void drive(double left, double right) {
    driveLeft1.set(ControlMode.PercentOutput, -left);
    driveRight1.set(ControlMode.PercentOutput, right);
    if(hyperspeed_happyface_){
      //remove any drive restrictions, current limiting, etc.
      driveLeft1.enableCurrentLimit(false);
      driveRight1.enableCurrentLimit(false);
      //TODO: make this disable other subsystems to redirect as much power as possible to the drive.
    }else if(!has_disabled_hyperspeed_sadface_){
      driveLeft1.enableCurrentLimit(true);
      driveRight1.enableCurrentLimit(true);
      has_disabled_hyperspeed_sadface_ = true;
    }
  }
}
