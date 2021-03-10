/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

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
