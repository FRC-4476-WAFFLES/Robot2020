package frc.robot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class DriveTest extends BaseTestFixture {
    @Test
    public void testTeleopDrive() throws Exception {
        mSimulator.enableTeleop();

        // Stop both sides
        mSimulator.getJoystick(0).setAxis(1, 0);
        mSimulator.getJoystick(1).setAxis(1, 0);

        simulateForTime(0.1);

        Assertions.assertTrue(
            approxZero(mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1)) &&
            approxZero(mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1)),
            "Drive should start stopped");

        // Move forward left
        mSimulator.getJoystick(0).setAxis(1, 1);
        mSimulator.getJoystick(1).setAxis(1, 0);

        simulateForTime(0.1);

        Assertions.assertTrue(
            !approxZero(mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1)) &&
            approxZero(mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1)),
            "Left drive should move");

        // Move forward right
        mSimulator.getJoystick(0).setAxis(1, 0);
        mSimulator.getJoystick(1).setAxis(1, 1);

        simulateForTime(0.1);

        Assertions.assertTrue(
            approxZero(mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1)) &&
            !approxZero(mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1)),
            "Right drive should move");

        // Move forward both
        mSimulator.getJoystick(0).setAxis(1, 1);
        mSimulator.getJoystick(1).setAxis(1, 1);

        simulateForTime(0.1);

        Assertions.assertTrue(
            !approxZero(mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1)) &&
            !approxZero(mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1)),
            "Both drives should move");

        // Move forward left, back right
        mSimulator.getJoystick(0).setAxis(1, 1);
        mSimulator.getJoystick(1).setAxis(1, -1);

        simulateForTime(0.1);

        Assertions.assertTrue(
            approxZero(mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1) - mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1)),
            "Drive should turn");
    }
}