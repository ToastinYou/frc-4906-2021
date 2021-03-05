package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.kLimelight.*;

public class Limelight extends SubsystemBase {
  private NetworkTable limelightNetworkTable;
  private NetworkTableEntry limelightLEDEntry, limelightModeEntry;
  private NetworkTableEntry pipeline, xOffset, yOffset;
  private int currentPipeline = 0;

  public Limelight(NetworkTable limelightNetworkTable, NetworkTableEntry limelightLEDEntry, 
                    NetworkTableEntry limelightModeEntry, NetworkTableEntry pipeline, NetworkTableEntry xOffset, 
                    NetworkTableEntry yOffset) {
    this.limelightNetworkTable = limelightNetworkTable;
    this.limelightLEDEntry = limelightLEDEntry;
    this.limelightModeEntry = limelightModeEntry;
    this.pipeline = pipeline;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

  public static Limelight create() {
    NetworkTable limelightNetworkTable = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry limelightLEDEntry = limelightNetworkTable.getEntry("ledMode");
    NetworkTableEntry limelightModeEntry = limelightNetworkTable.getEntry("camMode");
    NetworkTableEntry pipeline = limelightNetworkTable.getEntry("pipeline");
    NetworkTableEntry xOffset = limelightNetworkTable.getEntry("tx");
    NetworkTableEntry yOffset = limelightNetworkTable.getEntry("ty");

    return new Limelight(limelightNetworkTable, limelightLEDEntry, limelightModeEntry, pipeline, xOffset, yOffset);
  }
  
  /**
   * Toggles between two pipelines identical other than one is 2x zoomed.
   */
  public void toggleZoom() {
    if (currentPipeline == 0) {
      currentPipeline = 1;
    }
    else if (currentPipeline == 1) {
      currentPipeline = 0;
    }

    setPipeline(currentPipeline);
  }

  /**
   * Calculates distance to target using techniques from:
   * https://docs.limelightvision.io/en/latest/cs_estimating_distance.html#fixed-angle-camera
   * @return distance to target
   */
  public double getDistanceFromTarget() { return SHOT_HEIGHT / Math.abs(Math.toRadians(getYOffset())); }

  public double getXOffset() { return xOffset.getDouble(0); }
  public double getYOffset() { return yOffset.getDouble(0); }
  public void setPipeline(int id) { pipeline.setNumber(id); }

  public void setLimelightLED(LIMELIGHT_LED led) {
    limelightLEDEntry.setNumber(led.ordinal());
  }

  public void setLimelightMode(LIMELIGHT_MODE mode) {
    limelightModeEntry.setNumber(mode.ordinal());
  }

  public void toggleLimelightLED() {
    Number current = limelightLEDEntry.getNumber(LIMELIGHT_LED.ON.ordinal());

    if (current.intValue() == LIMELIGHT_LED.ON.ordinal() || 
        current.intValue() == LIMELIGHT_LED.BLINK.ordinal() ||
        current.intValue() == LIMELIGHT_LED.DEFAULT.ordinal()) {
      // Limelight is on in some form, turn it off.
      setLimelightLED(LIMELIGHT_LED.OFF);
    }
    else {
      // Limelight is off, turn it on.
      setLimelightLED(LIMELIGHT_LED.ON);
    }
  }

  public void toggleLimelightMode() {
    Number current = limelightModeEntry.getNumber(LIMELIGHT_MODE.VISION_TRACKING.ordinal());

    if (current.intValue() == LIMELIGHT_MODE.VISION_TRACKING.ordinal()) {
      setLimelightMode(LIMELIGHT_MODE.DRIVER_CAMERA);
    }
    else {
      setLimelightMode(LIMELIGHT_MODE.VISION_TRACKING);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
