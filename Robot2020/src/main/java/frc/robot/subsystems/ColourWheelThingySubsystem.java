/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
/// import com.revrobotics.ColorSensorV3; 
import frc.robot.Constants;
// import edu.wpi.first.wpilibj.I2C;

public class ColourWheelThingySubsystem extends SubsystemBase {
  TalonSRX colourWheelMotor = new TalonSRX(Constants.COLOUR_WHEEL_MANIPULATOR);
  // private final I2C.Port i2cPort = I2C.Port.kOnboard;
  /// private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  /**
   * Creates a new ColourWheelThingySubsystem.
   */
  public ColourWheelThingySubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
