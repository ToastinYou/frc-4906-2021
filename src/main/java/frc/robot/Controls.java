package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import static frc.robot.Constants.*;
import frc.robot.Constants.kButtons.kLogitechButtons;
import frc.robot.Constants.kButtons.kXboxButtons;

public class Controls {
  public static XboxController xboxController = new XboxController(XBOX_CONTROLLER);
  public static Joystick logitechController = new Joystick(LOGITECH_CONTROLLER);

  public static class Xbox {
    public static JoystickButton aButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_BUTTON_A);
    public static JoystickButton bButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_BUTTON_B);
    public static JoystickButton xButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_BUTTON_X);
    public static JoystickButton yButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_BUTTON_Y);
    
    public static JoystickButton bumperLeftButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_BUMPER_LEFT);
    public static JoystickButton bumperRightButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_BUMPER_RIGHT);
    
    public static JoystickButton backButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_BACK_BUTTON);
    public static JoystickButton startButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_START_BUTTON);
    
    public static JoystickButton stickLeftButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_STICK_BUTTON_LEFT);
    public static JoystickButton stickRightButton = new JoystickButton(xboxController, kButtons.kXboxButtons.XB_STICK_BUTTON_RIGHT);

    public static double getXboxRawAxis(int axis) { return xboxController.getRawAxis(axis); }
  
    public static double getXboxRawAxis(int axis, double deadzone) {
      double speed = getXboxRawAxis(axis);
  
      if (Math.abs(speed) < deadzone) {
        return 0.0;
      }
  
      return speed;
    }
    
    public static double getXboxLeftStick() { return getXboxRawAxis(kXboxButtons.XB_STICK_Y_LEFT, kDrivetrain.TURN_DEADZONE); }
    public static double getXboxRightStick() { return getXboxRawAxis(kXboxButtons.XB_STICK_Y_RIGHT, kDrivetrain.DRIVE_DEADZONE); }
  }

  public static class Logi {
    public static JoystickButton logiTriggerButton = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_TRIGGER);
    public static JoystickButton logiButton2 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_2);
    public static JoystickButton logiButton3 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_3);
    public static JoystickButton logiButton4 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_4);
    public static JoystickButton logiButton5 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_5);
    public static JoystickButton logiButton6 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_6);
    public static JoystickButton logiButton7 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_7);
    public static JoystickButton logiButton8 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_8);
    public static JoystickButton logiButton9 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_9);
    public static JoystickButton logiButton10 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_10);
    public static JoystickButton logiButton11 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_11);
    public static JoystickButton logiButton12 = new JoystickButton(xboxController, kButtons.kLogitechButtons.LOGI_BUTTON_12);

    public static double getLogitechRawAxis(int axis) { return logitechController.getRawAxis(axis); }
  
    public static double getLogitechRawAxis(int axis, double deadzone) {
      double speed = getLogitechRawAxis(axis);
  
      if (Math.abs(speed) < deadzone) {
        return 0.0;
      }
  
      return speed;
    }

    public static double getLogiX() { return getLogitechRawAxis(kLogitechButtons.LOGI_STICK_X, kDrivetrain.TURN_DEADZONE); }
    public static double getLogiY() { return getLogitechRawAxis(kLogitechButtons.LOGI_STICK_Y, kDrivetrain.DRIVE_DEADZONE); }
    public static double getLogiZ() { return getLogitechRawAxis(kLogitechButtons.LOGI_STICK_Z, kDrivetrain.TURN_DEADZONE); }
  }
}
