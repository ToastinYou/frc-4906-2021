package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.kAir.*;

public class Air extends SubsystemBase {
  private Compressor compressor;

  public Air(Compressor compressor) {
    this.compressor = compressor;
  }

  public static Air create() {
    Compressor compressor = new Compressor(COMPRESSOR);
    compressor.start();
    return new Air(compressor);
  }

  public void setCompressorStatus(boolean enable) {
    if (enable) {
      compressor.start();
    }
    else {
      compressor.stop();
    }
  }

  public boolean getCompressorStatus() {
    return compressor.enabled();
  }

  public void toggleCompressorStatus() {
    setCompressorStatus(!getCompressorStatus());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
