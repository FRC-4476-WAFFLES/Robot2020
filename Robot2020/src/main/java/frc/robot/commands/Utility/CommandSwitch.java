/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Utility;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SelectCommand;

/**
 * Add your docs here.
 */
public class CommandSwitch extends SelectCommand {
    private static class SwitchSupplier implements Supplier<Command> {
        private int i = -1;
        private Command[] list;

        public SwitchSupplier(Command... commands) {
            list = commands;
        }

        @Override
        public Command get() {
            i++;

            if(i >= list.length) {
                i = 0;
            }

            return list[i];
        }
    }

    public CommandSwitch(Command... commands) {
        super(new SwitchSupplier(commands));
    }
}

