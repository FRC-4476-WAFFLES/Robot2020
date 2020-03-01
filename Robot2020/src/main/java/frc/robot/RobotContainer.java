/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.triggers.CollidedWithBarTrigger;
import frc.robot.triggers.POVTrigger;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.Drive.OperatorTankDrive;
import frc.robot.commands.Shooter.ShooterIdle;
import frc.robot.commands.Shooter.ShooterRun;
import frc.robot.commands.Shooter.ShooterShoot;
import frc.robot.commands.Utility.CommandSwitch;
import frc.robot.commands.Autonomous.AimAndShoot;
import frc.robot.commands.Autonomous.DriveForward;
import frc.robot.commands.Autonomous.PIDDrive;
import frc.robot.commands.Autonomous.ShootDriveForewardRed;
import frc.robot.commands.Autonomous.ShootDriveForward;
import frc.robot.commands.Climber.ClimberDefault;
import frc.robot.commands.Climber.ClimberUndeploy;
import frc.robot.commands.Climber.ClimberWinchCommand;
import frc.robot.commands.Climber.MoveClimber;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.robot.commands.Intake.IntakeDefault;
import frc.robot.commands.Intake.IntakeExtend;
import frc.robot.commands.Intake.IntakeRetract;
import frc.robot.commands.Drive.CameraAim;
import frc.robot.subsystems.Camera;

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
  public static final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
  public static final DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public static final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

  // input devices
  public static final Joystick leftJoystick = new Joystick(0);
  public static final Joystick rightJoystick = new Joystick(1);
  public static final XboxController operate = new XboxController(2);
  public static final PowerDistributionPanel pdp = new PowerDistributionPanel(0);
  public static final Camera vision = new Camera();

  // Default commands
  private final OperatorTankDrive driveCommand = new OperatorTankDrive();
  private final ShooterIdle shooterIdle = new ShooterIdle();
  private final ClimberDefault climberDefault = new ClimberDefault();
  private final IntakeDefault intakeDefault = new IntakeDefault();

  // Autonomous command chooser
  private final SendableChooser<Command> autoChooser = new SendableChooser<Command>();

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
    CommandScheduler.getInstance().registerSubsystem(vision);

    autoChooser.addOption("Do Nothing", new InstantCommand());
    autoChooser.setDefaultOption("Drive Forward", new PIDDrive(-1.0, 0, 0.1, 0.2, true));// new DriveForward());
    // autoChooser.addOption("Shoot Drive Forward", new ShootDriveForward());
    autoChooser.addOption("Shoot Drive No Vision", new ShootDriveForewardRed());
    autoChooser.addOption("Aim and Shoot", new AimAndShoot());
    SmartDashboard.putData("Auto Chooser", autoChooser);
    vision.setLEDMode(Camera.CameraLEDMode.Off);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    final var x = new JoystickButton(operate, XboxController.Button.kX.value);
    final var y = new JoystickButton(operate, XboxController.Button.kY.value);
    // final var back = new JoystickButton(operate, XboxController.Button.kBack.value);
    // final var start = new JoystickButton(operate, XboxController.Button.kStart.value);
    // final var bumperLeft = new JoystickButton(operate, XboxController.Button.kBumperLeft.value);
    final var bumperRight = new JoystickButton(operate, XboxController.Button.kBumperRight.value);
    final var left6 = new JoystickButton(leftJoystick, 6);
    final var left7 = new JoystickButton(leftJoystick, 7);
    final var right10 = new JoystickButton(rightJoystick, 10);
    final var right11 = new JoystickButton(rightJoystick, 11);

    final var povUp = new POVTrigger(operate, 0);
    final var povDown = new POVTrigger(operate, 180);
    final var doUndeploy = new CollidedWithBarTrigger();

    x.toggleWhenPressed(new ShooterRun());
    y.whileHeld(new ShooterShoot());

    // Colour wheel controls
    // start.whileHeld(
    //     new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Right), colourWheelThingySubsystem));
    // back.whileHeld(
    //     new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Left), colourWheelThingySubsystem));
    // start.or(back).whenInactive(
    //     new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Stop), colourWheelThingySubsystem));
    // bumperLeft.toggleWhenActive(
    //     new StartEndCommand(() -> colourWheelThingySubsystem.deploy(), () -> colourWheelThingySubsystem.recall()));

    povUp.whenActive(new MoveClimber(1));
    povDown.whenActive(new MoveClimber(-1));

    // doUndeploy.whenActive(new ClimberUndeploy().andThen(new ClimberWinchCommand()));
    bumperRight.whenPressed(new CommandSwitch(new IntakeExtend(), new IntakeRetract()));

    left6.or(left7).or(right10).or(right11).whileActiveContinuous(new CameraAim());

    new JoystickButton(rightJoystick, 1).whenPressed(new InstantCommand(() -> shooterSubsystem.moveHood(true)));
    new JoystickButton(leftJoystick, 1).whenPressed(new InstantCommand(() -> shooterSubsystem.moveHood(false)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return autoChooser.getSelected();
  }
}
