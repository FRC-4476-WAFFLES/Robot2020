package frc.robot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class DriveTest extends BaseTestFixture {
    @Test
    public void testTeleopDrive() throws Exception {
        // Stop both sides
        mSimulator.getJoystick(0).setAxis(1, 0);
        mSimulator.getJoystick(1).setAxis(1, 0);

        simulateForTime(0.1);

        Assertions.assertEquals(0, mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1), "Left drive did not stop");
        Assertions.assertEquals(0, mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1), "Right drive did not stop");

        // Move forward left
        mSimulator.getJoystick(0).setAxis(1, 1);
        mSimulator.getJoystick(1).setAxis(1, 0);

        simulateForTime(0.1);

        Assertions.assertNotEquals(0, mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1), "Left drive did not go");
        Assertions.assertEquals(0, mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1), "Right drive went when it should not have");

        // Move forward right
        mSimulator.getJoystick(0).setAxis(1, 0);
        mSimulator.getJoystick(1).setAxis(1, 1);

        simulateForTime(0.1);

        Assertions.assertEquals(0, mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1), "Left drive went when it should not have");
        Assertions.assertNotEquals(0, mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1), "Right drive did not go");

        // Move forward both
        mSimulator.getJoystick(0).setAxis(1, 1);
        mSimulator.getJoystick(1).setAxis(1, 1);

        simulateForTime(0.1);

        Assertions.assertNotEquals(0, mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1), "Left drive did not go");
        Assertions.assertNotEquals(0, mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1), "Right drive did not go");

        // Move forward left, back right
        mSimulator.getJoystick(0).setAxis(1, 1);
        mSimulator.getJoystick(1).setAxis(1, -1);

        simulateForTime(0.1);

        Assertions.assertEquals(mSimulator.getTalonOutput(Constants.DRIVE_LEFT_1), mSimulator.getTalonOutput(Constants.DRIVE_RIGHT_1), "Turn was not equal forwards and back");
    }
}