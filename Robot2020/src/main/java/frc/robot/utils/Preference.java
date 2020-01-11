package frc.robot.utils;

import edu.wpi.first.wpilibj.Preferences;

/**
 * An alternative to the default preferences manager. The main difference is
 * that the preferences are set to the default value if they are not set.
 */
public class Preference {
    public static double getDouble(String key, double defaultValue) {
        Preferences preferences = Preferences.getInstance();

        if(!preferences.containsKey(key)) {
            preferences.putDouble(key, defaultValue);
        }

        return preferences.getDouble(key, defaultValue);
    }
}
