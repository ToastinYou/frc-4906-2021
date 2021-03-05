package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.playingwithfusion.*;
import com.playingwithfusion.TimeOfFlight.RangingMode;

import static frc.robot.Constants.kTOF.*;

public class TOF extends SubsystemBase {
  private TimeOfFlight tofLower, tofUpper;

  public TOF(TimeOfFlight tofLower, TimeOfFlight tofUpper) {
    this.tofLower = tofLower;
    this.tofUpper = tofUpper;
  }

  public static TOF create() {
    TimeOfFlight tofLower = new TimeOfFlight(TOF_LOWER);
    TimeOfFlight tofUpper = new TimeOfFlight(TOF_UPPER);

    // Sample time must be between 24 and 1000 milliseconds.
    tofLower.setRangingMode(RangingMode.Short, 24);

    return new TOF(tofLower, tofUpper);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /** Get the lower TOF's range in millimeters. */
  public double getLowerRange() { return tofLower.getRange(); }
  
  /** Get the upper TOF's range in millimeters. */
  public double getUpperRange() { return tofUpper.getRange(); }
}
