package frc.robot.sim;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

public class JoystickSim {
    int aJoystick;
    float[] aAxisValues = new float[6];
    short[] aPovValues = new short[1];
    int aButtonCount = 13;
    int aButtonMask = 0;

    public JoystickSim(int aJoystick) {
        this.aJoystick = aJoystick;
    }

    public void setAxis(int axis, float value) {
        aAxisValues[axis] = value;
        update();
    }

    public void setPov(short direction) {
        aPovValues[0] = direction;
        update();
    }

    public void setButton(int button, boolean pressed) {
        if(pressed) {
            aButtonMask |= 1 << button;
        } else {
            aButtonMask &= ~(1 << button);
        }
        update();
    }

    private void update() {
        DataAccessorFactory.getInstance().getDriverStationAccessor().setJoystickInformation(
            aJoystick, aAxisValues, aPovValues, aButtonCount, aButtonMask);
    }
}