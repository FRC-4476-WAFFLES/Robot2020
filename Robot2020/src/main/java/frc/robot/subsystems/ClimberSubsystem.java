/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;
import frc.robot.utils.PreferenceManager;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
  private static final int[] deploySetpoints = new int[] { -130, 200, 200, 200 };

  private int currentDeploySetpoint = 0;
  private double currentDeployFudge = 0;
  private boolean winchFollows = false;
  private boolean isTravelling = false;

  // TODO: make sure the deploy is stowed before climbing
  public static final int deployThreshold = 40;

  /**
   * Creates a new ClimberSubsystem.
   */
  public ClimberSubsystem() {
    climberWinchEncoderLeft.setPosition(0);
    climberWinchEncoderRight.setPosition(0);
    climberWinchLeft.setSmartCurrentLimit(20);
    climberWinchRight.setSmartCurrentLimit(20);

    climberDeploy.configFactoryDefault();
    climberDeploy.configContinuousCurrentLimit(20);
    climberDeploy.configPeakCurrentLimit(20);
    climberDeploy.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
    climberDeploy.setSelectedSensorPosition(0);
    climberDeploy.setSensorPhase(false);
    climberDeploy.setInverted(false);

    PreferenceManager.watchSrxPID("climberDeploy", climberDeploy, 0.0, 0.0, 0.0);
    // TODO: make sure these motors dont need spearate PIDs
    PreferenceManager.watchNeoPID("climberWinch", climberWinchPIDLeft, 0.0, 0.0, 0.0);
    PreferenceManager.watchNeoPID("climberWinch", climberWinchPIDRight, 0.0, 0.0, 0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Climber/Deploy Position", climberDeploy.getSelectedSensorPosition());
    SmartDashboard.putNumber("Climber/Left Winch", climberWinchEncoderLeft.getPosition());
    SmartDashboard.putNumber("Climber/Right Winch", climberWinchEncoderRight.getPosition());
    SmartDashboard.putNumber("Climber/Deploy Error", getDeployError());

    if(winchFollows) {
      double deployPosition = climberDeploy.getSelectedSensorPosition();
      double deploySetpoint = deploySetpoints[currentDeploySetpoint] + currentDeployFudge;
      double setpoint = deploySetpoint * 1.0; // TODO set the ratio

      climberWinchPIDLeft.setReference(setpoint, ControlType.kPosition);
      climberWinchPIDRight.setReference(setpoint, ControlType.kPosition);
    }

    if(Math.abs(getDeployError()) < deployThreshold) {
      isTravelling = false;
    }

    // TODO: Don't disengage when moving with the ratchet.
    boolean motionLeft = Math.abs(climberWinchLeft.getAppliedOutput()) > 0.05 || Math.abs(climberWinchEncoderLeft.getVelocity()) > 1.0;
    boolean motionRight = Math.abs(climberWinchRight.getAppliedOutput()) > 0.05 || Math.abs(climberWinchEncoderRight.getVelocity()) > 1.0;
    if(motionLeft || motionRight) {
       climberLock.set(true);
    } else {
       climberLock.set(false);
    }
  }

  public void changeDeploySetpoint(int change, boolean winchFollows) {
    this.winchFollows = winchFollows;

    currentDeploySetpoint += change;
    currentDeployFudge = 0;

    if (currentDeploySetpoint < 0) {
      currentDeploySetpoint = 0;
    } else if (currentDeploySetpoint >= deploySetpoints.length) {
      currentDeploySetpoint = deploySetpoints.length - 1;
    }

    isTravelling = true;
    climberDeploy.set(ControlMode.Position, deploySetpoints[currentDeploySetpoint]);
  }

  public void deployFudge(double change) {
    // currentDeployFudge += change * 0.02;
    // climberDeploy.set(ControlMode.Position, deploySetpoints[currentDeploySetpoint] + currentDeployFudge);
  }

  public void setLeftWinchSetpoint(double point) {
    winchFollows = false;
    climberWinchPIDLeft.setReference(point, ControlType.kPosition);
  }

  public void setRightWinchSetpoint(double point) {
    winchFollows = false;
    climberWinchPIDRight.setReference(point, ControlType.kPosition);
  }

  public double getDeployError() {
    return climberDeploy.getClosedLoopError();
  }

  public boolean getIsTravelling() {
    return isTravelling;
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
}
