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
import frc.robot.utils.Preference;

public class ClimberSubsystem extends SubsystemBase {
  //////////////////////////////////////// encoders need to be more positive as
  //////////////////////////////////////// they extend
  private TalonSRX climberDeploy = new TalonSRX(Constants.CLIMBER_DEPLOY);
  private TalonSRX climberWinchRight = new TalonSRX(Constants.CLIMBER_RIGHT_WINCH);
  private TalonSRX climberWinchLeft = new TalonSRX(Constants.CLIMBER_LEFT_WINCH);
  // private DoubleSolenoid climberLock = new
  // DoubleSolenoid(Constants.CLIMBER_LOCK, Constants.CLIMBER_UNLOCK);
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
    climberWinchLeft.setSelectedSensorPosition(0);
    climberWinchRight.setSelectedSensorPosition(0);
  }

  @Override
  public void periodic() {
    updateClimberPIDs();

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

    // TODO: make this relationship a real one
    double deployWinchLinearRelation = point;

    climberWinchLeft.set(ControlMode.Position, deployWinchLinearRelation);
    climberWinchRight.set(ControlMode.Position, deployWinchLinearRelation);
  }

  public void setLeftWinchSetpoint(float point) {
    climberWinchLeft.set(ControlMode.Position, point);
  }

  public void setRightWinchSetpoint(float point) {
    climberWinchRight.set(ControlMode.Position, point);
  }

  public void ToggleWinchLock() {
    if (isClimbLocked) {
      // climberLock.set(DoubleSolenoid.Value.kReverse);
      isClimbLocked = false;
    } else {
      // climberLock.set(DoubleSolenoid.Value.kForward);
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

  public int getLeftWinchPosition() {
    return climberWinchLeft.getSelectedSensorPosition();
  }

  public int getRigthWinchPosition() {
    return climberWinchRight.getSelectedSensorPosition();
  }

  public int getAvgWinchPositions() {
    float avg = (getLeftWinchPosition() + getRigthWinchPosition()) / 2;
    return (int) avg;
  }

  private void updateClimberPIDs() {
    Preference.UpdateSRXPIDPreferences("climberDeploy", climberDeploy, 0.0, 0.0, 0.0);
    // TODO: make sure these motors dont need spearate PIDs
    Preference.UpdateSRXPIDPreferences("climberWinch", climberWinchLeft, 0.0, 0.0, 0.0);
    Preference.UpdateSRXPIDPreferences("climberWinch", climberWinchRight, 0.0, 0.0, 0.0);
  }
}
