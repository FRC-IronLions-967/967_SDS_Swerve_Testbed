package frc.robot.subsystems;

import java.util.List;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Utils.Constants;

public class SubsystemsInstance {
    public Drivetrain drivetrain;
   
    private static SubsystemsInstance inst;

    private SendableChooser<PathPlannerTrajectory> autoChooser = new SendableChooser<>();

    private SubsystemsInstance() {
        drivetrain = new Drivetrain();

        CommandScheduler.getInstance().registerSubsystem(drivetrain);
        
        PathPlannerTrajectory trajDriveAuto1 = PathPlanner.loadPath("DriveAuto1", new PathConstraints(2, 2));
        PathPlannerTrajectory trajDriveAuto2 = PathPlanner.loadPath("DriveAuto2", new PathConstraints(2, 2));
        autoChooser.setDefaultOption("Drive Auto Loading Side", trajDriveAuto1);
        autoChooser.addOption("Drive Auto Cable Cover Side", trajDriveAuto2);
        

    }
    public static SubsystemsInstance getInstance () {
        if(inst == null) inst = new SubsystemsInstance();

        return inst;

    }

    public Command getAutoCommand() {
        return drivetrain.getAutoCommand(autoChooser.getSelected());
    }
}