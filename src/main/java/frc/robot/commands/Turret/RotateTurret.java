package frc.robot.commands.Turret;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Utilities;
import frc.robot.subsystems.Turret;

import static frc.robot.Constants.kTurret.*;

public class RotateTurret extends CommandBase {
  private Turret turret;
  private DoubleSupplier input;

  public RotateTurret(Turret turret, DoubleSupplier input) {
    this.turret = turret;
    this.input = input;
    addRequirements(turret);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // divide by two to allow for much finer turret control
    double speed = Utilities.applyDeadband(input.getAsDouble(), CONTROL_DEADBAND) / 2;
    turret.runTurret(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stopTurret();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
