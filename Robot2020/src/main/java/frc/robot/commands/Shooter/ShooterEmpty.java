/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.RobotContainer.*;

public class ShooterEmpty extends CommandBase {
  Timer emptyDelay = new Timer();

  /**
   * Creates a new ShooterEmpty.
   */
  public ShooterEmpty() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  @Override
  public void initialize() {
    emptyDelay.stop();
    emptyDelay.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(!intakeSubsystem.LowIR() && !intakeSubsystem.HighIR() && !intakeSubsystem.MidIR()) {
      emptyDelay.start();
      if(emptyDelay.get() > 0.3) {
        return true;
      }
    } else {
      emptyDelay.stop();
      emptyDelay.reset();
    }
    return false;
  }
}
