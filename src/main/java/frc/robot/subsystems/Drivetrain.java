package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.kDrivetrain.*;

public class Drivetrain extends SubsystemBase {
  private WPI_TalonFX motorLeftFront, motorLeftRear, motorRightFront, motorRightRear;
  private TalonFXSensorCollection leftEncoder, rightEncoder;

  private DifferentialDrive dDrive;
  private MecanumDrive mDrive;

  private DoubleSolenoid gearSolenoid;

  public Drivetrain(WPI_TalonFX motorLeftFront, WPI_TalonFX motorLeftRear, 
                    WPI_TalonFX motorRightFront, WPI_TalonFX motorRightRear, 
                    TalonFXSensorCollection leftEncoder, TalonFXSensorCollection rightEncoder, 
                    DifferentialDrive dDrive, MecanumDrive mDrive, DoubleSolenoid gearSolenoid) {
    this.motorLeftFront = motorLeftFront;
    this.motorLeftRear = motorLeftRear;
    this.motorRightFront = motorRightFront;
    this.motorRightRear = motorRightRear;
    this.leftEncoder = leftEncoder;
    this.rightEncoder = rightEncoder;
    this.dDrive = dDrive;
    this.mDrive = mDrive;
    this.gearSolenoid = gearSolenoid;
  }

  public static Drivetrain create() {
    WPI_TalonFX motorLeftFront = new WPI_TalonFX(MOTOR_LEFT_FRONT_ID);
    WPI_TalonFX motorLeftRear = new WPI_TalonFX(MOTOR_LEFT_REAR_ID);
    WPI_TalonFX motorRightFront = new WPI_TalonFX(MOTOR_RIGHT_FRONT_ID);
    WPI_TalonFX motorRightRear = new WPI_TalonFX(MOTOR_RIGHT_REAR_ID);

    motorLeftFront.setNeutralMode(NeutralMode.Brake);
    motorLeftRear.setNeutralMode(NeutralMode.Brake);
    motorRightFront.setNeutralMode(NeutralMode.Brake);
    motorRightRear.setNeutralMode(NeutralMode.Brake);

    motorLeftFront.configNeutralDeadband(0.001);
    motorLeftRear.configNeutralDeadband(0.001);
    motorRightFront.configNeutralDeadband(0.001);
    motorRightRear.configNeutralDeadband(0.001);

    TalonFXSensorCollection leftEncoder = new TalonFXSensorCollection(motorLeftFront);
    TalonFXSensorCollection rightEncoder = new TalonFXSensorCollection(motorRightFront);

    SpeedControllerGroup leftController = new SpeedControllerGroup(motorLeftFront, motorLeftRear);
    SpeedControllerGroup rightController = new SpeedControllerGroup(motorRightFront, motorRightRear);
    
    leftController.setInverted(true);
    rightController.setInverted(false);

    DifferentialDrive dDrive = new DifferentialDrive(leftController, rightController);
    dDrive.setMaxOutput(DRIVE_MAX_OUTPUT);

    MecanumDrive mDrive = new MecanumDrive(motorLeftFront, motorLeftRear, motorRightFront, motorRightRear);

    DoubleSolenoid gearSolenoid = new DoubleSolenoid(LOW_GEAR_SOLENOID_CHANNEL, HIGH_GEAR_SOLENOID_CHANNEL);

    return new Drivetrain(motorLeftFront, motorLeftRear, motorRightFront, motorRightRear, leftEncoder, 
                          rightEncoder, dDrive, mDrive, gearSolenoid);
  }

  public double getLeftEncoderPosition() { return leftEncoder.getIntegratedSensorPosition(); }
  public double getLeftEncoderVelocity() { return leftEncoder.getIntegratedSensorVelocity(); }

  public void resetEncoders() {
    leftEncoder.setIntegratedSensorPosition(0, 30);
    rightEncoder.setIntegratedSensorPosition(0, 30);
  }

  /**
   * Arcade drive method for differential drive platform.
   *
   * @param drive The robot's speed along the X axis [-1.0..1.0]. Forward is positive. Divided by DRIVE_DIVISOR.
   * @param steer The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *     positive. Divided by TURN_DIVISOR.
   */
  public void arcadeDrive(double drive, double steer) {
    arcadeDrive(drive, steer, false);
  }

  /**
   * Arcade drive method for differential drive platform.
   *
   * @param drive The robot's speed along the X axis [-1.0..1.0]. Forward is positive. Divided by DRIVE_DIVISOR.
   * @param steer The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *     positive. Divided by TURN_DIVISOR.
   * @param sqInputs If set, decreases the input sensitivity at low speeds.
   */
  public void arcadeDrive(double drive, double steer, boolean sqInputs) {
    dDrive.arcadeDrive(drive / DRIVE_DIVISOR, steer / TURN_DIVISOR, sqInputs);
  }

  /**
   * Tank drive method for differential drive platform.
   *
   * @param leftSpeed The robot left side's speed along the X axis [-1.0..1.0]. Forward is positive. Divided by DRIVE_DIVISOR.
   * @param rightSpeed The robot right side's speed along the X axis [-1.0..1.0]. Forward is
   *     positive. Divided by DRIVE_DIVISOR.
   */
  public void tankDrive(double leftSpeed, double rightSpeed) {
    tankDrive(leftSpeed, rightSpeed, false);
  }

  /**
   * Tank drive method for differential drive platform.
   *
   * @param leftSpeed The robot left side's speed along the X axis [-1.0..1.0]. Forward is positive. Divided by DRIVE_DIVISOR.
   * @param rightSpeed The robot right side's speed along the X axis [-1.0..1.0]. Forward is
   *     positive. Divided by DRIVE_DIVISOR.
   * @param squareInputs If set, decreases the input sensitivity at low speeds.
   */
  public void tankDrive(double leftSpeed, double rightSpeed, boolean sqInputs) {
    dDrive.tankDrive(leftSpeed / DRIVE_DIVISOR, rightSpeed / DRIVE_DIVISOR, sqInputs);
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's speed is independent
   * from its angle or rotation rate.
   *
   * @param ySpeed The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
   * @param xSpeed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *     positive.
   */
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
    driveCartesian(ySpeed, xSpeed, zRotation, 0.0);
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's speed is independent
   * from its angle or rotation rate.
   *
   * @param ySpeed The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
   * @param xSpeed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *     positive.
   * @param gyroAngle The current angle reading from the gyro in degrees around the Z axis. Use this
   *     to implement field-oriented controls.
   */
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
    mDrive.driveCartesian(ySpeed, -xSpeed, zRotation, gyroAngle);
  }

  public void setLeftMotors(double speed) {
    motorLeftFront.set(ControlMode.PercentOutput, speed);
    motorLeftRear.set(ControlMode.PercentOutput, speed);
  }

  public void setRightMotors(double speed) {
    motorRightFront.set(ControlMode.PercentOutput, speed);
    motorRightRear.set(ControlMode.PercentOutput, speed);
  }

  public void stopMotors() {
    dDrive.stopMotor();
    mDrive.stopMotor(); // TODO: will this cause an error if using dDrive?
  }

  public void setDriveHighGear() {
    gearSolenoid.set(HIGH_GEAR_SOLENOID_VALUE);
  }

  public void setDriveLowGear() {
    gearSolenoid.set(LOW_GEAR_SOLENOID_VALUE);
  }

  public boolean isHighGearActive() {
    return gearSolenoid.get() == HIGH_GEAR_SOLENOID_VALUE;
  }

  public void toggleDriveGear() {
    if (isHighGearActive()) {
      setDriveLowGear();
    }
    else {
      setDriveHighGear();
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
