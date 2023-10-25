package frc.robot.lib;

import edu.wpi.first.wpilibj.AnalogEncoder;
import frc.robot.Utils.Constants;

public class ThriftyEncoder {
    private AnalogEncoder encoder;
    private int port;
    private double zeroOffset;

    public ThriftyEncoder(int rioAnalogPort) {
        port = rioAnalogPort;
        encoder = new AnalogEncoder(port);

        // Unit are Radians per second and range is -PI to PI
        

        zeroOffset = Constants.kSwerveOffsets.get(port);
        encoder.setPositionOffset(zeroOffset);
        
    }

    public double getAbsolutePosition() {
        double steeringPosition = encoder.getAbsolutePosition();
        //The default output is an absolute position between 0 and 1
        //Swerve module code relies on a heading between -Pi and Pi
        steeringPosition = steeringPosition - 0.5;
        steeringPosition = steeringPosition * 2.0 * Math.PI;
        return steeringPosition;
    }

    /**
     * 
     */
    public void resetZero() {
        double currentPosition = encoder.getAbsolutePosition();
        zeroOffset = -currentPosition;
        Constants.kSwerveOffsets.put(port, zeroOffset);
    }


}