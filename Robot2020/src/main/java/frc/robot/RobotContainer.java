/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ColourWheelThingySubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ColourWheelThingySubsystem.Direction;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.Drive.OperatorTankDrive;
import frc.robot.commands.Shooter.ShooterIdle;
import frc.robot.commands.Shooter.ShooterRun;
import frc.robot.commands.Shooter.ShooterShoot;
import frc.robot.commands.Climber.ClimberDefault;
import frc.robot.commands.ColourWheel.ColourWheelDefault;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.robot.commands.Intake.IntakeDefault;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // subsystems
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
  private final ColourWheelThingySubsystem colourWheelThingySubsystem = new ColourWheelThingySubsystem();
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

  // input devices
  public final Joystick leftJoystick = new Joystick(0);
  public final Joystick rightJoystick = new Joystick(1);
  public final XboxController operate = new XboxController(2);
  public final static PowerDistributionPanel pdp = new PowerDistributionPanel(0);

  // Default commands
  private final OperatorTankDrive driveCommand = new OperatorTankDrive(driveSubsystem, leftJoystick, rightJoystick,
      operate);
  private final ShooterIdle shooterIdle = new ShooterIdle(shooterSubsystem);
  private final ClimberDefault climberDefault = new ClimberDefault(climberSubsystem, operate);
  private final IntakeDefault intakeDefault = new IntakeDefault(intakeSubsystem, operate);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    driveSubsystem.setDefaultCommand(driveCommand);
    shooterSubsystem.setDefaultCommand(shooterIdle);
    climberSubsystem.setDefaultCommand(climberDefault);
    intakeSubsystem.setDefaultCommand(intakeDefault);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    JoystickButton a = new JoystickButton(operate, XboxController.Button.kA.value);
    JoystickButton y = new JoystickButton(operate, XboxController.Button.kY.value);
    JoystickButton back = new JoystickButton(operate, XboxController.Button.kBack.value);
    JoystickButton start = new JoystickButton(operate, XboxController.Button.kStart.value);
    JoystickButton bumperLeft = new JoystickButton(operate, XboxController.Button.kBumperLeft.value);

    // TODO: make sure this works as a toggle.
    a.whenPressed(new ShooterRun(shooterSubsystem, operate));
    y.whenPressed(new ShooterShoot(shooterSubsystem, operate));

    // Colour wheel controls
    start.whileHeld(
        new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Right), colourWheelThingySubsystem));
    back.whileHeld(
        new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Left), colourWheelThingySubsystem));
    start.or(back).whenInactive(
        new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Stop), colourWheelThingySubsystem));
    bumperLeft.toggleWhenActive(
        new StartEndCommand(() -> colourWheelThingySubsystem.deploy(), () -> colourWheelThingySubsystem.recall()));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return driveCommand;
  }
}
