package frc.robot.commandgroups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Index.*;
import frc.robot.commands.Turret.*;
import frc.robot.commands.Shooter.*;

import static frc.robot.Robot.*;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoFire extends SequentialCommandGroup {
  public AutoFire() {
    // Fires 4 Power Cells Consistantly.
    // Drivers need to make sure the Limelight can see some of the target
    addCommands(
      // My old code:
      /* container.raiseIntakeCommand,
      container.intakeKickBackCommand,
      new AlignTurret(container.turret, container.limelight, true),
      new ShootForTime(container.shooter, 0.75, 2.0),
      new InstantCommand(() -> container.index.powerCellsUp()),
      new WaitCommand(0.7),
      new IndexerLoad(container.index),
      new WaitCommand(1.0),
      container.stopShooterCommand,
      container.stopIndexCommand */

      container.limelightTrackingMode,
      new WaitCommand(1.0),
      container.raiseIntakeCommand,
      container.intakeKickBackCommand,
      new AlignTurret(container.turret, container.limelight).withTimeout(1.0),
      new WaitCommand(1.0),
      new ShootForTime(container.shooter, 0.65, 2.5),
      new InstantCommand(() -> container.index.powerCellsUp()),
      new WaitCommand(0.7),
      new IndexerLoad(container.index),
      new WaitCommand(1.5),
      container.stopShooterCommand,
      container.stopIndexCommand,
      container.limelightDriverMode
      
      // Grenier's new code:
      /* new InstantCommand(() -> limelight.LimeOn()),
      new WaitCommand(1),
      new IntakeRaise(intake),
      new IntakeKickBack(indexer),
      new AlignTurret(turret, limelight, true).withTimeout(1),
      new WaitCommand(1),
      new ShootForTime(shooter, .65, 2.5),
      new InstantCommand(() -> indexer.powerCellsIn2()),
      new WaitCommand(.7),
      new IndexerLoad(indexer),
      new WaitCommand(1.5),
      new ShooterStop(shooter),
      new IndexerStop(indexer),
      new InstantCommand(() -> limelight.LimeOff()) */
    );
  }
}
