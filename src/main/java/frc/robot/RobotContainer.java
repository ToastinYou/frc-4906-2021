/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.kLimelight;
import frc.robot.commands.Drivetrain.*;
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
  public Index index = Index.create();
  public Intake intake = Intake.create();
  public Limelight limelight = Limelight.create();
  public Shooter shooter = Shooter.create();
  public Turret turret = Turret.create();

  //private Command arcadeDrive = new ArcadeDrive(drivetrain);
  //private Command gtaDrive = new GTADrive(drivetrain);
  //private Command mecanumDrive = new MecanumDrive(drivetrain);
  //private Command tankDrive = new TankDrive(drivetrain);

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
    Logi.logiButton11.whenPressed(new InstantCommand(drivetrain::toggleDriveGear, drivetrain));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
      /*
      new DriveStrightFwd(drive).withTimeout(0.3),
      new WaitCommand(3),
      */
      
      new InstantCommand(() -> limelight.setLimelightLED(kLimelight.LIMELIGHT_LED.ON), limelight).andThen(() -> limelight.setLimelightMode(kLimelight.LIMELIGHT_MODE.VISION_TRACKING)),
      new InstantCommand(intake::lowerIntake, intake),
      new AlignTurret(turret, limelight, true)

      //new IntakeLower(m_intake),
      //new InstantCommand(() -> limelight.setLimelightLED(Constants.LIMELIGHT_LED.ON), limelight).andThen(() -> limelight.setLimelightMode(Constants.LIMELIGHT_MODE.VISION_TRACKING)),
      //new AlignTurret(m_turret, limelight, true),
      //new InstantCommand(() -> limelight.setLimelightLED(Constants.LIMELIGHT_LED.OFF), limelight),

      /*
      new ShootForTime(m_shooter, .62, 2.25),
      // 62% at 2.25 seconds seems to work
      new IndexerLoad(m_indexer), 
      new WaitCommand(.15),
      new IndexerStop(m_indexer),
      new WaitCommand(.4),
      new IndexerLoad(m_indexer),
      new WaitCommand(.27), 
      new IndexerStop(m_indexer),
      new WaitCommand(.4),
      new IndexerStop(m_indexer),
      new DriveForTime(drive, -.5, 2.75),
      new IntakeLoader(m_intake),
      new IndexerLoad(m_indexer),
      new WaitCommand(.3),
      new IndexerStop(m_indexer),
      new IntakeStop(m_intake),
      new DriveForTime(drive, -.5, 3),
      new IntakeLoader(m_intake),
      new IndexerLoad(m_indexer),
      new WaitCommand(.3),
      new IndexerStop(m_indexer),
      new IntakeStop(m_intake),
      new DriveForTime(drive, .8, 2),
      new AlignTurret(m_turret, limelight, true), 
      new IndexerLoad(m_indexer), 
      new WaitCommand(.3),
      new IndexerStop(m_indexer),
      new WaitCommand(.4),
      new IndexerLoad(m_indexer),
      new WaitCommand(.4), 
      new IndexerStop(m_indexer),
      new WaitCommand(2),
      */

      //new ShooterStop(m_shooter)
    );

    // An ExampleCommand will run in autonomous
    //return new ExampleCommand();
  }
}
