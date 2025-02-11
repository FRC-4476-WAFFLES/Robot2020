/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class IntakeSubsystem extends SubsystemBase {
  private final DoubleSolenoid intakeExtend = new DoubleSolenoid(Constants.INTAKE_EXTEND, Constants.INTAKE_RETRACT);
  private final TalonSRX intakeRoller = new TalonSRX(Constants.INTAKE_ROLLER);
  private final TalonSRX conveyor = new TalonSRX(Constants.CONVEYOR);
  private final TalonSRX funnel = new TalonSRX(Constants.FUNNEL);
  private final DigitalInput highIR = new DigitalInput(Constants.HIGH_IR);
  private final DigitalInput lowIR = new DigitalInput(Constants.LOW_IR);
  private final DigitalInput midIR = new DigitalInput(Constants.MID_IR);

  /**
   * Creates a new IntakeSubsystem.
   */
  public IntakeSubsystem() {
    conveyor.configContinuousCurrentLimit(20);
    conveyor.configPeakCurrentLimit(30);
    intakeRoller.configContinuousCurrentLimit(20);
    intakeRoller.configPeakCurrentLimit(20);
    intakeRoller.enableCurrentLimit(true);
    funnel.configContinuousCurrentLimit(20);
    funnel.configPeakCurrentLimit(25);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Intake/HighIR", HighIR());
    SmartDashboard.putBoolean("Intake/LowIR", LowIR());
    SmartDashboard.putBoolean("Intake/MidIR", MidIR());
    SmartDashboard.putNumber("Intake/RollerCurrent", intakeRoller.getSupplyCurrent());
    if (DriverStation.getInstance().getGameSpecificMessage().length() > 0) {
      SmartDashboard.putString("Intake/Colour Wheel Value",
          "" + DriverStation.getInstance().getGameSpecificMessage().charAt(0));
    } else {
      SmartDashboard.putString("Intake/Colour Wheel Value", "NONE");
    }

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

  public void intake() {
    intake(1, true);
  }

  public boolean HighIR() {
    return !highIR.get();
  }

  public boolean LowIR() {
    return !lowIR.get();
  }

  public boolean MidIR() {
    return !midIR.get();
  }

  public void intake(final double percent, boolean withConveyor) {

    if (percent < 0) {
      conveyor.set(ControlMode.PercentOutput, 1);
    } else {
      if (!HighIR() && LowIR()) {
        conveyor.set(ControlMode.PercentOutput, -percent * 0.9);
      } else if (HighIR() && !MidIR() && LowIR()) {
        conveyor.set(ControlMode.PercentOutput, -percent * 0.9);
      } else {
        conveyor.set(ControlMode.PercentOutput, 0);
      }
    }

    // conveyor.set(ControlMode.PercentOutput, -percent*0.5);
    intakeRoller.set(ControlMode.PercentOutput, percent * 0.9);
    funnel.set(ControlMode.PercentOutput, -percent * 0.5);
  }

  public void run(final double percent) {
    intakeRoller.set(ControlMode.PercentOutput, percent);
    conveyor.set(ControlMode.PercentOutput, -percent);
    funnel.set(ControlMode.PercentOutput, -0.5);
  }

  public void stop() {
    intakeRoller.set(ControlMode.PercentOutput, 0);
    conveyor.set(ControlMode.PercentOutput, 0);
    funnel.set(ControlMode.PercentOutput, 0);
  }

  public void unrun(final double spd) {
    intakeRoller.set(ControlMode.PercentOutput, 0);
    conveyor.set(ControlMode.PercentOutput, -spd);
    funnel.set(ControlMode.PercentOutput, -0.5);
  }

  public double getIntakeCurrent(){
    return intakeRoller.getStatorCurrent();
  }

  public double getFunnelCurrent(){
    return funnel.getStatorCurrent();
  }

  public double getConveyorCurrent(){
    return conveyor.getStatorCurrent();
  }
}
