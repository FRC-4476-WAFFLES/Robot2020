/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColourWheelThingySubsystem;
import frc.robot.subsystems.ColourWheelThingySubsystem.Direction;

public class ColourWheelSpinLeft extends CommandBase {
  ColourWheelThingySubsystem sys;
  /**
   * Creates a new ColourWheelSpinLeft.
   * needs to be while held
   */
  public ColourWheelSpinLeft(ColourWheelThingySubsystem colourWheelThingySubsystem) {
    this.sys = colourWheelThingySubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(colourWheelThingySubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    sys.setDirection(Direction.Left);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    sys.setDirection(Direction.Stop);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
