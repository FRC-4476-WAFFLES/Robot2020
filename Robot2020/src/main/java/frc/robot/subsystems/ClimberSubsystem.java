/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;
import frc.robot.utils.PreferenceManager;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import edu.wpi.first.wpilibj.Solenoid;

public class ClimberSubsystem extends SubsystemBase {
  //////////////////////////////////////// encoders need to be more positive as
  //////////////////////////////////////// they extend
  private TalonSRX climberDeploy = new TalonSRX(Constants.CLIMBER_DEPLOY);
  private CANSparkMax climberWinchRight = new CANSparkMax(Constants.CLIMBER_RIGHT_WINCH, CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax climberWinchLeft = new CANSparkMax(Constants.CLIMBER_LEFT_WINCH, CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANEncoder climberWinchEncoderLeft = climberWinchLeft.getEncoder();
  private CANEncoder climberWinchEncoderRight = climberWinchRight.getEncoder();
  // work in rotations
  private CANPIDController climberWinchPIDLeft = climberWinchLeft.getPIDController();
  private CANPIDController climberWinchPIDRight = climberWinchRight.getPIDController();

  private Solenoid climberLock = new Solenoid(Constants.CLIMBER_LOCK);

  // TODO: make sure the setpoints are correct
  private static final int[] deploySetpoints = new int[] { 0, 0, 0, 0 };
  public boolean isGoingToSetPoint = true;
  private boolean isClimbLocked = false;

  private int currentDeploySetpoint = 0;

  // TODO: make sure the deploy is stowed before climbing
  public static final int deployStowedThreshold = 0;

  /**
   * Creates a new ClimberSubsystem.
   */
  public ClimberSubsystem() {
    climberDeploy.setSelectedSensorPosition(0);
    climberWinchEncoderLeft.setPosition(0);
    climberWinchEncoderRight.setPosition(0);
    climberWinchLeft.setSmartCurrentLimit(20);
    climberWinchRight.setSmartCurrentLimit(20);
    climberDeploy.configContinuousCurrentLimit(20);
    climberDeploy.configPeakCurrentLimit(20);

    PreferenceManager.watchSrxPID("climberDeploy", climberDeploy, 0.0, 0.0, 0.0);
    // TODO: make sure these motors dont need spearate PIDs
    PreferenceManager.watchNeoPID("climberWinch", climberWinchPIDLeft, 0.0, 0.0, 0.0);
    PreferenceManager.watchNeoPID("climberWinch", climberWinchPIDRight, 0.0, 0.0, 0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  // TODO make all these functions do stuff
  public void setDeploySetpoint(int change) {
    currentDeploySetpoint += change;

    if (currentDeploySetpoint < 0) {
      currentDeploySetpoint = 0;
    } else if (currentDeploySetpoint >= deploySetpoints.length) {
      currentDeploySetpoint = deploySetpoints.length - 1;
    }

    climberDeploy.set(ControlMode.Position, deploySetpoints[currentDeploySetpoint]);
  }

  public void changeDeployWinchSetpoint(int change) {
    currentDeploySetpoint += change;

    if (currentDeploySetpoint < 0) {
      currentDeploySetpoint = 0;
    } else if (currentDeploySetpoint >= deploySetpoints.length) {
      currentDeploySetpoint = deploySetpoints.length - 1;
    }
    setDeployWinchSetpoint(deploySetpoints[currentDeploySetpoint]);
  }

  public void setDeployWinchSetpoint(double point) {
    climberDeploy.set(ControlMode.Position, point);

    // TODO: make this relationship a real one, remembering that neos work in rotations
    double deployWinchLinearRelation = point;

    climberWinchPIDLeft.setReference(deployWinchLinearRelation, ControlType.kPosition);
    climberWinchPIDRight.setReference(deployWinchLinearRelation, ControlType.kPosition);
  }

  public void setLeftWinchSetpoint(double point) {
    climberWinchPIDLeft.setReference(point, ControlType.kPosition);
  }

  public void setRightWinchSetpoint(double point) {
    climberWinchPIDRight.setReference(point, ControlType.kPosition);
  }

  public void ToggleWinchLock() {
    if (isClimbLocked) {
      climberLock.set(false);
      isClimbLocked = false;
    } else {
      climberLock.set(true);
      isClimbLocked = true;
    }
  }

  public boolean getIsClimbLocked() {
    return isClimbLocked;
  }

  public float getDeployError() {
    return climberDeploy.getClosedLoopError();
  }

  public int getDeployPosition() {
    return climberDeploy.getSelectedSensorPosition();
  }

  public double getLeftWinchPosition() {
    //returns the rotations of the left winch
    return climberWinchEncoderLeft.getPosition();
  }

  public double getRigthWinchPosition() {
    // returns the rotations of the right winch
    return climberWinchEncoderRight.getPosition();
  }

  public double getAvgWinchPositions() {
    double avg = (getLeftWinchPosition() + getRigthWinchPosition()) / 2;
    return avg;
  }

  private void updateClimberPIDs() {
    Preference.UpdateSRXPIDPreferences("climberDeploy", climberDeploy, 0.0, 0.0, 0.0);
    // TODO: make sure these motors dont need spearate PIDs
    Preference.UpdateNEOPIDPreferences("climberWinch", climberWinchPIDLeft, 0.0, 0.0, 0.0);
    Preference.UpdateNEOPIDPreferences("climberWinch", climberWinchPIDRight, 0.0, 0.0, 0.0);
  }

  // public void MoveWinchDumb(double percent){
  //   // climberWinchLeft.set(percent);
  //   // climberWinchRight.set(-percent);
  //   climberDeploy.set(ControlMode.PercentOutput, percent*0.5);
  // }
}
