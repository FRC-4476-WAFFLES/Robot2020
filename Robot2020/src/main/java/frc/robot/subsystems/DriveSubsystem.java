/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.PreferenceManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import com.analog.adis16448.frc.ADIS16448_IMU;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;

import edu.wpi.first.wpilibj.controller.PIDController;

public class DriveSubsystem extends SubsystemBase {
  private final TalonFX driveLeft1 = new TalonFX(Constants.DRIVE_LEFT_1);
  private final TalonFX driveLeft2 = new TalonFX(Constants.DRIVE_LEFT_2);
  private final TalonFX driveRight1 = new TalonFX(Constants.DRIVE_RIGHT_1);
  private final TalonFX driveRight2 = new TalonFX(Constants.DRIVE_RIGHT_2);
  private final ADIS16448_IMU gyro = new ADIS16448_IMU();
  public boolean hyperspeed_happyface_ = false;
  private boolean has_disabled_hyperspeed_sadface_ = true;
  // Odometry class for tracking robot pose
  private final DifferentialDriveOdometry m_odometry;
  PIDController aim = new PIDController(0.1, 0, 0);
  public PIDController auto_line = new PIDController(0.1, 0, 0);
  public PIDController auto_turn = new PIDController(0.1, 0, 0);

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

    // current limiting
    driveLeft1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 60, 60, 0.03));
    driveRight1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 60, 60, 0.03));

    // Sensor config
    driveLeft1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    driveRight1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    driveLeft1.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms);
    driveRight1.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_50Ms);
    driveLeft1.configVelocityMeasurementWindow(4);
    driveRight1.configVelocityMeasurementWindow(4);

    driveLeft1.setInverted(true);
    driveRight1.setInverted(false);
    driveLeft1.configVoltageCompSaturation(12);
    driveRight1.configVoltageCompSaturation(12);
    driveLeft1.enableVoltageCompensation(true);
    driveRight1.enableVoltageCompensation(true);

    // coast/brake mode
    driveLeft1.setNeutralMode(NeutralMode.Brake);
    driveLeft2.setNeutralMode(NeutralMode.Brake);
    driveRight1.setNeutralMode(NeutralMode.Brake);
    driveRight2.setNeutralMode(NeutralMode.Brake);

    // Odometry
    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));

    PreferenceManager.watchPIDController("Drive/aim", aim, 0.1, 0, 0);
    PreferenceManager.watchPIDController("Drive/distance", auto_line, 0.1, 0, 0);
    PreferenceManager.watchPIDController("Drive/turn", auto_turn, 0.1, 0, 0);

    setPose(new Pose2d(0, 0, new Rotation2d(0)));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Drive/left", driveLeft1.getMotorOutputPercent());
    SmartDashboard.putNumber("Drive/right", driveRight1.getMotorOutputPercent());
    SmartDashboard.putNumber("Drive/left encoder", nativeToM(driveLeft1.getSelectedSensorPosition()));
    SmartDashboard.putNumber("Drive/right encoder", nativeToM(driveRight1.getSelectedSensorPosition()));
    SmartDashboard.putNumber("Drive/gyro", gyro.getAngle());
    SmartDashboard.putNumber("Drive/PoseX", Units.metersToFeet(getPose().getTranslation().getX()));
    SmartDashboard.putNumber("Drive/PoseY", Units.metersToFeet(getPose().getTranslation().getY()));
    SmartDashboard.putNumber("Drive/PoseR", getPose().getRotation().getDegrees());
    SmartDashboard.putNumber("Drive/leftVelocity", getLeftVelocity());

    if (DriverStation.getInstance().isDisabled()) {
      // coast/brake mode
      driveLeft1.setNeutralMode(NeutralMode.Coast);
      driveLeft2.setNeutralMode(NeutralMode.Coast);
      driveRight1.setNeutralMode(NeutralMode.Coast);
      driveRight2.setNeutralMode(NeutralMode.Coast);
    } else {
      // coast/brake mode
      driveLeft1.setNeutralMode(NeutralMode.Brake);
      driveLeft2.setNeutralMode(NeutralMode.Brake);
      driveRight1.setNeutralMode(NeutralMode.Brake);
      driveRight2.setNeutralMode(NeutralMode.Brake);
    }
    // Odometry
    m_odometry.update(Rotation2d.fromDegrees(getHeading()), -nativeToM(driveLeft1.getSelectedSensorPosition()),
        -nativeToM(driveRight1.getSelectedSensorPosition()));
  }

  public void drive(final double left, final double right) {
    driveLeft1.set(ControlMode.PercentOutput, left);
    driveRight1.set(ControlMode.PercentOutput, right);

    if (hyperspeed_happyface_) {
      // remove any drive restrictions, current limiting, etc.
      driveLeft1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(false, 22, 22, 0.03));
      driveRight1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(false, 22, 22, 0.03));
      // TODO: make this disable other subsystems to redirect as much power as
      // possible to the drive.
    } else if (!has_disabled_hyperspeed_sadface_) {
      driveLeft1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 22, 22, 0.03));
      driveRight1.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 22, 22, 0.03));
      has_disabled_hyperspeed_sadface_ = true;
    }
  }

  public double getHeading() {
    // TODO: make sure it is not reversed
    return Math.IEEEremainder(-gyro.getAngle(), 360) * (1.0);
  }

  public double getRightPos() {
    return driveRight1.getSelectedSensorPosition();
  }

  public double getLeftPos() {
    return driveLeft1.getSelectedSensorPosition();
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public void setPose(Pose2d newPose) {
    driveLeft1.setSelectedSensorPosition(0);
    driveRight1.setSelectedSensorPosition(0);
    m_odometry.resetPosition(newPose, Rotation2d.fromDegrees(getHeading()));
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    double left = nativeToMPerS(driveLeft1.getSelectedSensorVelocity());
    double right = nativeToMPerS(driveRight1.getSelectedSensorVelocity());
    return new DifferentialDriveWheelSpeeds(-left, -right);
  }

  public void tankDriveVoltage(double leftVoltage, double rightVoltage) {
    driveLeft1.set(ControlMode.PercentOutput, -leftVoltage / 12.0);
    driveRight1.set(ControlMode.PercentOutput, -rightVoltage / 12.0);
  }

  public void tankDrivePercent(double left, double right) {
    driveLeft1.set(ControlMode.PercentOutput, left);
    driveRight1.set(ControlMode.PercentOutput, right);
  }

  public double mToNative(double convert) {
    double gearReduction = (50.0 / 14.0) * (54.0 / 20.0);
    double codesPerRot = 2048.0;
    double diameter_inchesPerRot = 6 * Math.PI;
    double metersPerInch = 0.0254;
    return (convert / (metersPerInch * diameter_inchesPerRot)) * (codesPerRot * gearReduction);
  }

  public double nativeToM(double convert) {
    double gearReduction = (50.0 / 14.0) * (54.0 / 20.0);
    double codesPerRot = 2048.0;
    double diameter_inchesPerRot = 6 * Math.PI;
    double metersPerInch = 0.0254;
    return convert * (metersPerInch * diameter_inchesPerRot) / (codesPerRot * gearReduction);
  }

  private double nativeToMPerS(double convert) {
    // in: m/s
    // out: tics/0.1sec
    double timeshift = 0.1;
    return nativeToM(convert) / timeshift;
  }

  public void aimTowards(double angle) {
    double out = aim.calculate(angle, 0);
    tankDrivePercent(out, -out);
  }

  public double clamp(double value, double min, double max) {
    if (value < min) {
      return min;
    } else if (value > max) {
      return max;
    } else {
      return value;
    }
  }

  public double getLeftCurrent() {
    return driveLeft1.getStatorCurrent();
  }

  public double getLeftVelocity() {
    return driveLeft1.getSelectedSensorVelocity();
  }

  public double getRightCurrent() {
    return driveRight1.getStatorCurrent();
  }

  public double getRightVelocity() {
    return driveRight1.getSelectedSensorVelocity();
  }
}
