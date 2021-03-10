package frc.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.kGyro.*;

public class Gyro extends SubsystemBase {
  private ADXRS450_Gyro gyro;

  //private static final double kAngleSetpoint = 0.0;
	//private static final double kP = 0.005; // propotional turning constant

  public Gyro(ADXRS450_Gyro gyro) {
    this.gyro = gyro;
  }

  public static Gyro create() {
    ADXRS450_Gyro gyro = new ADXRS450_Gyro(GYRO_PORT);
    
    return new Gyro(gyro);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    /* double turningValue = (kAngleSetpoint - gyro.getAngle()) * kP;
		// Invert the direction of the turn if we are going backwards
		turningValue = Math.copySign(turningValue, joystick.getY());
		drivetrain.arcadeDrive(joystick.getY(), turningValue); */
  }

  public void calibrate() {
    gyro.calibrate();
  }

  public void reset() {
    gyro.reset();
  }

  /**
   * Return the heading of the robot in degrees.
   *
   * <p>The angle is continuous, that is it will continue from 360 to 361 degrees. This allows
   * algorithms that wouldn't want to see a discontinuity in the gyro output as it sweeps past from
   * 360 to 0 on the second time around.
   *
   * <p>The angle is expected to increase as the gyro turns clockwise when looked at from the top.
   * It needs to follow the NED axis convention.
   *
   * <p>This heading is based on integration of the returned rate from the gyro.
   *
   * @return the current heading of the robot in degrees.
   */
  public double getAngle() {
    return gyro.getAngle();
  }

  /**
   * Return the rate of rotation of the gyro.
   *
   * <p>The rate is based on the most recent reading of the gyro analog value
   *
   * <p>The rate is expected to be positive as the gyro turns clockwise when looked at from the top.
   * It needs to follow the NED axis convention.
   *
   * @return the current rate in degrees per second
   */
  public double getRate() {
    return gyro.getRate();
  }
}
