package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Index.*;
import frc.robot.commands.Turret.*;
import frc.robot.commands.Shooter.*;
import frc.robot.subsystems.*;

import static frc.robot.Robot.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoFire extends SequentialCommandGroup {
  public AutoFire(Shooter shooter, Index index, Intake intake, Turret turret, Limelight limelight) {
    // Fires 4 Power Cells Consistantly.
    // Drivers need to make sure the Limelight can see some of the target
    addCommands(
      container.raiseIntakeCommand,
      container.intakeKickBackCommand,
      new AlignTurret(turret, limelight, true),
      new ShootForTime(shooter, 0.75, 2.0),
      new InstantCommand(() -> index.powerCellsUp()),
      new WaitCommand(0.7),
      new IndexerLoad(index),
      new WaitCommand(1.0),
      container.stopShooterCommand,
      container.stopIndexCommand
    );
  }
}
