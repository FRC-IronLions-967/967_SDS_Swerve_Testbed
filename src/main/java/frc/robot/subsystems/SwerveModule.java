// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController.AccelStrategy;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.Utils.Constants;
import frc.robot.Utils.SwerveSteeringEncoder;

public class SwerveModule {

  private CANSparkMax driveMotor;
  private SparkMaxPIDController driveMotorController;
  private CANSparkMax turningMotor;

  private SwerveSteeringEncoder turningEncoder;

  private int i;
  private int iCanId;

  // Gains are for example purposes only - must be determined for your own robot!
  private final PIDController turningPIDController =
       new PIDController(
          0,0,0);


  // Gains are for example purposes only - must be determined for your own robot!
  //private final SimpleMotorFeedforward m_driveFeedforward = new SimpleMotorFeedforward(1, 3);
  //private final SimpleMotorFeedforward m_turnFeedforward = new SimpleMotorFeedforward(1, 0.5);

  /**
   * Constructs a SwerveModule with a drive motor, turning motor, drive encoder and turning encoder.
   *
   * @param driveMotorCANId PWM output for the drive motor.
   * @param turningMotorCANId PWM output for the turning motor.
   * @param turningEncoderCANId DIO input for the turning encoder channel A
   */
  public SwerveModule(
      int driveMotorCANId,
      int turningMotorCANId,
      int turningEncoderCANId) {
    driveMotor = new CANSparkMax(driveMotorCANId, MotorType.kBrushless);
    turningMotor = new CANSparkMax(turningMotorCANId, MotorType.kBrushed);
    turningMotor.setIdleMode(IdleMode.kBrake);
    driveMotor.setIdleMode(IdleMode.kCoast);

    turningPIDController.setTolerance(0.08,0.0);

    //REVPhysicsSim.getInstance().addSparkMax(driveMotor, DCMotor.getNEO(1));
    //REVPhysicsSim.getInstance().addSparkMax(turningMotor, DCMotor.getVex775Pro(1));

    iCanId = turningEncoderCANId;
    turningEncoder = new SwerveSteeringEncoder(turningEncoderCANId);

    /*
     * native units of rpm to m/s
     */
    driveMotor.getEncoder().setVelocityConversionFactor(4.3/5700);
    //driveMotor.getEncoder().setVelocityConversionFactor((2 * Math.PI * kWheelRadius) / (kSecondsPerMinute * kGearRatio));
    //driveMotor.getEncoder().setPositionConversionFactor((2 * Math.PI * kWheelRadius) / (kSecondsPerMinute * kGearRatio));
    driveMotorController = driveMotor.getPIDController();
    driveMotorController.setP(0.05);
    driveMotorController.setI(0.0);
    driveMotorController.setD(0.025);
    driveMotorController.setFF(0.3);

    turningPIDController.reset();
    turningPIDController.enableContinuousInput(-Math.PI, Math.PI);
    turningPIDController.setPID(
      3,
      0,
      0.1);
  }


  /**
   * Returns the current state of the module.
   *
   * @return The current state of the module.
   */
  public SwerveModuleState getState() {
    return new SwerveModuleState(
        driveMotor.getEncoder().getVelocity(), new Rotation2d(turningEncoder.getAbsolutePosition()));
  }

  /**
   * Returns the current position of the module.
   *
   * @return The current position of the module.
   */
  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
        driveMotor.getEncoder().getPosition(), new Rotation2d(turningEncoder.getAbsolutePosition()));
  }

  /**
   * Sets the desired state for the module.
   *
   * @param desiredState Desired state with speed and angle.
   */
  public void setDesiredState(SwerveModuleState desiredState) {
    // Optimize the reference state to avoid spinning further than 90 degrees
    SwerveModuleState state =
        SwerveModuleState.optimize(desiredState, new Rotation2d(turningEncoder.getAbsolutePosition()));
    // Calculate the turning motor output from the turning PID controller.
    final double turnOutput =
      turningPIDController.calculate(turningEncoder.getAbsolutePosition(), MathUtil.angleModulus(state.angle.getRadians()));
      
    // if (i == 0) {
    //   // System.out.println("Measured Angle  " + iCanId + ":   " + driveMotorController.setS(null, i));
    //   System.out.println("Commanded Speed " + iCanId + ":   " + state.speedMetersPerSecond);
    //   System.out.println("Motor Speed     " + iCanId + ": " + driveMotor.getEncoder().getVelocity());
    // }
    // i = (i + 1) % 100;
    // final double turnFeedforward =
    //     m_turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);

    driveMotorController.setReference(state.speedMetersPerSecond, ControlType.kVelocity);
    turningMotor.setVoltage(turnOutput);
  }
}