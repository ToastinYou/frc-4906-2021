package frc.robot.commands.Index;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Index;

import static frc.robot.Constants.kIndex.*;

public class IndexerLoad extends CommandBase {
  private Index index;

  public IndexerLoad(Index index) {
    this.index = index;
    addRequirements(index);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    index.runHorizontalMotor(CONVEYER_REV_SPEED);
    index.runVerticalMotor(CONVEYER_FWD_SPEED);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
