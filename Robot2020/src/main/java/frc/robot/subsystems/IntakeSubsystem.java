/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class IntakeSubsystem extends SubsystemBase {
  private final DoubleSolenoid intakeExtend = new DoubleSolenoid(Constants.INTAKE_EXTEND, Constants.INTAKE_RETRACT);
  private final VictorSPX intakeRoller = new VictorSPX(Constants.INTAKE_ROLLER);

  /**
   * Creates a new IntakeSubsystem.
   */
  public IntakeSubsystem() {
  }

  public void extend() {
    intakeExtend.set(DoubleSolenoid.Value.kForward);
  }

  public void retract() {
    intakeExtend.set(DoubleSolenoid.Value.kReverse);
  }

  public void run() {
    run(1.0);
  }

  public void run(double percent){
    intakeRoller.set(ControlMode.PercentOutput, percent);
  }

  public void stop() {
    intakeRoller.set(ControlMode.PercentOutput, 0);
  }
}
