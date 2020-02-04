package frc.robot.sim;

import com.snobot.simulator.ASimulator;
import com.snobot.simulator.robot_container.IRobotClassContainer;
import com.snobot.simulator.robot_container.JavaRobotContainer;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;

import frc.robot.Robot;

/**
 * When you have custom factories, you need to tell SnobotSim about them. This class,
 * upon construction, tells the library to use your overrides instead of the default.
 * <p>
 * Note: This class must be called out in the configuration file, {@code simulator_config/simulator_config.properties}
 */
public class WafflesSimulator extends ASimulator
{
    private Robot mRobot;
    private JoystickSim[] joysticks;

    /**
     * Constructor.
     */
    public WafflesSimulator()
    {
        joysticks = new JoystickSim[] { new JoystickSim(0), new JoystickSim(1), new JoystickSim(2) };
    }

    /**
     * Returns the output voltage of the given talon/victor
     */
    public double getTalonOutput(int aPort) {
        return DataAccessorFactory.getInstance().getSpeedControllerAccessor().getVoltagePercentage(aPort + 100);
    }

    /**
     * Returns the joystick bound to the given port
     */
    public JoystickSim getJoystick(int aJoystick) {
        return joysticks[aJoystick];
    }
    
    @Override
    public void setRobot(IRobotClassContainer aRobot)
    {
        setRobot((Robot) ((JavaRobotContainer) aRobot).getJavaRobot());
    }

    public void setRobot(Robot aRobot)
    {
        mRobot = aRobot;
    }


    @Override
    public void update()
    {
    }
}
