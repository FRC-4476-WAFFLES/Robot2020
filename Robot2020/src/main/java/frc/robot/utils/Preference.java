package frc.robot.utils;

import edu.wpi.first.wpilibj.Preferences;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;

/**
 * An alternative to the default preferences manager. The main difference is
 * that the preferences are set to the default value if they are not set.
 */
public class Preference {
  public static double getDouble(String key, double defaultValue) {
    Preferences preferences = Preferences.getInstance();

    if (!preferences.containsKey(key)) {
      preferences.putDouble(key, defaultValue);
    }

    return preferences.getDouble(key, defaultValue);
  }

  public static void UpdateSRXPIDPreferences(String name, TalonSRX talon, double kP, double kI, double kD) {
    talon.config_kP(0, getDouble(name + " P", kP), 0);
    talon.config_kI(0, getDouble(name + " I", kI), 0);
    talon.config_kD(0, getDouble(name + " D", kD), 0);

  }

  public static void UpdateSRXPIDPreferences(String name, TalonSRX talon, double kP, double kI, double kD, double kF) {
    UpdateSRXPIDPreferences(name, talon, kP, kI, kD);
    talon.config_kF(0, getDouble(name + " F", kF), 0);
  }

  public static void UpdateNEOPIDPreferences(String name, CANPIDController pid, double kP, double kI, double kD) {
    pid.setP(getDouble(name + " P", kP), 0);
    pid.setI(getDouble(name + " I", kI), 0);
    pid.setD(getDouble(name + " D", kD), 0);
  }

  public static void UpdateNEOPIDPreferences(String name, CANPIDController pid, double kP, double kI, double kD, double kF) {
    UpdateNEOPIDPreferences(name, pid, kP, kI, kD);
    pid.setFF(getDouble(name + " F", kF), 0);
  }
}
