/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class OperatorTankDrive extends CommandBase {
  private final DriveSubsystem driveSubsystem; 
  private final Joystick leftJoystick, rightJoystick; 
  private final XboxController operate;
  /**
   * Creates a new OperatorTankDrive.
   */
  public OperatorTankDrive(DriveSubsystem driveSubsystem, Joystick leftJoystick, Joystick rightJoystick, XboxController operate) {
    this.driveSubsystem = driveSubsystem;
    this.leftJoystick = leftJoystick;
    this.rightJoystick = rightJoystick;
    this.operate = operate;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.drive(leftJoystick.getY(), rightJoystick.getY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.drive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
