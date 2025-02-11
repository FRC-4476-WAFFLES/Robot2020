/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.utils.PreferenceManager;

import java.util.Map;
import java.util.TreeMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.Solenoid;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ShooterSubsystem extends SubsystemBase {
  private final CANSparkMax shooterMaster = new CANSparkMax(Constants.SHOOTER_MASTER, MotorType.kBrushless);
  private final CANSparkMax shooterFollower = new CANSparkMax(Constants.SHOOTER_FOLLOWER, MotorType.kBrushless);
  private final TalonSRX shooterFeeder = new TalonSRX(Constants.SHOOTER_PREP);
  private final CANEncoder shooterEncoder = shooterMaster.getEncoder();
  private final Solenoid hood = new Solenoid(Constants.HOOD);

  public boolean shouldIntake = false;
  public double savedConsistentArea = 0;
  private double targetRpm = 0;

  private final int ACCEPTABLE_VELOCITY_ERROR = 300;

  // A mapping of speeds (in RPM) to output volatages.
  final TreeMap<Double, Double> feed_forwards = new TreeMap<Double, Double>(
      Map.of(2500.0, 0.5, 3300.0, 0.6, 3800.0, 0.7, 4400.0, 0.8, 4900.0, 0.9, 5500.0, 1.0));

  /**
   * Creates a new ShooterSubsystem.
   */
  public ShooterSubsystem() {
    shooterMaster.setSmartCurrentLimit(23);
    shooterFollower.setSmartCurrentLimit(23);
    shooterMaster.setIdleMode(IdleMode.kCoast);
    shooterFollower.setIdleMode(IdleMode.kCoast);

    // Follower follows master (inverted)
    shooterFollower.follow(shooterMaster, true);

    shooterFeeder.configContinuousCurrentLimit(30);
    shooterFeeder.configPeakCurrentLimit(30);
    shooterFeeder.enableCurrentLimit(true);

    var pid = shooterMaster.getPIDController();
    PreferenceManager.whenChanged("Shooter/kP", 0.001, (v) -> pid.setP(v));
    PreferenceManager.whenChanged("Shooter/kI", 0.000, (v) -> pid.setI(v));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("PDP/Temp", RobotContainer.pdp.getTemperature());
    SmartDashboard.putNumber("PDP/Tot Current", RobotContainer.pdp.getTotalCurrent());
    SmartDashboard.putNumber("PDP/shooter Current", RobotContainer.pdp.getCurrent(12));
    SmartDashboard.putNumber("Shooter/FeederCurrent", shooterFeeder.getSupplyCurrent());
    SmartDashboard.putNumber("Shooter/Consistent Area", savedConsistentArea);

    // Show motor velocity in RPM on the dashboard
    // Close speed is 4300
    // Far speed is 4700
    SmartDashboard.putNumber("Shooter/Speed (rpm)", shooterEncoder.getVelocity());
  }

  /**
   * Sets the speed of the shooter wheel.
   * 
   * @param rpm Speed in rotations per minute.
   */
  public void setSpeed(final double rpm) {
    // Estimate an output value that best fits the set speed
    Map.Entry<Double, Double> entry = feed_forwards.floorEntry(rpm);
    if (entry == null) {
      entry = feed_forwards.lastEntry();
    }

    // Calculate the raw voltage output that will be closest to the speed.
    final double output = entry.getValue();
    final double estimatedRpm = entry.getKey();
    final double kF = output / estimatedRpm;

    // Set the motor setpoint and feed-forward
    shooterMaster.getPIDController().setFF(kF);
    shooterMaster.getPIDController().setReference(-rpm, ControlType.kVelocity);
    targetRpm = rpm;

  }

  /**
   * Stop the shooter wheel from spinning.
   */
  public void stop() {
    // Workaround for snobotsim bug #147
    {
      shooterMaster.getPIDController().setP(0);
      shooterMaster.getPIDController().setI(0);
      shooterMaster.getPIDController().setFF(0);
      shooterMaster.getPIDController().setReference(0, ControlType.kVelocity);
    }

    // Set output to zero.
    shooterMaster.set(0);
    targetRpm = 0;
  }

  public boolean canShoot() {
    return Math.abs(shooterEncoder.getVelocity() - targetRpm) < ACCEPTABLE_VELOCITY_ERROR
        && Math.abs(targetRpm) > 1000;
  }

  public void feed(boolean feeding, double percent) {
    if (feeding) {
      shooterFeeder.set(ControlMode.PercentOutput, -percent);
      shouldIntake = true;
    } else {
      shooterFeeder.set(ControlMode.PercentOutput, 0);
      shouldIntake = false;
    }
  }

  public void moveHood(Boolean up) {
    hood.set(up);
  }

  public void unfeed() {
    shooterFeeder.set(ControlMode.PercentOutput, 0.5);
  }

}
