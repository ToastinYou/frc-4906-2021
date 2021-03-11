package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.kDrivetrain.*;

public class Drivetrain extends SubsystemBase {
  public boolean positionClosedLoop;

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

    config();
    // Just in case:
    stopMotors();
    resetEncoders();
  }

  public static Drivetrain create() {
    WPI_TalonFX motorLeftFront = new WPI_TalonFX(MOTOR_LEFT_FRONT_ID);
    WPI_TalonFX motorLeftRear = new WPI_TalonFX(MOTOR_LEFT_REAR_ID);
    WPI_TalonFX motorRightFront = new WPI_TalonFX(MOTOR_RIGHT_FRONT_ID);
    WPI_TalonFX motorRightRear = new WPI_TalonFX(MOTOR_RIGHT_REAR_ID);
    DifferentialDrive dDrive = new DifferentialDrive(motorLeftFront, motorRightFront);
    MecanumDrive mDrive = new MecanumDrive(motorLeftFront, motorLeftRear, motorRightFront, motorRightRear);

    TalonFXSensorCollection leftEncoder = new TalonFXSensorCollection(motorLeftFront);
    TalonFXSensorCollection rightEncoder = new TalonFXSensorCollection(motorRightFront);

    DoubleSolenoid gearSolenoid = new DoubleSolenoid(HIGH_GEAR_SOLENOID_CHANNEL, LOW_GEAR_SOLENOID_CHANNEL);

    return new Drivetrain(motorLeftFront, motorLeftRear, motorRightFront, motorRightRear, leftEncoder, 
                          rightEncoder, dDrive, mDrive, gearSolenoid);
  }

  public /* static */ void config() {
    WPI_TalonFX[] motors = { motorLeftFront, motorLeftRear, motorRightFront, motorRightRear };
    TalonFXInvertType leftInvert = TalonFXInvertType.Clockwise; // True
    TalonFXInvertType rightInvert = TalonFXInvertType.CounterClockwise; // False

    for (WPI_TalonFX motor : motors) {
      motor.configFactoryDefault();
      motor.setNeutralMode(NeutralMode.Brake);
      motor.configNeutralDeadband(NEUTRAL_DEADBAND);

      /**
       * Max out the peak output (for all modes).  
       * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
       */
      motor.configPeakOutputForward(1.0);
      motor.configPeakOutputReverse(-1.0);
    }
		
		/* Configure output and sensor direction */
		motorLeftFront.setInverted(leftInvert);
    motorRightFront.setInverted(rightInvert);
    
    motorLeftRear.follow(motorLeftFront);
    motorRightRear.follow(motorRightFront);
        
    dDrive.setMaxOutput(DRIVE_MAX_OUTPUT);

    if (!positionClosedLoop) {
      return;
    }

    TalonFXConfiguration leftConfig = new TalonFXConfiguration();
    TalonFXConfiguration rightConfig = new TalonFXConfiguration();

    /* Configure the left Talon's selected sensor as local Integrated Sensor */
    leftConfig.primaryPID.selectedFeedbackSensor =	TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice();	// Local Feedback Source

    /* Configure the Remote Talon's selected sensor as a remote sensor for the right Talon */
    rightConfig.remoteFilter0.remoteSensorDeviceID = motorLeftFront.getDeviceID(); // Device ID of Source
    rightConfig.remoteFilter0.remoteSensorSource = RemoteSensorSource.TalonFX_SelectedSensor; // Remote Feedback Source

    /* Now that the Left sensor can be used by the master Talon,
		 * set up the Left (Aux) and Right (Master) distance into a single
		 * Robot distance as the Master's Selected Sensor 0. */
    setRobotDistanceConfigs(rightInvert, rightConfig);
    
    /* FPID Gains for distance servo */
		rightConfig.slot0.kP = kGains_Distanc.kP;
		rightConfig.slot0.kI = kGains_Distanc.kI;
		rightConfig.slot0.kD = kGains_Distanc.kD;
		rightConfig.slot0.kF = kGains_Distanc.kF;
		rightConfig.slot0.integralZone = kGains_Distanc.kIzone;
		rightConfig.slot0.closedLoopPeakOutput = kGains_Distanc.kPeakOutput;
    rightConfig.slot0.allowableClosedloopError = 0;
    
    /**
		 * 1ms per loop.  PID loop can be slowed down if need be.
		 * For example,
		 * - if sensor updates are too slow
		 * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		 * - sensor movement is very slow causing the derivative error to be near zero.
		 */
		int closedLoopTimeMs = 1;
		rightConfig.slot0.closedLoopPeriod = closedLoopTimeMs;
		rightConfig.slot1.closedLoopPeriod = closedLoopTimeMs;
		rightConfig.slot2.closedLoopPeriod = closedLoopTimeMs;
		rightConfig.slot3.closedLoopPeriod = closedLoopTimeMs;

		motorLeftFront.configAllSettings(leftConfig);
		motorRightFront.configAllSettings(rightConfig);

    /* Set status frame periods to ensure we don't have stale data */
		motorRightFront.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, kTimeoutMs);
		motorRightFront.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, kTimeoutMs);
		motorRightFront.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, kTimeoutMs);
		motorRightFront.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20, kTimeoutMs);
    motorLeftFront.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, kTimeoutMs);

    motorRightFront.selectProfileSlot(kSlot_Distanc, PID_PRIMARY);
  }

  public void driveUsingSensors(double forward, double turn) {
    /* Calculate targets from gamepad inputs */
    double targetSensorUnits = forward * kSensorUnitsPerRotation * kRotationsToTravel;
    double feedFwdTerm = turn * 0.10;	// Pecent to add to Closed Loop Output
    
    /* Configured for Position Closed Loop on Integrated Sensors' Sum and Arbitrary FeedForward on joyX */
    motorRightFront.set(TalonFXControlMode.Position, targetSensorUnits, DemandType.ArbitraryFeedForward, feedFwdTerm);
    motorLeftFront.follow(motorRightFront);
  }

  public double getLeftEncoderPosition() {
    return leftEncoder.getIntegratedSensorPosition();
  }
  public double getLeftEncoderVelocity() {
    return leftEncoder.getIntegratedSensorVelocity();
  }

  public void resetEncoders() {
    leftEncoder.setIntegratedSensorPosition(0.0, kTimeoutMs);
    rightEncoder.setIntegratedSensorPosition(0.0, kTimeoutMs);
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
  
  /** 
	 * Determines if SensorSum or SensorDiff should be used 
	 * for combining left/right sensors into Robot Distance.  
	 * 
	 * Assumes Aux Position is set as Remote Sensor 0.  
	 * 
	 * configAllSettings must still be called on the master config
	 * after this function modifies the config values. 
	 * 
	 * @param masterInvertType Invert of the Master Talon
	 * @param masterConfig Configuration object to fill
	 */
  private static void setRobotDistanceConfigs(TalonFXInvertType masterInvertType, TalonFXConfiguration masterConfig){
		/**
		 * Determine if we need a Sum or Difference.
		 * 
		 * The auxiliary Talon FX will always be positive
		 * in the forward direction because it's a selected sensor
		 * over the CAN bus.
		 * 
		 * The master's native integrated sensor may not always be positive when forward because
		 * sensor phase is only applied to *Selected Sensors*, not native
		 * sensor sources.  And we need the native to be combined with the 
		 * aux (other side's) distance into a single robot distance.
		 */

		/* THIS FUNCTION should not need to be modified. 
		   This setup will work regardless of whether the master
		   is on the Right or Left side since it only deals with
		   distance magnitude.  */

		/* Check if we're inverted */
		if (masterInvertType == TalonFXInvertType.Clockwise){
			/* 
				If master is inverted, that means the integrated sensor
				will be negative in the forward direction.
				If master is inverted, the final sum/diff result will also be inverted.
				This is how Talon FX corrects the sensor phase when inverting 
				the motor direction.  This inversion applies to the *Selected Sensor*,
				not the native value.
				Will a sensor sum or difference give us a positive total magnitude?
				Remember the Master is one side of your drivetrain distance and 
				Auxiliary is the other side's distance.
					Phase | Term 0   |   Term 1  | Result
				Sum:  -1 *((-)Master + (+)Aux   )| NOT OK, will cancel each other out
				Diff: -1 *((-)Master - (+)Aux   )| OK - This is what we want, magnitude will be correct and positive.
				Diff: -1 *((+)Aux    - (-)Master)| NOT OK, magnitude will be correct but negative
			*/

			masterConfig.diff0Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local Integrated Sensor
			masterConfig.diff1Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice();   //Aux Selected Sensor
			masterConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorDifference.toFeedbackDevice(); //Diff0 - Diff1
		} else {
			/* Master is not inverted, both sides are positive so we can sum them. */
			masterConfig.sum0Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice();    //Aux Selected Sensor
			masterConfig.sum1Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local IntegratedSensor
			masterConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorSum.toFeedbackDevice(); //Sum0 + Sum1
		}

		/* Since the Distance is the sum of the two sides, divide by 2 so the total isn't double
		   the real-world value */
		masterConfig.primaryPID.selectedFeedbackCoefficient = 0.5;
	 }
}
