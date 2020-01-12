/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends SubsystemBase {
  TalonSRX shooterMaster = new TalonSRX(Constants.SHOOTER_MASTER);
  VictorSPX shooterFollower = new VictorSPX(Constants.SHOOTER_FOLLOWER);
  TalonSRX shooterPrototype = new TalonSRX(Constants.PROTOTYPE_SHOOTER);
  PowerDistributionPanel pdp = new PowerDistributionPanel(0);
  boolean init = true;
  /**
   * Creates a new ShooterSubsystem.
   */
  public ShooterSubsystem() {
    shooterFollower.follow(shooterMaster);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("PDP/Temp", pdp.getTemperature());
    SmartDashboard.putNumber("PDP/Tot Current", pdp.getTotalCurrent());
    SmartDashboard.putNumber("PDP/shooter Current", pdp.getCurrent(12));
    if(init){
      shooterPrototype.configContinuousCurrentLimit(23, 10);
      shooterPrototype.configPeakCurrentLimit(30, 10);
      shooterPrototype.enableCurrentLimit(true);
      init = false;
    }
  }

  public void setSpeed(double speed) {
    shooterMaster.set(ControlMode.PercentOutput, speed);
    shooterPrototype.set(ControlMode.PercentOutput, speed);
  }
}
