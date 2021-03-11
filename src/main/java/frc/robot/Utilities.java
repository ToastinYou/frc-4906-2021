package frc.robot;

public class Utilities {
	/** Multiply value by a specified scalar. */
    public static double scale(double value, double scalar) {
		return value * scalar;
	}

    /** Treat values within specified deadband as zero. */
    public static double applyDeadband(double value, double deadband) {
        if(Math.abs(value) < deadband) {
			return 0.0;
		}
		
		return value;
    }
}
