package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Utilities;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.Constants.kDrivetrain.*;
import static frc.robot.Constants.kButtons.kXboxButtons.*;
import static frc.robot.Controls.*;

// NOTE: This command assumes the DriverController is the Xbox 360 Controller.
public class GTADrive extends CommandBase {
  private Drivetrain drivetrain;
  public GTADrive(Drivetrain drivetrain) {
    this.drivetrain = drivetrain;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // speed/acceleration
    double triggerVal = -Logi.getLogitechRawAxis(XB_TRIGGER_RIGHT) + Logi.getLogitechRawAxis(XB_TRIGGER_LEFT);
    
    // turning
    double stick = Utilities.scale(Logi.getLogitechRawAxis(XB_STICK_X_LEFT), TURN_RATE);
    
    drivetrain.tankDrive((triggerVal + stick), (triggerVal - stick), true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) { drivetrain.stopMotors(); }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() { return false; }
}
