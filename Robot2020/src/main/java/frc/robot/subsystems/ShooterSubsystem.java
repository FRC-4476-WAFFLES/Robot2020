/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.utils.Preference;

import java.util.Map;
import java.util.TreeMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class ShooterSubsystem extends SubsystemBase {
  TalonSRX shooterMaster = new TalonSRX(Constants.SHOOTER_MASTER);
  VictorSPX shooterFollower = new VictorSPX(Constants.SHOOTER_FOLLOWER);

  // A mapping of speeds (in RPM) to output percentages.
  final TreeMap<Double, Double> feed_forwards = new TreeMap<Double, Double>(Map.of(6000.0, 1.0));

  /**
   * Creates a new ShooterSubsystem.
   */
  public ShooterSubsystem() {
    // Use default settings
    shooterFollower.configFactoryDefault();
    shooterMaster.configFactoryDefault();

    // Follower follows master
    shooterFollower.follow(shooterMaster);

    // Use Quad Encoder input
    shooterMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Constants.kTimeoutMs);
    shooterMaster.setSensorPhase(true);

    shooterMaster.configNominalOutputForward(0, Constants.kTimeoutMs);
    shooterMaster.configNominalOutputReverse(0, Constants.kTimeoutMs);
    shooterMaster.configPeakOutputForward(1, Constants.kTimeoutMs);
    shooterMaster.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    shooterMaster.configContinuousCurrentLimit(23, 10);
    shooterMaster.configPeakCurrentLimit(30, 10);
    shooterMaster.enableCurrentLimit(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("PDP/Temp", RobotContainer.pdp.getTemperature());
    SmartDashboard.putNumber("PDP/Tot Current", RobotContainer.pdp.getTotalCurrent());
    SmartDashboard.putNumber("PDP/shooter Current", RobotContainer.pdp.getCurrent(12));

    // Config the Velocity closed loop gains in slot0
    shooterMaster.config_kP(0, Preference.getDouble("Shooter/kP", 0.001), Constants.kTimeoutMs);
    shooterMaster.config_kI(0, Preference.getDouble("Shooter/kI", 0.001), Constants.kTimeoutMs);
    shooterMaster.config_kD(0, Preference.getDouble("Shooter/kD", 1.0), Constants.kTimeoutMs);

    // Show motor velocity in RPM on the dashboard
    final double unitsPer100Ms = shooterMaster.getSelectedSensorVelocity();
    final double rpm = unitsPer100Ms * Constants.kShooterUnitsPer100MsToRPM;
    SmartDashboard.putNumber("Shooter/Speed (rpm)", rpm);
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
    final double outputSpeed = (entry.getKey() / Constants.kShooterUnitsPer100MsToRPM);
    final double kF = (output * 1023) / outputSpeed;

    // Set the feed-forward
    shooterMaster.config_kF(0, kF, Constants.kTimeoutMs);

    // Set the motor setpoint
    final double unitsPer100Ms = rpm / Constants.kShooterUnitsPer100MsToRPM;
    shooterMaster.set(ControlMode.Velocity, unitsPer100Ms);
  }

  /**
   * Stop the shooter wheel from spinning.
   */
  public void stop() {
    shooterMaster.set(ControlMode.Disabled, 0.0);
  }
}
