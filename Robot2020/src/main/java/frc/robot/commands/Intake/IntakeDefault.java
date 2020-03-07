/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;
import edu.wpi.first.wpilibj.Timer;

public class IntakeDefault extends CommandBase {
  private Timer minball = new Timer();
  /**
   * Creates a new IntakeDefault.
   */
  public IntakeDefault() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    minball.reset();
    minball.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!shooterSubsystem.shouldIntake) {
      double in = operate.getRawAxis(3);
      double out = operate.getRawAxis(2);
      double combined = (in * in - out * out) * 0.5;
      if(combined <= 0.03){
        minball.reset();
        minball.start();
      }

      if(minball.get() < 0.6){
        intakeSubsystem.intake(combined, true);
      }else if(intakeSubsystem.HighIR() && intakeSubsystem.LowIR()){
        intakeSubsystem.intake(combined, false);
      }else if(intakeSubsystem.LowIR()){
        intakeSubsystem.intake(combined, true);
      }else{
        intakeSubsystem.intake(combined, false);
      }
    } else {
      intakeSubsystem.run();
    }

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
