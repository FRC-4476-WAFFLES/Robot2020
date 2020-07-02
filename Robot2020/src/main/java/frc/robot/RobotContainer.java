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
import frc.robot.commands.Autonomous.BeingFed;
import frc.robot.commands.Autonomous.DriveForward;
import frc.robot.commands.Autonomous.ShootDriveForewardRed;
import frc.robot.commands.Autonomous.ShootandPickup;
import frc.robot.commands.Autonomous.TurnTestAuto;
import frc.robot.commands.Climber.ClimberDefault;
import frc.robot.commands.Climber.MoveClimber;
import frc.robot.commands.Climber.WindRight;
import frc.robot.commands.Climber.windLeft;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.robot.commands.Intake.IntakeDefault;
import frc.robot.commands.Intake.IntakeExtend;
import frc.robot.commands.Intake.IntakeRetract;
import frc.robot.commands.Drive.CameraAim;
import frc.robot.commands.Climber.ClimberClimb;
import frc.robot.commands.Drive.CameraAimDrive;
import frc.robot.subsystems.Camera;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */

 /**
  * Hello! Welcome to the Robot code for the 2019-2020 robot.
  * This is essentially the main part of the robot code where we can plug in all our custom code to the rest of the roborio. 
  * You can think of this as the electrical pannel in your home, its where everything you use plugs in, but you wouldn't normally,  
  * say, plug your phone in to charge there.
  * What actually resides in this file is all the subsystems *while they are running* and button mappings that the robot uses,
  * so we define all of our button functionality here, and when we want to get something from one of the subsystems,
  * we go through this file. 
  * Also in this file is the code that decides what autonomous code to run by looking at what the robot operators set on the smartdashboard
  * as well as any other code that we only want to run once when the power switch is flipped on.
  */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  /**
   * Here is where we instantiate the subsystems, or take them from the mold we write in the subsystems folder, and transform them
   * into a real thing in the code. Subsystems are written as classes that get transformed into objects, meaning that it would be 
   * possible to make multiple clones (objects) of the same subsystem at a time, but doing this would mean they would conflict with 
   * each other and cause lots of problems. Once the subsystem turns into an obkect, the things that you write in the file they come
   * from are able to work, and the numbers they start out with might not stay the same, depending on what you tell them to do.
   * 
   * These subsystems are instantiated with the three prefixes "public", "static", and "final"
   * "public" means that things outside the class RobotContainer (that we are currently in) can see them.
   * "static" means that any time that RobotContainer is instantiated, it will always use the same copy of ClimberSubsystem() (the subsystem object)
   * "final" means that the variable climberSubsystem cannot be reassigned after this, for example you cant then tell the variable to be 
   * a different instance of ClimberSubsystem() once you've already told it to be a ClimberSubsystem(). It makes more sense to think about this as
   * an int, where if you use this prefix, you can't set it to 5 if you already set it to 10.
   * 
   * After the prefixes, there is the type definition. this means that the variable name that follows will only ever be able to hold that type of 
   * variable. since the subsystems are classes, the type of thing the variable should hold is the same as the thing that you instantiate it with.
   * for example, the variable that holds the climber subsystem should be of the type climber subsystem, the same as the class.
   * 
   * After the name of the variable and the equals sign (or assignment operator) is the value, or specific thing that the variable
   * is going to hold. In this case, we want it to hold a brand new instance of the subsystem (since one doesn't exist yet), so 
   * we tell it to make a "new" instance of the class. 
   */
  // subsystems
  //    prefixes          type            name              object (new instance of class.)
  public static final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
  public static final DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public static final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

  /**
   * In the area below, we define all the devices that give us information about the state of the robot's environment, battery
   * info, and commands. 
   * They are defined in mostly the same ways as the subsystems, with the one difference being that we didn't write most of the 
   * classes that we're using, as they are built in, with the execption of the camera class. 
   * The Joystick class is used to get button presses and axis positions of the two main joysticks, usually used by the driver.
   * The XboxController class is used to get the states of the buttons, joysticks, and dPad of the xbox controller, usually used
   * by the operator.
   * The PowerDistributionPanel class is used to get information about battery voltage and current, as well as how much power
   * each of the channels on the pdp are using at any given time. this can be usefull for logging/diagnostics, or for looking at 
   * the current draw of motor controllers that don't report it on thier own. The update cycle of the pdp is pretty slow, so 
   * it's not amazing for uses that are time/accuracy sensitive.
   */
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
    // autoChooser.setDefaultOption("Drive Forward", new PIDDrive(-1.0, 0, 0.1, 0.2, true));// new DriveForward());
    autoChooser.setDefaultOption("Drive Forward", new DriveForward());
    // autoChooser.addOption("Shoot Drive Forward", new ShootDriveForward());
    autoChooser.addOption("Shoot Drive No Vision", new ShootDriveForewardRed());
    autoChooser.addOption("Shoot, Pickup", new ShootandPickup());
    autoChooser.addOption("Turn", new TurnTestAuto());
    autoChooser.addOption("Aim and Shoot", new AimAndShoot());
    autoChooser.addOption("Being Fed Auto", new BeingFed());
    SmartDashboard.putData("Auto Chooser", autoChooser);
    vision.setLEDMode(Camera.CameraLEDMode.Off);
    vision.setProcesingMode(Camera.ProcessingMode.Vision);
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
    final var a = new JoystickButton(operate, XboxController.Button.kA.value);
    final var b = new JoystickButton(operate, XboxController.Button.kB.value);
    // final var back = new JoystickButton(operate, XboxController.Button.kBack.value);
    // final var start = new JoystickButton(operate, XboxController.Button.kStart.value);
    // final var bumperLeft = new JoystickButton(operate, XboxController.Button.kBumperLeft.value);
    final var bumperRight = new JoystickButton(operate, XboxController.Button.kBumperRight.value);
    final var left6 = new JoystickButton(leftJoystick, 6);
    final var left7 = new JoystickButton(leftJoystick, 7);
    final var right10 = new JoystickButton(rightJoystick, 10);
    final var right11 = new JoystickButton(rightJoystick, 11);
    final var left10 = new JoystickButton(leftJoystick, 10);
    final var right6 = new JoystickButton(rightJoystick, 6);

    final var povUp = new POVTrigger(operate, 0);
    final var povDown = new POVTrigger(operate, 180);
    final var povLeft = new POVTrigger(operate, 270);
    final var povRight = new POVTrigger(operate, 90);
    final var doUndeploy = new CollidedWithBarTrigger();

    x.toggleWhenPressed(new ShooterRun());
    y.whileHeld(new ShooterShoot());
    a.whenPressed(new InstantCommand(() -> {shooterSubsystem.savedConsistentArea = 2;}));
    b.whenPressed(new InstantCommand(() -> {shooterSubsystem.savedConsistentArea = 0.75;}));

    // Colour wheel controls
    // start.whileHeld(
    //     new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Right), colourWheelThingySubsystem));
    // back.whileHeld(
    //     new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Left), colourWheelThingySubsystem));
    // start.or(back).whenInactive(
    //     new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Stop), colourWheelThingySubsystem));
    // bumperLeft.toggleWhenActive(
    //     new StartEndCommand(() -> colourWheelThingySubsystem.deploy(), () -> colourWheelThingySubsystem.recall()));


    povUp.whileActiveOnce(new MoveClimber(1));
    povDown.whileActiveOnce(new MoveClimber(-1));
    povLeft.whileActiveOnce(new ClimberClimb());
    povRight.whileActiveOnce(new InstantCommand(() -> climberSubsystem.undeploy()));

    // doUndeploy.whenActive(new ClimberUndeploy().andThen(new ClimberWinchCommand()));
    bumperRight.whenPressed(new CommandSwitch(new IntakeExtend(), new IntakeRetract()));

    left6.or(left7).or(right10).or(right11).whileActiveContinuous(new CameraAimDrive());
    left10.whileActiveOnce(new windLeft());
    right6.whileActiveOnce(new WindRight());
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
