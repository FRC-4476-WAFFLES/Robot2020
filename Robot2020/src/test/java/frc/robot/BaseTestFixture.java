package frc.robot;

import frc.robot.Robot;
import frc.robot.sim.WafflesSimulator;
import com.snobot.simulator.DefaultDataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.SnobotLogLevel;

public class BaseTestFixture
{
    private static boolean INITIALIZED;

    protected static Robot mRobot;
    protected static WafflesSimulator mSimulator;

    protected BaseTestFixture()
    {
        if(!INITIALIZED) {
            setupSimulator();
            
            mSimulator = new WafflesSimulator();
            mSimulator.loadConfig("simulator_config/simulator_config.yml");

            mRobot = new Robot();
            mRobot.robotInit();

            mSimulator.setRobot(mRobot);
            mSimulator.enableTeleop();
        }
    }

    private void setupSimulator()
    {
        if (!INITIALIZED)
        {
            DefaultDataAccessorFactory.initalize();
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().setLogLevel(SnobotLogLevel.DEBUG);

            INITIALIZED = true;
        }
        
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().reset();
    }

    protected void simulateForTime(double aSeconds)
    {
        simulateForTime(aSeconds, () -> 
        {
        });
    }

    protected void simulateForTime(double aSeconds, Runnable aTask)
    {
        simulateForTime(aSeconds, .02, aTask);
    }

    protected void simulateForTime(double aSeconds, double aUpdatePeriod, Runnable aTask)
    {
        double updateFrequency = 1 / aUpdatePeriod;

        for (int i = 0; i < updateFrequency * aSeconds; ++i)
        {
            aTask.run();
            DataAccessorFactory.getInstance().getDriverStationAccessor().waitForNextUpdateLoop(aUpdatePeriod);
            mSimulator.update();
            mRobot.robotPeriodic();
            mRobot.teleopPeriodic();
            DataAccessorFactory.getInstance().getSimulatorDataAccessor().updateSimulatorComponents(aUpdatePeriod);
        }
    }

    protected boolean approxZero(double a) {
        return approxEq(a, 0);
    }

    protected boolean approxEq(double a, double b) {
        return approxEq(a, b, 0.01);
    }

    protected boolean approxEq(double a, double b, double tolerence) {
        return Math.abs(a - b) < tolerence;
    }
}
