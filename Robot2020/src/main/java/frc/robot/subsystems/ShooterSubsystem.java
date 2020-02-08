/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.utils.Preference;

import java.util.Map;
import java.util.TreeMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ShooterSubsystem extends SubsystemBase {
  private final CANSparkMax shooterMaster = new CANSparkMax(Constants.SHOOTER_MASTER, MotorType.kBrushless);
  private final CANSparkMax shooterFollower = new CANSparkMax(Constants.SHOOTER_FOLLOWER, MotorType.kBrushless);
  private final VictorSPX shooterFeeder = new VictorSPX(Constants.SHOOTER_FEEDER_1);
  private final DoubleSolenoid hood = new DoubleSolenoid(Constants.HOOD_EXTEND, Constants.HOOD_RETRACT);

  public boolean shouldIntake = false;
  private double targetRpm = 0;

  private final int ACCEPTABLE_VELOCITY_ERROR = 10;

  // A mapping of speeds (in RPM) to output percentages.
  final TreeMap<Double, Double> feed_forwards = new TreeMap<Double, Double>(Map.of(6000.0, 1.0));

  /**
   * Creates a new ShooterSubsystem.
   */
  public ShooterSubsystem() {
    // Follower follows master (inverted)
    shooterFollower.follow(shooterMaster, true);

    shooterMaster.setSmartCurrentLimit(23);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("PDP/Temp", RobotContainer.pdp.getTemperature());
    SmartDashboard.putNumber("PDP/Tot Current", RobotContainer.pdp.getTotalCurrent());
    SmartDashboard.putNumber("PDP/shooter Current", RobotContainer.pdp.getCurrent(12));

    // Config the Velocity closed loop gains in slot0
    var pid = shooterMaster.getPIDController();
    pid.setP(Preference.getDouble("Shooter/kP", 0.001));
    pid.setI(Preference.getDouble("Shooter/kI", 0.001));
    pid.setD(Preference.getDouble("Shooter/kD", 1.0));

    // Show motor velocity in RPM on the dashboard
    SmartDashboard.putNumber("Shooter/Speed (rpm)", shooterMaster.getEncoder().getVelocity());
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

    // Calculate kF and the raw output value
    final double output = entry.getValue();
    final double outputSpeed = entry.getKey();
    final double kF = output / outputSpeed;

    // Set the feed-forward
    shooterMaster.getPIDController().setFF(kF);

    // Set the motor setpoint
    shooterMaster.getPIDController().setReference(rpm, ControlType.kVelocity);
    targetRpm = rpm;
  }

  /**
   * Stop the shooter wheel from spinning.
   */
  public void stop() {
    shooterMaster.set(0);
  }

  public boolean canShoot() {
    return Math.abs(shooterMaster.getEncoder().getVelocity() - targetRpm) < ACCEPTABLE_VELOCITY_ERROR
        && Math.abs(shooterMaster.getEncoder().getVelocity()) > 1000;
  }

  public void feed(boolean feeding) {
    if (feeding) {
      shooterFeeder.set(ControlMode.PercentOutput, 1);
      shouldIntake = true;
    } else {
      shooterFeeder.set(ControlMode.PercentOutput, 0);
      shouldIntake = false;
    }
  }

  public void moveHood(Boolean up) {
    hood.set(up ? Value.kForward : Value.kReverse);
  }

}
