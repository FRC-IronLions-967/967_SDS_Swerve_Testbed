package frc.robot.Utils;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;

import edu.wpi.first.wpilibj.Preferences;

public class SwerveSteeringEncoder {
    private CANCoder encoder;
    private int canId;
    private double zeroOffset;

    public SwerveSteeringEncoder(int encoderCanId) {
        canId = encoderCanId;
        encoder = new CANCoder(canId);

        // Unit are Radians per second and range is -PI to PI
        processErrorCode(encoder.configAbsoluteSensorRange(AbsoluteSensorRange.Signed_PlusMinus180, 3));
        processErrorCode(encoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition, 3));
        processErrorCode(encoder.configFeedbackCoefficient(2 * Math.PI / 4096,"Radians", SensorTimeBase.PerSecond, 3));
        
        if(Preferences.containsKey("TurningEncoder" + canId) && !Constants.overrideSavedSwerveOffsets) {
            //zeroOffset = Preferences.getDouble("TurningEncoder" + canId, Constants.kSwerveOffsets.get(canId));
        } else {
            //zeroOffset = Constants.kSwerveOffsets.get(canId);
            Preferences.initDouble("TurningEncoder" + canId, zeroOffset);
            Preferences.setDouble("TurningEncoder" + canId, zeroOffset);
        }
        
    }

    public double getAbsolutePosition() {
        return encoder.getAbsolutePosition();
    }

    /**
     * 
     */
    public void resetZero() {
        double currentPosition = encoder.getAbsolutePosition();
        zeroOffset = -currentPosition;
        Preferences.setDouble("TurningEncoder" + canId, -currentPosition);
    }

    /**
     * print out error to RIO log
     * @param error
     */
    private void processErrorCode(ErrorCode error){
        if (error != ErrorCode.OK ){
            System.out.println("Encoder Initialization Error:  " + error);
        }
    }
}