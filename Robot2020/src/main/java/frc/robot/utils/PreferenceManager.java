package frc.robot.utils;

import java.util.function.DoubleConsumer;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANPIDController;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.Robot;

/**
 * An alternative to the default preferences manager. The main difference is
 * that the preferences are set to the default value if they are not set.
 */
public final class PreferenceManager {
  public static final void whenChanged(String key, double defaultValue, DoubleConsumer update) {
    var preferences = Preferences.getInstance();

    // Insert the default key if it does not already exist.
    if(!preferences.containsKey(key)) {
      preferences.putDouble(key, defaultValue);
    }

    // Run update when the value is updated.
    var table = NetworkTableInstance.getDefault().getTable("Preferences");
    table.addEntryListener(key,
      (_table, _key, entry, value, flags) -> update.accept(value.getDouble()),
      EntryListenerFlags.kImmediate | EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kLocal);
  }

  public static double getDouble(String key, double defaultValue) {
    Preferences preferences = Preferences.getInstance();

    if (!preferences.containsKey(key)) {
      preferences.putDouble(key, defaultValue);
    }

    return preferences.getDouble(key, defaultValue);
  }

  public static void watchSrxPID(String name, TalonSRX talon, double kP, double kI, double kD) {
    whenChanged(name + " P", kP, (v) -> talon.config_kP(0, v, 0));
    whenChanged(name + " I", kI, (v) -> talon.config_kI(0, v, 0));
    whenChanged(name + " D", kD, (v) -> talon.config_kD(0, v, 0));
  }

  public static void watchSrxPIDF(String name, TalonSRX talon, double kP, double kI, double kD, double kF) {
    watchSrxPID(name, talon, kP, kI, kD);
    whenChanged(name + " F", kF, (v) -> talon.config_kF(0, v, 0));
  }

  public static void watchNeoPID(String name, CANPIDController pid, double kP, double kI, double kD) {
    whenChanged(name + " P", kP, (v) -> pid.setP(v, 0));
    whenChanged(name + " I", kI, (v) -> pid.setI(v, 0));

    // Workaround for error message from SnobotSim
    if(Robot.isReal()) {
      whenChanged(name + " D", kD, (v) -> pid.setD(v, 0));
    }
  }

  public static void watchNeoPIDF(String name, CANPIDController pid, double kP, double kI, double kD, double kF) {
    watchNeoPID(name, pid, kP, kI, kD);
    pid.setFF(getDouble(name + " F", kF), 0);
  }

  public static void watchPIDController(String name, PIDController controller, double kP, double kI, double kD) {
    whenChanged(name + " P", kP, (v) -> controller.setP(v));
    whenChanged(name + " I", kI, (v) -> controller.setI(v));
    whenChanged(name + " D", kD, (v) -> controller.setD(v));
  }
}
