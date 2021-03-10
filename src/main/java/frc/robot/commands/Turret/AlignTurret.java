package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Limelight;

import static frc.robot.Constants.kTurret.*;

/**
 * This class is the command responsible for aligning the
 * turret towards the Limelight's target.
 * 
 * Before running this command, drivers should check the
 * Limelight's camera feed to ensure it is locked on to
 * the correct target.
 */
public class AlignTurret extends CommandBase {
    private Turret turret;
    private Limelight limelight;

    public AlignTurret(Turret turret, Limelight limelight) {
        this.turret = turret;
        this.limelight = limelight;
        
        addRequirements(turret, limelight);
    }

    @Override
    public void initialize() { }

    @Override
    public void execute() {
        // Positive speed -> Clockwise motion
        // Negative speed -> Counter-clockwise motion
        double speed = limelight.getXOffset() * TURRET_P;

        // Limit speed to 10% motor speed in either direction.
        // This should be adjusted later to find optimal rotating speed.
        if (speed > 0.30) {
            speed = 0.30;
        }
        else if (speed < -0.30) {
            speed = -0.30;
        }

        turret.setRotatorSpeed(speed);
    }

    @Override
    public boolean isFinished() {
        // Because the Limelight's x offset has some error while targeting,
        // don't wait for it to be exactly zero, but instead within an
        // acceptable error range.
        return Math.abs(limelight.getXOffset()) < SENSITIVITY_DEGREES;
    }

    @Override
    public void end(boolean interrupted) {
        turret.setRotatorSpeed(0);
    }
}