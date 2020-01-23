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
import com.revrobotics.ColorSensorV3;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ColourWheelThingySubsystem extends SubsystemBase {
  private final TalonSRX colourWheelMotor = new TalonSRX(Constants.COLOUR_WHEEL_MANIPULATOR);
  private final DoubleSolenoid deployWheel = new DoubleSolenoid(Constants.COLOUR_WHEEL_DEPLOY, Constants.COLOUR_WHEEL_RECALL);
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  public static boolean deploymentState = false;

  public static enum Direction{
    Left, Right, Stop
  }

  /**
   * Creates a new ColourWheelThingySubsystem.
   */
  public ColourWheelThingySubsystem() {
    deployWheel.set(DoubleSolenoid.Value.kReverse);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("ColorWheelThingySubsystem/hue", getHue());
  }
  public void deploy(){
    deployWheel.set(DoubleSolenoid.Value.kForward);
    deploymentState = true;
  }
  public void recall(){
    deployWheel.set(DoubleSolenoid.Value.kReverse);
    deploymentState = false;
  }

  public void setDirection(Direction direction){
    //TODO: make sure the motor spin directions correllates to the set direction
    float spd;
    if(direction == Direction.Left){
      spd = 1;
    }else if(direction == Direction.Right){
      spd = -1;
    }else{
      spd = 0
    }
    colourWheelMotor.set(ControlMode.PercentOutput, spd);
  }

  private double getHue() {
    final double R = m_colorSensor.getRed();
    final double G = m_colorSensor.getGreen();
    final double B = m_colorSensor.getBlue();

    final double hue = (Math.atan2(Math.sqrt(3) * (G - B), 2 * R - G - B) / Math.PI) * 180;

    return hue < 0 ? 360 + hue : hue;
  }
  
  
}
