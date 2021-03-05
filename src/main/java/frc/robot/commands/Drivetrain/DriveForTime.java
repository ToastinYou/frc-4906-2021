package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

/**
 * This command allows the drivetrain to run at a certain
 * percent output for a certain time.
 * 
 * This is only used in autonomous.
 */
public class DriveForTime extends CommandBase {
  private Drivetrain drivetrain;
  private double speed;
  private int counter, target;

  public DriveForTime(Drivetrain drivetrain, double speed, double seconds) {
    this.drivetrain = drivetrain;
    this.speed = speed;

    // Convert time in seconds to robot cycles (50 cycles/s)
    target = (int)(seconds * 50);

    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (counter < target) { counter++; }
    drivetrain.arcadeDrive(speed, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
