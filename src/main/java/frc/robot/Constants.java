/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
	// Controllers
	public static final int XBOX_CONTROLLER = 0;
	public static final int LOGITECH_CONTROLLER = 1;
		
	public static final class kButtons {
		// Xbox 360 Buttons
		public static final class kXboxButtons {
			public static final int XB_BUTTON_A = 1;
			public static final int XB_BUTTON_B = 2;
			public static final int XB_BUTTON_X = 3;
			public static final int XB_BUTTON_Y = 4;
			public static final int XB_BUMPER_LEFT = 5;
			public static final int XB_BUMPER_RIGHT = 6;
			public static final int XB_BACK_BUTTON = 7;
			public static final int XB_START_BUTTON = 8;
			public static final int XB_STICK_BUTTON_LEFT = 9;
			public static final int XB_STICK_BUTTON_RIGHT = 10;
			
			public static final int XB_DPAD_UP = 0;
			public static final int XB_DPAD_RIGHT = 90;
			public static final int XB_DPAD_DOWN = 180;
			public static final int XB_DPAD_LEFT = 270;
			
			public static final int XB_STICK_X_LEFT = 0;
			public static final int XB_STICK_Y_LEFT = 1;
			public static final int XB_STICK_X_RIGHT = 4;
			public static final int XB_STICK_Y_RIGHT = 5;
			
			public static final int XB_TRIGGER_LEFT = 2;
			public static final int XB_TRIGGER_RIGHT = 3;
		}

		// Logitech Extreme 3D Pro Buttons
		public static final class kLogitechButtons {
			public static final int LOGI_BUTTON_TRIGGER = 1;
			public static final int LOGI_BUTTON_2 = 2;
			public static final int LOGI_BUTTON_3 = 3;
			public static final int LOGI_BUTTON_4 = 4;
			public static final int LOGI_BUTTON_5 = 5;
			public static final int LOGI_BUTTON_6 = 6;
			public static final int LOGI_BUTTON_7 = 7;
			public static final int LOGI_BUTTON_8 = 8;
			public static final int LOGI_BUTTON_9 = 9;
			public static final int LOGI_BUTTON_10 = 10;
			public static final int LOGI_BUTTON_11 = 11;
			public static final int LOGI_BUTTON_12 = 12;

			public static final int LOGI_DPAD_UP = 0; 		// TODO: VERIFY THIS MAP
			public static final int LOGI_DPAD_RIGHT = 90; 	// TODO: VERIFY THIS MAP
			public static final int LOGI_DPAD_DOWN = 180; 	// TODO: VERIFY THIS MAP
			public static final int LOGI_DPAD_LEFT = 270; 	// TODO: VERIFY THIS MAP

			public static final int LOGI_STICK_X = 0;
			public static final int LOGI_STICK_Y = 1;
			public static final int LOGI_STICK_Z = 2;

			public static final int LOGI_SLIDER = 3;
		}
	}
	
	public static final class kDrivetrain {
		public static final int MOTOR_LEFT_FRONT_ID = 14;
		public static final int MOTOR_LEFT_REAR_ID = 13;
		public static final int MOTOR_RIGHT_FRONT_ID = 1;
		public static final int MOTOR_RIGHT_REAR_ID = 0;

		public static final double NEUTRAL_DEADBAND = 0.001;

		/**
		 * PID Gains may have to be adjusted based on the responsiveness of control loop.
		 * kF: 1023 represents output value to Talon at 100%, 6800 represents Velocity units at 100% output
		 * Not all set of Gains are used in this project and may be removed as desired.
		 * 
		 * 	                                    			  kP   kI   kD   kF               Iz    PeakOut */
		public static final Gains kGains_Distanc = new Gains( 0.1, 0.0,  0.0, 0.0,            100,  0.50 );
		public static final Gains kGains_Turning = new Gains( 2.0, 0.0,  4.0, 0.0,            200,  1.00 );
		public static final Gains kGains_Velocit = new Gains( 0.1, 0.0, 20.0, 1023.0/6800.0,  300,  0.50 );
		public static final Gains kGains_MotProf = new Gains( 1.0, 0.0,  0.0, 1023.0/6800.0,  400,  1.00 );
		
		/**
		 * Set to zero to skip waiting for confirmation.
		 * Set to nonzero to wait and report to DS if action fails.
		 */
		public static final int kTimeoutMs = 30;
		
		/**
		 * How many sensor units per rotation.
		 * Using Talon FX Integrated Sensor.
		 * @link https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
		 */
		public final static int kSensorUnitsPerRotation = 2048;
		
		/**
		 * Number of rotations to drive when performing Distance Closed Loop
		 */
		public final static double kRotationsToTravel = 6;

		/** ---- Flat constants, you should not need to change these ---- */
		/* We allow either a 0 or 1 when selecting an ordinal for remote devices [You can have up to 2 devices assigned remotely to a talon/victor] */
		public final static int REMOTE_0 = 0;
		public final static int REMOTE_1 = 1;
		/* We allow either a 0 or 1 when selecting a PID Index, where 0 is primary and 1 is auxiliary */
		public final static int PID_PRIMARY = 0;
		public final static int PID_TURN = 1;
		/* Firmware currently supports slots [0, 3] and can be used for either PID Set */
		public final static int SLOT_0 = 0;
		public final static int SLOT_1 = 1;
		public final static int SLOT_2 = 2;
		public final static int SLOT_3 = 3;
		/* ---- Named slots, used to clarify code ---- */
		public final static int kSlot_Distanc = SLOT_0;
		public final static int kSlot_Turning = SLOT_1;
		public final static int kSlot_Velocit = SLOT_2;
		public final static int kSlot_MotProf = SLOT_3;
		
		public static final double TURN_DIVISOR = 2;
		public static final double TURN_RATE = 0.7; // Rate at which the robot turns
		public static final double TURN_DEADZONE = 0.15;

		public static final double DRIVE_DIVISOR = 2;
		public static final double DRIVE_MAX_OUTPUT = 0.6;
		public static final double DRIVE_DEADZONE = 0.15;

		public static final int HIGH_GEAR_SOLENOID_CHANNEL = 2;
		public static final int LOW_GEAR_SOLENOID_CHANNEL = 3;

		public static final Value LOW_GEAR_SOLENOID_VALUE = Value.kReverse;
		public static final Value HIGH_GEAR_SOLENOID_VALUE = Value.kForward;
	}

	public static final class kTurret {
		public static final int MOTOR_ID = 20;

		// Turret Turning
		public static final double RIGHT_MOTOR_SPEED = -0.25; // Turret Right
		public static final double LEFT_MOTOR_SPEED = 0.25; // Turret Left
		public static final Hand ROTATE_TURRET = Hand.kLeft;
		public static final double CONTROL_DEADBAND = 0.20;

		public static final double TURRET_P = 0.1;

		public static final double SENSITIVITY_DEGREES = 0.01;
		public static final double SENSITIVITY_VELOCITY = 0.0;
	}

	public static final class kShooter {
		public static final int RIGHT_MOTOR_ID = 3; // Shooter Right
		public static final int LEFT_MOTOR_ID = 12; // Shooter Left

		public static final double FWD_SPEED = 1.0; // Shooter Forward
		public static final double REV_SPEED = -1.0; // Shoter Reverse
	}

	public static final class kIndex {
		public static final int CONVEYER_HORIZONTAL_MOTOR_ID = 11; // Conveyor Horizontal
		public static final int CONVEYER_VERTICAL_MOTOR_ID = 5; // Conveyor Vertical
		public static final int SHOOTER_INTAKE_MOTOR_ID = 21; // Pre loader Wheels

		// Feed in
		// TODO: Wrong values ?? - same values -->.
		public static final double CONVEYER_FWD_SPEED = -0.6; // Conveyor Forward
		public static final double CONVEYER_REV_SPEED = -0.6; // Conveyor Reverse
		public static final double SHOOTER_LOADER_SPEED_FWD = 1.0; // Shooter PreLoader

		// Feed out
		// TODO: Wrong values ?? - same values -->.
		public static final double INDEX_REV = 0.35; // Conveyor Forward
		public static final double INDEX_FWD = 0.35; // Conveyor Reverse

		// Feed back Speed
		public static final double INDEX_BACK_SPEED = 0.4;
	}

	public static final class kIntake {
		public static final int INTAKE_MOTOR_ID = 4;

		// piston (double solenoid) that lowers and raises the intake
		public static final int INTAKE_LOWER_SOLENOID_CHANNEL = 0;
		public static final int INTAKE_RAISE_SOLENOID_CHANNEL = 1;

		public static final Value INTAKE_LOWER_VALUE = Value.kReverse;
		public static final Value INTAKE_RAISE_VALUE = Value.kForward;

		public static final double INTAKE_MOTOR_SPEED = 0.5;
		public static final double EJECT_MOTOR_SPEED = -0.5;
	}

	public static final class kAir {
		public static final int COMPRESSOR = 0;
	}

	// Time of Flight Sensors
	public static final class kTOF {
		public static final int TOF_LOWER = 0;
		public static final int TOF_UPPER = 1;
		public static final int TOF_HALF_DISTANCE = 80;
	}

	// Limelight
	public static final class kLimelight {
		// TODO: Measurement type?
		public static final double HEIGHT_ABOVE_GROUND = 2.75;
		public static final double TARGET_HEIGHT = 7.5;
		public static final double SHOT_HEIGHT = TARGET_HEIGHT - HEIGHT_ABOVE_GROUND;
	
		public static enum LIMELIGHT_LED {
			DEFAULT,
			OFF,
			BLINK,
			ON
		}
	
		public static enum LIMELIGHT_MODE {
			VISION_TRACKING,
			DRIVER_CAMERA
		}
	}

	public static final class kGyro {
		public static final SPI.Port GYRO_PORT = SPI.Port.kOnboardCS0;
	}
}
