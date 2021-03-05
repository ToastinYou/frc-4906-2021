package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class ShootForTime extends CommandBase {
  private Shooter shooter;
  private double speed;
  private int counter, target;

  public ShootForTime(Shooter shooter, double speed, double seconds){
    this.shooter = shooter;
    this.speed = speed;

    // Convert time in seconds to robot cycles (50 cycles/s)
    target = (int)(seconds * 50);

    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  @Override
  public void execute(){
    if (counter < target) { counter++; }
    shooter.fire(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) { shooter.stop(); }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() { return counter >= target; }
}