package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.kIndex.*;

public class Index extends SubsystemBase {
  private WPI_VictorSPX verticalMotor, horizontalMotor;
  private CANSparkMax shooterLoaderMotor;
  
  public Index(WPI_VictorSPX verticalMotor, WPI_VictorSPX horizontalMotor, CANSparkMax shooterLoaderMotor) {
    this.verticalMotor = verticalMotor;
    this.horizontalMotor = horizontalMotor;
    this.shooterLoaderMotor = shooterLoaderMotor;
  }

  public static Index create() {
    WPI_VictorSPX horizontalMotor = new WPI_VictorSPX(CONVEYER_HORIZONTAL_MOTOR_ID);
    WPI_VictorSPX verticalMotor = new WPI_VictorSPX(CONVEYER_VERTICAL_MOTOR_ID);
    CANSparkMax shooterLoaderMotor = new CANSparkMax(SHOOTER_INTAKE_MOTOR_ID, MotorType.kBrushless);

    horizontalMotor.setNeutralMode(NeutralMode.Brake);
    verticalMotor.setNeutralMode(NeutralMode.Brake);
    shooterLoaderMotor.setIdleMode(IdleMode.kBrake);

    horizontalMotor.setInverted(false);
    verticalMotor.setInverted(true);
    shooterLoaderMotor.setInverted(true);

    return new Index(verticalMotor, horizontalMotor, shooterLoaderMotor);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void runHorizontalMotor(double speed) {
    horizontalMotor.set(speed);
  }

  public void runVerticalMotor(double speed) {
    verticalMotor.set(speed);
    shooterLoaderMotor.set(speed);
  }

  public void powerCellsIn() {
    runHorizontalMotor(CONVEYER_REV_SPEED);
  }

  public void powerCellsUp() {
    runVerticalMotor(CONVEYER_FWD_SPEED);
  }

  public void powerCellsOut() {
    runHorizontalMotor(CONVEYER_FWD_SPEED);
  }

  public void powerCellsDown() {
    runVerticalMotor(CONVEYER_REV_SPEED);
  }

  public void timedVerticalDown() {
    runVerticalMotor(INDEX_BACK_SPEED);
    Timer.delay(0.05); // TODO: Is this the best way to pause for half a millisecond?
    horizontalMotor.stopMotor();
    verticalMotor.stopMotor();
  }

  public void stopIndex() {
    horizontalMotor.stopMotor();
    verticalMotor.stopMotor();
    shooterLoaderMotor.stopMotor();
  }
}
