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
import com.analog.adis16448.frc.ADIS16448_IMU;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import edu.wpi.first.wpilibj.controller.PIDController;

public class DriveSubsystem extends SubsystemBase {
  private final TalonFX driveLeft1 = new TalonFX(Constants.DRIVE_LEFT_1);
  private final TalonFX driveLeft2 = new TalonFX(Constants.DRIVE_LEFT_2);
  private final TalonFX driveRight1 = new TalonFX(Constants.DRIVE_RIGHT_1);
  private final TalonFX driveRight2 = new TalonFX(Constants.DRIVE_RIGHT_2);
  private final AnalogInput frontUltrasonic = new AnalogInput(Constants.FRONT_ULTRASONIC);
  private final ADIS16448_IMU gyro = new ADIS16448_IMU();
  public boolean hyperspeed_happyface_ = false;
  private boolean has_disabled_hyperspeed_sadface_ = true;
  // Odometry class for tracking robot pose
  private final DifferentialDriveOdometry m_odometry;
  //TODO: make sure this pid is tuned
  PIDController aim = new PIDController(0, 0, 0);

  /**
   * Creates a new DriveSubsystem.
   */
  public DriveSubsystem() {
    driveLeft1.configFactoryDefault();
    driveLeft2.configFactoryDefault();
    driveRight1.configFactoryDefault();
    driveRight2.configFactoryDefault();

    // make the secondary motors copy exactly what the master talons do.
    driveLeft2.follow(driveLeft1);
    driveLeft2.setInverted(InvertType.FollowMaster);
    driveRight2.follow(driveRight1);
    driveRight2.setInverted(InvertType.FollowMaster);

    // sensors are quadratic (greyhills)
    driveLeft1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    driveRight1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    driveLeft1.setInverted(true);

    //Odometry
    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
  }

  @Override
  public void periodic() {
    final double length = frontUltrasonic.getValue() * Constants.ulrasonicValueToInches;
    SmartDashboard.putNumber("Drive/ultarsonic value", frontUltrasonic.getValue());
    SmartDashboard.putNumber("Drive/length ultras", length);
    SmartDashboard.putNumber("Drive/DriveOut", driveLeft1.getMotorOutputPercent());
    // This method will be called once per scheduler run

    // Odometry 
    m_odometry.update(
      Rotation2d.fromDegrees(getHeading()),
      nativeToMPerS(driveLeft1.getSelectedSensorPosition()),
      nativeToMPerS(driveRight1.getSelectedSensorPosition()));
  }

  public void drive(final double left, final double right) {
    driveLeft1.set(ControlMode.PercentOutput, left);
    driveRight1.set(ControlMode.PercentOutput, right);
    SmartDashboard.putNumber("Drive/left", left);
    if(hyperspeed_happyface_){
      //remove any drive restrictions, current limiting, etc.
      driveLeft1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(false, 22, 22, 0.03));
      driveRight1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(false, 22, 22, 0.03));
      //TODO: make this disable other subsystems to redirect as much power as possible to the drive.
    }else if(!has_disabled_hyperspeed_sadface_){
      driveLeft1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 22, 22, 0.03));
      driveRight1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 22, 22, 0.03));
      has_disabled_hyperspeed_sadface_ = true;
    }
  }

  public double getHeading() {
    //TODO: make sure it is not reversed
    return Math.IEEEremainder(gyro.getAngle(), 360) * (1.0);
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    double left = nativeToMPerS(driveLeft1.getSelectedSensorVelocity());
    double right = nativeToMPerS(driveRight1.getSelectedSensorVelocity());
    return new DifferentialDriveWheelSpeeds(left, right);
  }

  public void tankDriveVoltage(double leftVoltage, double rightVoltage) {
    driveLeft1.set(ControlMode.PercentOutput, leftVoltage / driveLeft1.getBusVoltage());
    driveRight1.set(ControlMode.PercentOutput, rightVoltage / driveRight1.getBusVoltage());
  }
  
  private double nativeToMPerS(double convert){
    //in: m/s
    //out: tics/0.1sec
    double codesPerRot = 256;
    double ticsPerCode = 4;
    double diameter_inchesPerRot = 6;
    double metersPerInch = 0.0254;
    double timeshift = 0.1;
    return (convert/(codesPerRot*ticsPerCode*timeshift))*(metersPerInch*diameter_inchesPerRot);
  }

  public void aimTowards(double angle){
    //TODO: make sure this is added and not subtracted from the gyro
    double out = aim.calculate(angle, 0);
    drive(-out, out);    
  }
}
