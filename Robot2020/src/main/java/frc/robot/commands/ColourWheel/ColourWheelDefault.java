/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ColourWheel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColourWheelThingySubsystem;
import frc.robot.subsystems.ColourWheelThingySubsystem.Direction;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class ColourWheelDefault extends CommandBase {
  private final ColourWheelThingySubsystem sys;
  private final XboxController operate;
  private boolean hasPressedLeftBumper = false;
  /**
   * Creates a new ColourWheelDefault.
   */
  public ColourWheelDefault(ColourWheelThingySubsystem colourWheelThingySubsystem, XboxController operate) {
    this.sys = colourWheelThingySubsystem;
    this.operate = operate;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(colourWheelThingySubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(operate.getBumperPressed(Hand.kLeft) && !hasPressedLeftBumper){
      if(sys.deploymentState){
        sys.recall();
      }else{
        sys.deploy();
      }
    }
    if(operate.getStartButton()){
      sys.setDirection(Direction.Right);
    }else if(operate.getBackButton()){
      sys.setDirection(Direction.Left);
    }else{
      sys.setDirection(Direction.Stop);
    }
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
