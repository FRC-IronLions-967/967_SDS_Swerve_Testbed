package frc.robot.subsystems;

import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPlannerTrajectory;
import com.pathplanner.lib.path.PathPoint;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Utils.Constants;

public class SubsystemsInstance {
    public Drivetrain drivetrain;
   
    private static SubsystemsInstance inst;

    private final SendableChooser<Command> autoChooser;

    private SubsystemsInstance() {
        drivetrain = new Drivetrain();
        autoChooser = AutoBuilder.buildAutoChooser("Default_Auto");

        CommandScheduler.getInstance().registerSubsystem(drivetrain);
        
        
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }
    public static SubsystemsInstance getInstance () {
        if(inst == null) inst = new SubsystemsInstance();

        return inst;

    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}