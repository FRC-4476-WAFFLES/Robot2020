/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.XboxController;

public class IntakeDefault extends CommandBase {
  private final IntakeSubsystem intakeSubsystem;
  private final XboxController operate;
  /**
   * Creates a new IntakeDefault.
   */
  public IntakeDefault(IntakeSubsystem intakeSubsystem, XboxController operate) {
    this.intakeSubsystem = intakeSubsystem;
    this.operate = operate;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double in = operate.getRawAxis(3);
    double out = operate.getRawAxis(2);
    intakeSubsystem.run((in*in + out*out)*1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
