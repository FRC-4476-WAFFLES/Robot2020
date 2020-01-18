/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class CameraSubsystem extends SubsystemBase {
  public static enum CameraLEDMode {
    Default, Off, Strobe, On
  }
  public static enum ProcessingMode {
    Vision, Driver
  }
  enum SnapshotMode {
    SnapOff, SnapOn
  }
  enum Pipeline {
    Close, Far, Search
  }
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  /**
   * Creates a new CameraSubsystem.
   */
  public CameraSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

}
/*
tv:	Whether the limelight has any valid targets (0 or 1)
tx:	Horizontal Offset From Crosshair To Target (LL1: -27 degrees to 27 degrees | LL2: -29.8 to 29.8 degrees)
ty:	Vertical Offset From Crosshair To Target (LL1: -20.5 degrees to 20.5 degrees | LL2: -24.85 to 24.85 degrees)
ta:	Target Area (0% of image to 100% of image)
ts:	Skew or rotation (-90 degrees to 0 degrees)
tl:	The pipeline’s latency contribution (ms) Add at least 11ms for image capture latency.
tshort:	Sidelength of shortest side of the fitted bounding box (pixels)
tlong:	Sidelength of longest side of the fitted bounding box (pixels)
thor:	Horizontal sidelength of the rough bounding box (0 - 320 pixels)
tvert:	Vertical sidelength of the rough bounding box (0 - 320 pixels)
getpipe:	True active pipeline index of the camera (0 .. 9)
camtran:	Results of a 3D position solution, 6 numbers: Translation (x,y,y) Rotation(pitch,yaw,roll)
*/