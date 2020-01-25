/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.triggers;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import static frc.robot.RobotContainer.*;

/**
 * Add your docs here.
 */
public class CollidedWithBarTrigger extends Trigger{
    // TODO: make sure the deploy is stowed before climbing
    private final int compressionThreshold = 0;
    @Override
    public boolean get() {
        return !climberSubsystem.isGoingToSetPoint && Math.abs(climberSubsystem.getDeployError()) >= compressionThreshold;
    }
}
