package frc.robot;

import org.junit.jupiter.api.Test;

import com.snobot.simulator.joysticks.joystick_specializations.XboxButtonMap;

import org.junit.jupiter.api.Assertions;

public class ShooterTest extends BaseTestFixture {
    @Test
    public void testSpinUpSpinDown() throws Exception {
        // Check that the wheel starts stopped
        mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, false);
        simulateForTime(0.2);
        Assertions.assertTrue(
            approxZero(mSimulator.getVelocity(Constants.SHOOTER_MASTER)),
            "Shooter motor should start stopped");

        for(int i=0; i<3; i++) {
            // Check that wheel spins up after toggling
            mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, true);
            simulateForTime(0.1);
            mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, false);
            simulateForTime(0.2);
            Assertions.assertTrue(
                !approxZero(mSimulator.getVelocity(Constants.SHOOTER_MASTER)),
                "Shooter motor should spin up");

            // Check that the wheel stops again
            mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, true);
            simulateForTime(0.1);
            mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, false);
            simulateForTime(0.2);
            Assertions.assertTrue(
                approxZero(mSimulator.getVelocity(Constants.SHOOTER_MASTER)),
                "Shooter motor should spin down");
        }
    }
}