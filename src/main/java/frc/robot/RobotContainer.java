/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.kLimelight;
import frc.robot.commandgroups.AutoFire;
import frc.robot.commands.Drivetrain.*;
import frc.robot.commands.Index.*;
import frc.robot.commands.Shooter.*;
import frc.robot.commands.Turret.*;
import frc.robot.subsystems.*;

import static frc.robot.Controls.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public Air air = Air.create();
  public Drivetrain drivetrain = Drivetrain.create();
  public Gyro gyro = Gyro.create();
  public Index index = Index.create();
  public Intake intake = Intake.create();
  public Limelight limelight = Limelight.create();
  public Shooter shooter = Shooter.create();
  public Turret turret = Turret.create();

  //private Command gtaDrive = new GTADrive(drivetrain);

  // Commonly-Used Instant Commands
  public InstantCommand raiseIntakeCommand = new InstantCommand(intake::raiseIntake, intake);
  public InstantCommand lowerIntakeCommand = new InstantCommand(intake::lowerIntake, intake);
  public InstantCommand intakeCommand = new InstantCommand(intake::intake, intake);
  public InstantCommand intakeKickBackCommand = new InstantCommand(index::timedVerticalDown, index);
  public InstantCommand stopIntakeCommand = new InstantCommand(intake::stopIntake, intake);
  public InstantCommand stopShooterCommand = new InstantCommand(shooter::stop, shooter);
  public InstantCommand stopIndexCommand = new InstantCommand(index::stopIndex, index);

  public SequentialCommandGroup limelightTrackingMode = new InstantCommand(() -> limelight.setLimelightLED(kLimelight.LIMELIGHT_LED.ON), limelight).andThen(() -> limelight.setLimelightMode(kLimelight.LIMELIGHT_MODE.VISION_TRACKING));
  public SequentialCommandGroup limelightDriverMode = new InstantCommand(() -> limelight.setLimelightLED(kLimelight.LIMELIGHT_LED.OFF), limelight).andThen(() -> limelight.setLimelightMode(kLimelight.LIMELIGHT_MODE.DRIVER_CAMERA));

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureButtonBindings();
    setDefaultCommands();
  }

  private void setDefaultCommands() {
    // Inline command example.. https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
    // This will execute a command with drivetrain.arcadeDrive until ended/interrupted, 
    // and then it will call drivetrain.stopMotors.
    // Arcade Drive - Logi
    new RunCommand(() -> drivetrain.arcadeDrive(Logi.getLogiY(), Logi.getLogiZ(), true))
      .andThen(drivetrain::stopMotors, drivetrain);

    // Mecanum Drive - Logi
    //new RunCommand(() -> drivetrain.driveCartesian(Logi.getLogiX(), Logi.getLogiY(), Logi.getLogiZ()))
    //  .andThen(drivetrain::stopMotors, drivetrain);

    // Tank Drive - Xbox
    //new RunCommand(() -> drivetrain.tankDrive(Xbox.getXboxLeftStick(), Xbox.getXboxRightStick(), true))
    //  .andThen(drivetrain::stopMotors, drivetrain);
    
    // Set default limelight LED and mode (off, driver).
    limelight.setLimelightLED(Constants.kLimelight.LIMELIGHT_LED.OFF);
    limelight.setLimelightMode(Constants.kLimelight.LIMELIGHT_MODE.DRIVER_CAMERA);
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Toggle Compressor On/Off
    Xbox.backButton.whenPressed(new InstantCommand(air::toggleCompressorStatus, air));

    // Toggle Limelight LED
    Xbox.bumperLeftButton.whenPressed(new InstantCommand(limelight::toggleLimelightLED, limelight));

    // Toggle Limelight Mode
    Xbox.bumperRightButton.whenPressed(new InstantCommand(limelight::toggleLimelightMode, limelight));

    // Toggle Drive Gear
    Logi.logiButton3.whenPressed(new InstantCommand(drivetrain::toggleDriveGear, drivetrain));

    // Rotate Turret
    turret.setDefaultCommand(new RotateTurret(turret, () -> xboxController.getX(Hand.kRight)));

    Xbox.dpadUpButton.whenPressed(new AutoFire());
    Xbox.dpadRightButton.whenPressed(intakeKickBackCommand);
    Xbox.dpadRightButton.whenReleased(stopIndexCommand);

    Xbox.xButton.whenPressed(new IndexerLoad(index));
    Xbox.xButton.whenReleased(stopIndexCommand);

    // Could convert these into a while held, which calls intake::intake and ends with intake::stopIntake.
    Xbox.yButton.whenPressed(intakeCommand);
    Xbox.yButton.whenReleased(stopIntakeCommand);

    Xbox.bButton.whenPressed(lowerIntakeCommand);
    Xbox.bButton.whenReleased(raiseIntakeCommand);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
      // My old code:
      /* limelightTrackingMode,
      lowerIntakeCommand,
      new AlignTurret(turret, limelight, true),
      new ShootForTime(shooter, 0.62, 2.25),
      new IndexerLoad(index).withTimeout(0.15),
      new IndexerLoad(index).withTimeout(0.27),
      //new IndexerLoad(index).withTimeout(0.4),
      //new InstantCommand(shooter::stop, shooter),
      new DriveForTime(drivetrain, -0.5, 2.75),
      new IndexerLoad(index).alongWith(intakeCommand.withTimeout(0.3)),
      new DriveForTime(drivetrain, -0.5, 3.0),
      new IndexerLoad(index).alongWith(intakeCommand.withTimeout(0.3)),
      new DriveForTime(drivetrain, 0.8, 2.0),
      new AlignTurret(turret, limelight, true),
      //new ShootForTime(shooter, 0.65, 2.25),
      new IndexerLoad(index).withTimeout(0.3),
      new IndexerLoad(index).withTimeout(0.4),
      new WaitCommand(2.0),
      stopShooterCommand */

      limelightTrackingMode,
      new WaitCommand(1.0),
      lowerIntakeCommand,
      new AlignTurret(turret, limelight).withTimeout(1.0),
      new ShootForTime(shooter, 0.62, 2.0),
      new IndexerLoad(index), 
      new WaitCommand(0.15),
      stopIndexCommand,
      new WaitCommand(0.3),
      new IndexerLoad(index),
      new WaitCommand(0.27), 
      stopIndexCommand,
      new WaitCommand(0.3),
      new IndexerLoad(index),
      new WaitCommand(0.3), 
      stopIndexCommand,
      new DriveForTime(drivetrain, -0.5, 2.0),
      new ParallelCommandGroup(
        intakeCommand,
        new IndexerLoad(index),
        new WaitCommand(0.3)),
      stopIndexCommand,
      stopIntakeCommand,
      new DriveForTime(drivetrain, -0.5, 2.0),
      intakeCommand,
      new IndexerLoad(index),
      new WaitCommand(0.3),
      stopIndexCommand,
      stopIntakeCommand,
      new DriveForTime(drivetrain, 0.8, 2.0),
      new AlignTurret(turret, limelight).withTimeout(1.0),
      new IndexerLoad(index),
      new WaitCommand(0.5),
      limelightDriverMode,
      stopIndexCommand,
      new WaitCommand(0.4),
      stopShooterCommand

      // Grenier's new code:
      /* new InstantCommand(() -> limelight.LimeOn()),
      new WaitCommand(1), 
      new IntakeLower(m_intake),
      new AlignTurret(m_turret, limelight, true).withTimeout(1), 
      new ShootForTime(m_shooter, .62, 2),
      // 62% at 3 seconds seems to work
      new IndexerLoad(m_indexer), 
      new WaitCommand(.15),
      new IndexerStop(m_indexer),
      new WaitCommand(.3),
      new IndexerLoad(m_indexer),
      new WaitCommand(.27), 
      new IndexerStop(m_indexer),
      new WaitCommand(.3),
      new IndexerLoad(m_indexer),
      new WaitCommand(.3), 
      new IndexerStop(m_indexer),
      new DriveForTime(drive, -.5, 2),
      new ParallelCommandGroup(
        new IntakeLoader(m_intake),
        new IndexerLoad(m_indexer),
        new WaitCommand(.3)),
      new IndexerStop(m_indexer),
      new IntakeStop(m_intake),
      new DriveForTime(drive, -.5, 2),
      new IntakeLoader(m_intake),
      new IndexerLoad(m_indexer),
      new WaitCommand(.3),
      new IndexerStop(m_indexer),
      new IntakeStop(m_intake),
      new DriveForTime(drive, .8, 2),
      new AlignTurret(m_turret, limelight, true).withTimeout(1),
      new IndexerLoad(m_indexer), 
      new WaitCommand(.5),
      new InstantCommand(() -> limelight.LimeOff()),
      new IndexerStop(m_indexer),
      new WaitCommand(.4),
      new ShooterStop(m_shooter) */
    );
  }
}
