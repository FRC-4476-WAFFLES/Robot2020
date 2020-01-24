/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
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
  private DoubleSolenoid climberLock = new DoubleSolenoid(Constants.CLIMBER_LOCK, Constants.CLIMBER_UNLOCK);
  // TODO: make sure the setpoints are correct
  public static final float DEFAULT_DEPLOY_POSTION = 0;
  public static final float DEPLOY_CENTER_LOW = 0;
  public static final float DEPLOY_CENTER = 0;
  public static final float DEPLOY_CENTER_HIGH = 0;
  public boolean isGoingToSetPoint = true;
  public boolean public_isClimbLocked = false;
  private boolean private_isClimbLocked = false;

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
  public void setDeploySetpoint(float point) {
    climberDeploy.set(ControlMode.Position, point);
  }

  public void setDeployWinchSetpoint(double point) {
    // TODO: make this relationship a real one
    double deployWinchLinearRelation = point;

    climberDeploy.set(ControlMode.Position, point);
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
    if (private_isClimbLocked != public_isClimbLocked) {
      System.out.println("private - public climblock state mismatched!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
    if (private_isClimbLocked) {
      climberLock.set(DoubleSolenoid.Value.kReverse);
      private_isClimbLocked = false;
    } else {
      climberLock.set(DoubleSolenoid.Value.kForward);
      private_isClimbLocked = true;
    }
    public_isClimbLocked = private_isClimbLocked;
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
