package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.kShooter.*;

public class Shooter extends SubsystemBase {
  private WPI_TalonFX leftMotor, rightMotor;

  public Shooter(WPI_TalonFX leftMotor, WPI_TalonFX rightMotor) {
    this.leftMotor = leftMotor;
    this.rightMotor = rightMotor;
  }

  public static Shooter create() {
    WPI_TalonFX leftMotor = new WPI_TalonFX(LEFT_MOTOR_ID);
    WPI_TalonFX rightMotor = new WPI_TalonFX(RIGHT_MOTOR_ID);

    leftMotor.configFactoryDefault();
    rightMotor.configFactoryDefault();

    leftMotor.setNeutralMode(NeutralMode.Coast);
    rightMotor.setNeutralMode(NeutralMode.Coast);

    leftMotor.setInverted(true);
    rightMotor.setInverted(false);

    leftMotor.configClosedloopRamp(.2, 250);
    rightMotor.configClosedloopRamp(.2, 250);

    leftMotor.follow(rightMotor);

    return new Shooter(leftMotor, rightMotor);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void runShooter(double speed) {
    rightMotor.set(speed);
  }

  public void fire(double speed) {
    runShooter(speed);
  }

  public void reverse() {
    runShooter(REV_SPEED);
  }

  public void stop() {
    rightMotor.stopMotor();
  }
}
