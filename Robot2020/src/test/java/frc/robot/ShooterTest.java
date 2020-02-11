package frc.robot;

import org.junit.jupiter.api.Test;

import com.snobot.simulator.joysticks.joystick_specializations.XboxButtonMap;

import org.junit.jupiter.api.Assertions;

public class ShooterTest extends BaseTestFixture {
    @Test
    public void testSpinUpSpinDown() throws Exception {
        // Check that the wheel starts stopped
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

    @Test
    public void testShoot() throws Exception {
        // Check that we don't shoot while the shooter is not spun up.
        mSimulator.getJoystick(2).setButton(XboxButtonMap.Y_BUTTON, true);
        simulateForTime(0.1);
        Assertions.assertTrue(
            approxZero(mSimulator.getOutput(Constants.INTAKE_ROLLER)) &&
            approxZero(mSimulator.getOutput(Constants.INDEXER)) &&
            approxZero(mSimulator.getOutput(Constants.CONVEYOR)) &&
            approxZero(mSimulator.getOutput(Constants.SHOOTER_FEEDER_1)),
            "Should not shoot before the button is pressed");
        mSimulator.getJoystick(2).setButton(XboxButtonMap.Y_BUTTON, false);

        // Spin up the shooter motor
        mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, true);
        simulateForTime(0.1);
        mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, false);
        simulateForTime(0.2);

        // Check that we haven't shot yet.
        Assertions.assertTrue(
            approxZero(mSimulator.getOutput(Constants.INTAKE_ROLLER)) &&
            approxZero(mSimulator.getOutput(Constants.INDEXER)) &&
            approxZero(mSimulator.getOutput(Constants.CONVEYOR)) &&
            approxZero(mSimulator.getOutput(Constants.SHOOTER_FEEDER_1)),
            "Should not shoot before the button is pressed");

        // Check that we can shoot.
        mSimulator.getJoystick(2).setButton(XboxButtonMap.Y_BUTTON, true);
        simulateForTime(0.2);

        Assertions.assertTrue(
            !approxZero(mSimulator.getOutput(Constants.INTAKE_ROLLER)) &&
            !approxZero(mSimulator.getOutput(Constants.INDEXER)) &&
            !approxZero(mSimulator.getOutput(Constants.CONVEYOR)) &&
            !approxZero(mSimulator.getOutput(Constants.SHOOTER_FEEDER_1)),
            "Should run the intake and conveyor when shooting");

        // Check that we can stop shooting.
        mSimulator.getJoystick(2).setButton(XboxButtonMap.Y_BUTTON, false);
        simulateForTime(0.1);

        Assertions.assertTrue(
            approxZero(mSimulator.getOutput(Constants.INTAKE_ROLLER)) &&
            approxZero(mSimulator.getOutput(Constants.INDEXER)) &&
            approxZero(mSimulator.getOutput(Constants.CONVEYOR)) &&
            approxZero(mSimulator.getOutput(Constants.SHOOTER_FEEDER_1)),
            "Should not shoot before the button is pressed");

        // Spin down the shooter motor
        mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, true);
        simulateForTime(0.1);
        mSimulator.getJoystick(2).setButton(XboxButtonMap.X_BUTTON, false);
        simulateForTime(0.2);
    }
}