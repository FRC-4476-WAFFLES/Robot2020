/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Constants;


public class ClimberSubsystem extends SubsystemBase {
  private TalonSRX climberDeploy = new TalonSRX(Constants.CLIMBER_DEPLOY);
  private TalonSRX climberWinchRight = new TalonSRX(Constants.CLIMBER_RIGHT_WINCH);
  private TalonSRX climberWinchLeft = new TalonSRX(Constants.CLIMBER_LEFT_WINCH);
  public static final float DEFAULT_DEPLOY_POSTION = 0;
  public static final float DEPLOY_CENTER_LOW = 0;
  public static final float DEPLOY_CENTER = 0;
  public static final float DEPLOY_CENTER_HIGH = 0;
  public boolean isGoingToSetPoint = true;
  /**
   * Creates a new ClimberSubsystem.
   */
  public ClimberSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setDeploySetpoint(float point){

  }
  public void setDeployWinchSetpoint(float point){

  }
  public void setWinchSetpoint(float point){

  }
  public float getDeployError(){
    return climberDeploy.getClosedLoopError();
  }
  public int getDeployPosition(){
    return climberDeploy.getSelectedSensorPosition();
  }
  public int getLeftWinchPosition(){
    return climberWinchLeft.getSelectedSensorPosition();
  }
  public int getRigthWinchPosition(){
    return climberWinchRight.getSelectedSensorPosition();
  }
  public int getAvgWinchPositions(){
    float avg = (getLeftWinchPosition() + getRigthWinchPosition())/2;
    return (int)avg;
  }
}
