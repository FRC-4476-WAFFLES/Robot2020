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
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.Drive.OperatorTankDrive;
import frc.robot.commands.Shooter.ShooterIdle;
import frc.robot.commands.Shooter.ShooterRun;
import frc.robot.commands.Shooter.ShooterShoot;
import frc.robot.commands.Utility.CommandSwitch;
import frc.robot.commands.Autonomous.DriveForward;
import frc.robot.commands.Autonomous.TrenchPickup;
import frc.robot.commands.Climber.ClimberDefault;
import frc.robot.commands.Climber.MoveClimber;
import frc.robot.commands.Climber.WindRight;
import frc.robot.commands.Climber.WindLeft;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.robot.commands.Intake.IntakeDefault;
import frc.robot.commands.Intake.IntakeExtend;
import frc.robot.commands.Intake.IntakeRetract;
import frc.robot.commands.Drive.CameraAim;
import frc.robot.commands.Climber.ClimberClimb;
import frc.robot.subsystems.Camera;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */

/**
 * Hello! Welcome to the Robot code for the 2019-2020 robot. This is essentially
 * the main part of the robot code where we can plug in all our custom code to
 * the rest of the roborio. You can think of this as the electrical pannel in
 * your home, its where everything you use plugs in, but you wouldn't normally,
 * say, plug your phone in to charge there. What actually resides in this file
 * is all the subsystems *while they are running* and button mappings that the
 * robot uses, so we define all of our button functionality here, and when we
 * want to get something from one of the subsystems, we go through this file.
 * Also in this file is the code that decides what autonomous code to run by
 * looking at what the robot operators set on the smartdashboard as well as any
 * other code that we only want to run once when the power switch is flipped on.
 */
public class RobotContainer {
  /**
   * This is the begining of the RobotContainer class that is a part of all java
   * robots. This area of a class is used for defining variables.
   */
  // The robot's subsystems and commands are defined here...
  /**
   * Here is where we instantiate the subsystems, or take them from the mold we
   * write in the subsystems folder, and transform them into a real thing in the
   * code. Subsystems are written as classes that get transformed into objects,
   * meaning that it would be possible to make multiple clones (objects) of the
   * same subsystem at a time, but doing this would mean they would conflict with
   * each other and cause lots of problems. Once the subsystem turns into an
   * obkect, the things that you write in the file they come from are able to
   * work, and the numbers they start out with might not stay the same, depending
   * on what you tell them to do.
   * 
   * These subsystems are instantiated with the three prefixes "public", "static",
   * and "final" "public" means that things outside the class RobotContainer (that
   * we are currently in) can see them. "static" means that any time that
   * RobotContainer is instantiated, it will always use the same copy of
   * ClimberSubsystem() (the subsystem object) "final" means that the variable
   * climberSubsystem cannot be reassigned after this, for example you cant then
   * tell the variable to be a different instance of ClimberSubsystem() once
   * you've already told it to be a ClimberSubsystem(). It makes more sense to
   * think about this as an int, where if you use this prefix, you can't set it to
   * 5 if you already set it to 10.
   * 
   * After the prefixes, there is the type definition. this means that the
   * variable name that follows will only ever be able to hold that type of
   * variable. since the subsystems are classes, the type of thing the variable
   * should hold is the same as the thing that you instantiate it with. for
   * example, the variable that holds the climber subsystem should be of the type
   * climber subsystem, the same as the class.
   * 
   * After the name of the variable and the equals sign (or assignment operator)
   * is the value, or specific thing that the variable is going to hold. In this
   * case, we want it to hold a brand new instance of the subsystem (since one
   * doesn't exist yet), so we tell it to make a "new" instance of the class.
   */
  // subsystems
  // prefixes type name object (new instance of class.)
  public static final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
  public static final DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public static final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

  // input devices
  /**
   * In the area below, we define all the devices that give us information about
   * the state of the robot's environment, battery info, and commands. They are
   * defined in mostly the same ways as the subsystems, with the one difference
   * being that we didn't write most of the classes that we're using, as they are
   * built in, with the execption of the camera class. The Joystick class is used
   * to get button presses and axis positions of the two main joysticks, usually
   * used by the driver. The XboxController class is used to get the states of the
   * buttons, joysticks, and dPad of the xbox controller, usually used by the
   * operator. The PowerDistributionPanel class is used to get information about
   * battery voltage and current, as well as how much power each of the channels
   * on the pdp are using at any given time. this can be usefull for
   * logging/diagnostics, or for looking at the current draw of motor controllers
   * that don't report it on thier own. The update cycle of the pdp is pretty
   * slow, so it's not amazing for uses that are time/accuracy sensitive. The
   * Camera class is one of our classes that makes it look nicer when we get
   * information from it in the code. To the code, the camera just looks like a
   * table of values, so this class makes it easier to read from and write to that
   * table.
   * 
   * If you ever want to look to see what any class/object can do, you can either
   * "ctrl" + "click" on the name (which brings you to their definition), or for
   * classes that are a part of libraries, find the documentation for that library
   * and search for the name of the class.
   */
  public static final Joystick leftJoystick = new Joystick(0);
  public static final Joystick rightJoystick = new Joystick(1);
  public static final XboxController operate = new XboxController(2);
  public static final PowerDistributionPanel pdp = new PowerDistributionPanel(0);
  public static final Camera vision = new Camera();

  // Default commands
  /**
   * In this section, we tell the program what code to run when there is no
   * special command being called for a specific subsystem. This might happen at
   * the start of the robot code, when a special command has finished running, or
   * when the robot is disabledd; and should tell the robot what to do with the
   * hardware associated with the subsystem when it is in its default state.
   * 
   * To do this, we start by instantiating them into variables, in the same way as
   * we did with the subsystems, so that they remain objects accessible from the
   * robot container. They are instantiated with the prefixes "private" and
   * "final" "private" means that they can only be accessed from within the class
   * RobotContainer, and nowhere else. "Final" means that the variable cannot be
   * reassigned to another object.
   */
  private final OperatorTankDrive driveCommand = new OperatorTankDrive();
  private final ShooterIdle shooterIdle = new ShooterIdle();
  private final ClimberDefault climberDefault = new ClimberDefault();
  private final IntakeDefault intakeDefault = new IntakeDefault();

  // Autonomous command chooser
  /**
   * This is a class that allows us to give the robot operators a multiple choice.
   * The choices are given later, but this line instanitates the object to hold
   * them. The Class is called a SendableChooser, and it will show up on the
   * smartdashboard or the shuffleboard. We assigned it to a variable called
   * autoChooser, since that's what it does, and told it that we were going to
   * give it a bunch of commands to choose between. We could have given it a
   * different type of data to choose between as well, such as numbers or strings,
   * but giving it commands is the most direct. eg for ints, it would have been
   * SendableChooser<int> instead.
   */
  private final SendableChooser<Command> autoChooser = new SendableChooser<Command>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    /**
     * This part of a class is called the constructor. You need one for every class.
     * You can tell that it is the constructor because its name shaddows that of the
     * class. This code is run once at the time the class is instantiated, like you
     * can see above after the "new" keyword.
     */
    // Configure the button bindings
    /**
     * Here we call a function that we defined lower down in the class file.
     * controll-click on it to go to it's definition.
     */
    configureButtonBindings();
    /**
     * here we use the commands we instantiated above and set them as the default
     * commands for their respective subsystems. You may notice that the last line
     * is different from the rest of the subsystems, and the reason for this is that
     * the camera is a subsystem object, but acts as an input device. Because of
     * this, it does not have any commands at all, and so it needs to be registered
     * specially. That line is actually executed for all of the other subsystems as
     * a part of the "setDefaultCommand" function. Running this function means that
     * the function named "periodic" in the subsystem will be updated every robot
     * loop.
     */
    driveSubsystem.setDefaultCommand(driveCommand);
    shooterSubsystem.setDefaultCommand(shooterIdle);
    climberSubsystem.setDefaultCommand(climberDefault);
    intakeSubsystem.setDefaultCommand(intakeDefault);
    CommandScheduler.getInstance().registerSubsystem(vision);

    /**
     * This is where we add the options to the autonomous chooser we defined
     * earlier. Each option consists of both a string of text and a command object.
     * The commands that are supplied act as a list of instructions. One of the
     * objects is set as the default, and this is the one that will be returned, if
     * the opperators don't make a choice before the chooser reports the choice.
     * many of the lines are commented out, meaning they wont be run as code, but if
     * we want to add them back in, all we have to do is remove the double slashes.
     */
    autoChooser.addOption("Do Nothing", new InstantCommand());
    // autoChooser.setDefaultOption("Drive Forward", new PIDDrive(-1.0, 0, 0.1, 0.2,
    // true));// new DriveForward());
    autoChooser.setDefaultOption("Drive Forward", new DriveForward());
    autoChooser.addOption("Trench Pickup", new TrenchPickup());

    // These are PID autos, and are probably broken because we inverted the drive
    // encoders and gyro
    // autoChooser.addOption("Shoot Drive Forward", new ShootDriveForward());
    // autoChooser.addOption("Shoot Drive No Vision", new ShootDriveForewardRed());
    // autoChooser.addOption("Shoot, Pickup", new ShootandPickup());
    // autoChooser.addOption("Aim and Shoot", new AimAndShoot());
    // autoChooser.addOption("Being Fed Auto", new BeingFed());

    /**
     * this line puts the chooser on the smartdashboard/shuffleboard. It interacts
     * directly with the class, and not with an object. we use the putdata function
     * to add the chooser, and supply that function with the name of the widgit, and
     * the chooser object.
     */
    SmartDashboard.putData("Auto Chooser", autoChooser);
    /**
     * This is how we turn off the LEDs on the limelight, and since there is no need
     * for the drivers to use the camera, set it into processing mode so we don't
     * have to switch when we want to look for the targets.
     */
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
    /**
     * (you can scroll back up to line 160 to see where you just were.) Here is
     * where we connect all of the buttons to the parts of the code that perform
     * specific tasks. We do this throught the JoystickButton class, which takes the
     * button with a given number and makes it into an object that reacts to
     * external input, or basically a fancy wrapper arround the other built in class
     * Trigger. Each button has an index, kind of like a street address, and start
     * at 1. Each button is located on a joysick, and each joystick also has an
     * address, like a street number, that start at 0.
     * 
     * to define a button, we use the JoystickButton class, and give it the
     * controller that the button is located on, and the specific index (number) of
     * the button. for the xbox controller, there is already a mapping of these
     * buttons to what we would call the buttons in english, located in the
     * XboxController.Button class. (the k in the name is programming shorthand for
     * constant). The button objects are assigned to variables that are defined as
     * "final var." We already know what final means but var is new. Var means that
     * the variable will have the type of the object that is being assigned to it.
     * We can only do this because we are in a function, and not defining a variable
     * that will be used by the entire class.
     * 
     * You may also notice that even though we didn't tell the code what operate was
     * inside the function, it still recognises it as the thing defined above (you
     * can controll+click to remember what that was). This is because the function
     * is a part of the class we defined the variable operate in, and so the
     * function has access to everything that is a part of the class. This is
     * different from the variables inside the function, which once the function is
     * finished, will be deleted, and wont be accessable to anything outside the
     * function. for example x wont be accessable to anything outside the curly
     * braces of this function, and will only exist for a short period of time, but
     * this is ok because we only need them once.
     */
    final var x = new JoystickButton(operate, XboxController.Button.kX.value);
    final var y = new JoystickButton(operate, XboxController.Button.kY.value);
    final var a = new JoystickButton(operate, XboxController.Button.kA.value);
    final var b = new JoystickButton(operate, XboxController.Button.kB.value);
    // final var back = new JoystickButton(operate,
    // XboxController.Button.kBack.value);
    // final var start = new JoystickButton(operate,
    // XboxController.Button.kStart.value);
    // final var bumperLeft = new JoystickButton(operate,
    // XboxController.Button.kBumperLeft.value);
    final var bumperRight = new JoystickButton(operate, XboxController.Button.kBumperRight.value);
    final var left6 = new JoystickButton(leftJoystick, 6);
    final var left7 = new JoystickButton(leftJoystick, 7);
    final var right10 = new JoystickButton(rightJoystick, 10);
    final var right11 = new JoystickButton(rightJoystick, 11);
    final var left10 = new JoystickButton(leftJoystick, 10);
    final var right6 = new JoystickButton(rightJoystick, 6);

    /**
     * this is code used to interact with the DPad. DPads return angles instead of
     * button presses, with full up being 0 degrees and full right being 90 degrees.
     * To use it as a serries of buttons, we use the POVButton class to set a
     * trigger on each angle
     */
    final var povUp = new POVButton(operate, 0);
    final var povDown = new POVButton(operate, 180);
    final var povLeft = new POVButton(operate, 270);
    final var povRight = new POVButton(operate, 90);

    /**
     * this is where the buttons are mapped to pieces of the robot code. To do this
     * we create objects out of other commands we have written and feed them into
     * the button object through the method that will run the commands when we want
     * them to run. For example, we only want to shoot when the y button is being
     * held down, and not the rest of the time, so we use the whileHeld method. If
     * you wonder what any of the methods do, you can hover over them for a breif
     * description. In some cases, we didn't feel it necesary to make a new file to
     * contain a command, especially if the actual code was only going to be one
     * line long. in these cases, we just made them in this file, by creating
     * InstantCommands (commands that only run once) and sending them a line of code
     * that would end up in their execute function. An example of this is for the a
     * button below.
     */
    x.toggleWhenPressed(new ShooterRun());
    y.whileHeld(new ShooterShoot());
    a.whenPressed(new InstantCommand(() -> {
      // this line overwrites the variable that indirectly controlls the shooter
      // power, setting it to the low setting.
      shooterSubsystem.savedConsistentArea = 2;
    }));
    b.whenPressed(new InstantCommand(() -> {
      // same as above, but sets the shooter to higher power.
      shooterSubsystem.savedConsistentArea = 0.75;
    }));

    // Colour wheel controls
    // start.whileHeld(
    // new RunCommand(() ->
    // colourWheelThingySubsystem.setDirection(Direction.Right),
    // colourWheelThingySubsystem));
    // back.whileHeld(
    // new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Left),
    // colourWheelThingySubsystem));
    // start.or(back).whenInactive(
    // new RunCommand(() -> colourWheelThingySubsystem.setDirection(Direction.Stop),
    // colourWheelThingySubsystem));
    // bumperLeft.toggleWhenActive(
    // new StartEndCommand(() -> colourWheelThingySubsystem.deploy(), () ->
    // colourWheelThingySubsystem.recall()));

    povUp.whileActiveOnce(new MoveClimber(2));
    povDown.whileActiveOnce(new MoveClimber(1));
    povLeft.whileActiveOnce(new ClimberClimb());
    povRight.whileActiveOnce(new InstantCommand(() -> climberSubsystem.undeploy()));

    // doUndeploy.whenActive(new ClimberUndeploy().andThen(new
    // ClimberWinchCommand()));
    // this is a built in class that allows you to use one button to toggle a
    // function on the robot. the first time you press the
    // button, it runs the first command, the second time, the seccond command. You
    // can switch through any number of commands.
    bumperRight.whenPressed(new CommandSwitch(new IntakeExtend(), new IntakeRetract()));

    left6.or(left7).or(right10).or(right11).whileActiveContinuous(new CameraAim());
    left10.whileActiveOnce(new WindLeft());
    right6.whileActiveOnce(new WindRight());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    /**
     * this is a function that is called by the Robot.java class that gets whatever
     * command the robot operators select on the chooser, to be run during
     * autonomous mode. to get a better idea of how this all works, the next file
     * you should open is Robot.Java,
     */
    return autoChooser.getSelected();
  }
}
