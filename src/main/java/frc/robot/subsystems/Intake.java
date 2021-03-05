package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.Constants.kIntake.*;

public class Intake extends SubsystemBase {
  private CANSparkMax intakeMotor;
  private DoubleSolenoid intakePistons;

  enum State {
    WAITING_FOR_BALL,
    CLEAR_INTAKE,
    INTAKE_KICKBACK,
  };
  State state = State.WAITING_FOR_BALL;
  Timer wait_timer = new Timer();

  public Intake(CANSparkMax intakeMotor, DoubleSolenoid intakePistons) {
    this.intakeMotor = intakeMotor;
    this.intakePistons = intakePistons;
  }

  public static Intake create() {
    CANSparkMax intakeMotor = new CANSparkMax(INTAKE_MOTOR_ID, MotorType.kBrushless);
    intakeMotor.setIdleMode(IdleMode.kCoast);
    intakeMotor.setInverted(true);

    DoubleSolenoid intakePistons = new DoubleSolenoid(INTAKE_RAISE_SOLENOID_CHANNEL, INTAKE_LOWER_SOLENOID_CHANNEL);

    return new Intake(intakeMotor, intakePistons);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void autoIntake() {
    /*
        intake_motor.set(constants.intake_speed);
        intake_solenoid.set(true);
        if ( state == State.WAITING_FOR_BALL ) {
            if ( index_sensor.getRange() < constants.autointake_threshold )
                state = State.CLEAR_INTAKE;
        }
        else if ( state == State.CLEAR_INTAKE ) {
            if ( index_sensor.getRange() < constants.autointake_threshold ) {
                feeder_motor.set(constants.autointake_speed_top);
                for ( TalonSRX motor : indexer_motors ) {
                    motor.set(ControlMode.PercentOutput, constants.autointake_speed);
                }
            }
            else {
                state = State.INTAKE_KICKBACK;
                wait_timer.reset();
                wait_timer.start();
                feeder_motor.set(0);
                for ( TalonSRX motor : indexer_motors ) {
                    motor.set(ControlMode.PercentOutput, 0);
                }
            }
        }
        else if ( state == State.INTAKE_KICKBACK ) {
            feeder_motor.set(constants.autointake_speed_kickback);
            if ( wait_timer.get() > constants.autointake_delay ) {
                feeder_motor.set(0);
                state = State.WAITING_FOR_BALL;
            }
        }*/
  }

  // starts the intake motor
  // it is continuous until stopIntakeMotor is called
  public void runIntake(double speed) {
    //m_intakeStatus.setBoolean(true);
    intakeMotor.set(speed);
  }
 
  public void intake() {
    runIntake(INTAKE_MOTOR_SPEED);
  }
 
  public void eject() {
    runIntake(EJECT_MOTOR_SPEED);
  }
 
  // stops the intake motor
  public void stopIntake() {
    intakeMotor.set(0);
    //m_intakeStatus.setBoolean(false);
  }
 
  // raises the intake mechanism using pistons
  public void raiseIntake() {
    intakePistons.set(INTAKE_RAISE_VALUE);
  }
 
  // lowers the intake mechanism using pistons
  public void lowerIntake() {
    intakePistons.set(INTAKE_LOWER_VALUE);
  }

  public void toggleIntakePistons() {
    if (isIntakeLowered()) {
      raiseIntake();
    }
    else {
      lowerIntake();
    }
  }

  public boolean isIntakeLowered() { return intakePistons.get() == INTAKE_LOWER_VALUE; }
}
