package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.Constants.kTurret.*;

public class Turret extends SubsystemBase {
  private CANSparkMax turretMotor;
  private NetworkTable turretTable;
  private NetworkTableEntry turretStatus;

  public Turret(CANSparkMax turretMotor, NetworkTable turretTable, NetworkTableEntry turretStatus) {
    this.turretMotor = turretMotor;
    this.turretTable = turretTable;
    this.turretStatus = turretStatus;
  }

  public static Turret create() {
    CANSparkMax turretMotor = new CANSparkMax(MOTOR_ID, MotorType.kBrushless);

    turretMotor.restoreFactoryDefaults();
    turretMotor.setIdleMode(IdleMode.kBrake);
    turretMotor.setInverted(true);

    NetworkTable turretTable = NetworkTableInstance.getDefault().getTable("Turret Movement");
    NetworkTableEntry turretStatus = turretTable.getEntry("Turret is Turning");

    return new Turret(turretMotor, turretTable, turretStatus);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void runTurret(double speed) {
    turretMotor.set(speed);
    turretStatus.setBoolean(true);
  }

  public void turretRight() {
    runTurret(RIGHT_MOTOR_SPEED);
  }

  public void turretLeft() {
    runTurret(LEFT_MOTOR_SPEED);
  }

  public void setRotatorSpeed(double speed) {
    turretMotor.set(speed);
  }

  public void stopTurret() {
    turretMotor.stopMotor();
    turretStatus.setBoolean(false);
  }
}
