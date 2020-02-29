/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberSubsystem;

import static frc.robot.RobotContainer.*;

public class ClimberUndeploy extends CommandBase {

  /**
   * Creates a new ClimberUndeploy.
   */
  public ClimberUndeploy() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climberSubsystem);
  }

  @Override
  public void initialize() {
    climberSubsystem.changeDeploySetpoint(-1000, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (operate.getRawButton(1) && climberSubsystem.getDeployError() > ClimberSubsystem.deployThreshold) {
      return true;
    } else {
      return false;
    }
  }
}
