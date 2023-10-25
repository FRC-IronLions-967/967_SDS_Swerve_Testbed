package frc.robot.Utils;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.DoNothingCommand;

public final class Constants {

    public static final double kMaxSpeed = 4.3; // 4.3 meters per second / 14.2 ft per second
    public static final double kMaxAcceleration = 2.15; //2.15 meters per second per second
    public static final double kMaxAngularSpeed = 2 * Math.PI; // 1 rotation per second
    public static final double kMaxAngularAcceleration = Math.PI; // 1/2 roation per second squared
    public static final double kWheelRadius = 0.0406;
    //the number above is acurate
    public static final double kGearRatio = 5.25;
    //needs tunings
    public static final double kSecondsPerMinute = 60;
  
    private static final double kModuleMaxAngularVelocity = kMaxAngularSpeed;
    private static final double kModuleMaxAngularAcceleration =
        2 * Math.PI; // radians per second squared
    public static final Translation2d m_frontLeftLocation = new Translation2d(0.262, -0.298);
    public static final Translation2d m_frontRightLocation = new Translation2d(0.262, 0.298);
    public static final Translation2d m_backLeftLocation = new Translation2d(-0.262, -0.298);
    public static final Translation2d m_backRightLocation = new Translation2d(-0.262, 0.298);
      //real numbers are put in above
    public static final SwerveDriveKinematics m_kinematics =
    new SwerveDriveKinematics(
       m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation);
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
    new TrapezoidProfile.Constraints(
        kMaxAngularSpeed, kMaxAngularAcceleration);

    public static final Map<String, Command> autoEventMap = new HashMap<>();
    static {
      autoEventMap.put("autoEvent1", new DoNothingCommand());
      autoEventMap.put("autoEvent2", new DoNothingCommand());
      autoEventMap.put("autoEvent3", new DoNothingCommand());
    }

    public static final Map<Integer, Double> kSwerveOffsets = new HashMap<>();
    static {
      kSwerveOffsets.put(0, 0.0);
      kSwerveOffsets.put(1, 0.0);
      kSwerveOffsets.put(2, 0.0);
      kSwerveOffsets.put(3, 0.0);
    }
}
