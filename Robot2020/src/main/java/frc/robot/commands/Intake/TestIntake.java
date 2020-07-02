/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class TestIntake extends CommandBase {
  double totalCurrentIntake = 0;
  double totalCurrentConveyor = 0;
  double totalCurrentFunnel = 0;
  double maxCurrentIntake = 0;
  double maxCurrentConveyor = 0;
  double maxCurrentFunnel = 0;
  int loops = 0;

  /**
   * Creates a new TestIntake.
   */
  public TestIntake() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intakeSubsystem.run();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double intakeCurrent = intakeSubsystem.getIntakeCurrent();
    double conveyorCurrent = intakeSubsystem.getConveyorCurrent();
    double funnelCurrent = intakeSubsystem.getFunnelCurrent();
    totalCurrentIntake += intakeCurrent;
    totalCurrentConveyor += conveyorCurrent;
    totalCurrentFunnel += funnelCurrent;
    if (intakeCurrent > maxCurrentIntake) {
      maxCurrentIntake = intakeCurrent;
    }
    if (conveyorCurrent > maxCurrentConveyor) {
      maxCurrentConveyor = conveyorCurrent;
    }
    if (funnelCurrent > maxCurrentFunnel) {
      maxCurrentFunnel = funnelCurrent;
    }
    loops++;

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
