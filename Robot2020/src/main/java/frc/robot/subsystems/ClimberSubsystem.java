/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;
import frc.robot.utils.PreferenceManager;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
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

  // TODO: make sure the setpoints are correct
  private static final int[] deploySetpoints = new int[] { -130*2, 200*2, 1400*2 };
  private static final double[] winchSetpoints = new double[] {0, 30, 585.};
  private static final double[] deployFeedForwards = new double[] { 0.05, 0.11, 0.1261 };

  private int currentDeploySetpoint = 0;
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
    climberWinchLeft.setIdleMode(IdleMode.kBrake);
    climberWinchLeft.setInverted(false);
    climberWinchRight.setSmartCurrentLimit(20);
    climberWinchRight.setIdleMode(IdleMode.kBrake);
    climberWinchRight.setInverted(true);

    climberDeploy.configFactoryDefault();
    climberDeploy.configContinuousCurrentLimit(20);
    climberDeploy.configPeakCurrentLimit(20);
    climberDeploy.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
    climberDeploy.setSelectedSensorPosition(0);
    climberDeploy.setSensorPhase(false);
    climberDeploy.setInverted(false);
    climberDeploy.configClosedLoopPeakOutput(0, 0.2);

    PreferenceManager.watchSrxPID("climberDeploy", climberDeploy, 0.0, 0.0, 0.0);
    PreferenceManager.watchNeoPID("climberWinch", climberWinchPIDLeft, 0.0, 0.0, 0.0);
    PreferenceManager.watchNeoPID("climberWinch", climberWinchPIDRight, 0.0, 0.0, 0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Climber/Deploy Position", climberDeploy.getSelectedSensorPosition());
    SmartDashboard.putNumber("Climber/Left Winch", getLeftWinchPosition());
    SmartDashboard.putNumber("Climber/Right Winch", getRigthWinchPosition());
    SmartDashboard.putNumber("Climber/Deploy Error", getDeployError());
    SmartDashboard.putNumber("Climber/Deploy Out", climberDeploy.getMotorOutputPercent());
    SmartDashboard.putNumber("Climber/Ratio", climberDeploy.getSelectedSensorPosition()/getLeftWinchPosition());

    if(Math.abs(getDeployError()) < deployThreshold) {
      isTravelling = false;
    }
  }

  public void changeDeploySetpoint(int change, boolean winchFollows) {
    currentDeploySetpoint += change;

    if (currentDeploySetpoint < 0) {
      currentDeploySetpoint = 0;
    } else if (currentDeploySetpoint >= deploySetpoints.length) {
      currentDeploySetpoint = deploySetpoints.length - 1;
    }

    if(winchFollows){
      climberWinchPIDLeft.setReference(winchSetpoints[currentDeploySetpoint], ControlType.kPosition);
      climberWinchPIDRight.setReference(winchSetpoints[currentDeploySetpoint], ControlType.kPosition);
    }

    isTravelling = true;
    if(change < 0 && winchFollows && currentDeploySetpoint != 0){
      climberDeploy.set(ControlMode.PercentOutput, 0.2);
    }else{
      climberDeploy.set(ControlMode.Position, deploySetpoints[currentDeploySetpoint], DemandType.ArbitraryFeedForward, deployFeedForwards[currentDeploySetpoint]);
    }
  }

  public void undeploy(){
    climberDeploy.set(ControlMode.Position, 0);
  }

  public void climb(){
    climberDeploy.set(ControlMode.Position, 0);
    climberWinchPIDLeft.setReference(200, ControlType.kPosition);
    climberWinchPIDRight.setReference(200, ControlType.kPosition);
  }

  public void deployFudge(double change) {
    // currentDeployFudge += change * 0.02;
    // climberDeploy.set(ControlMode.Position, deploySetpoints[currentDeploySetpoint] + currentDeployFudge);
  }

  public void setLeftWinchSetpoint(double point) {
    climberWinchPIDLeft.setReference(point, ControlType.kPosition);
  }

  public void setRightWinchSetpoint(double point) {
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
