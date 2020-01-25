/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.Climber.ClimberWinchCommand;

public class ClimberUndeploy extends CommandBase {
  private final ClimberSubsystem climberSubsystem;
  private final XboxController operate;

  // TODO: make sure the deploy is stowed before climbing
  private final int deployStowedThreshold = 0;

  /**
   * Creates a new ClimberUndeploy.
   */
  public ClimberUndeploy(ClimberSubsystem climberSubsystem, XboxController operate) {
    this.climberSubsystem = climberSubsystem;
    this.operate = operate;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (operate.getRawButton(1) && climberSubsystem.getDeployError() > deployStowedThreshold) {
      new ClimberWinchCommand(climberSubsystem, operate).schedule(false);
      return true;
    } else {
      return false;
    }

  }
}
