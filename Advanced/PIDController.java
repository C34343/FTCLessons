import java.lang.Math;

// WARNING: This code is not tested and may not work as intended

public class PIDController {
    private double kP;
    private double kI;
    private double kD;
    private double setPoint;
    private double errorSum;
    private double lastActual;
    private double lastOutput;
    private boolean isReversed;
    private boolean firstRun;

    private boolean wrapping;
    private double wrapMin;
    private double wrapMax;
    private Boolean isRight;

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.setPoint = 0;
        this.errorSum = 0;
        this.isReversed = false;
        this.firstRun = true;
    }

    public PIDController(double kP) {
        this.kP = kP;
        this.kI = 0;
        this.kD = 0;
        this.setPoint = 0;
        this.errorSum = 0;
        this.isReversed = false;
        this.firstRun = true;
    }

    public void setSetpoint(double setPoint) {
        this.setPoint = setPoint;
    }

    public void setReversed(boolean reversed) {
        this.isReversed = reversed;
    }

    public void setWrapping(double wrapMin, double wrapMax) {
        this.wrapping = true;
        this.wrapMin = wrapMin;
        this.wrapMax = wrapMax;
    }

    public void disableWrapping() {
        this.wrapping = false;
        this.wrapMin = 0;
        this.wrapMax = 0;
    }

    public void setPID(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public double calculate(double actual, double setPoint) {
        double output;
		double Poutput;
		double Ioutput;
		double Doutput;

        this.setPoint = setPoint;

        double error = getError(actual, setPoint);
        

        Poutput=kP*error;

        if(firstRun){
			this.lastActual=actual;
			this.lastOutput=Poutput;
			this.firstRun=false;
		}

        Doutput = -kD * (actual - lastActual);
        lastActual = actual;

        Ioutput = kI * errorSum;

        output = Poutput + Ioutput + Doutput;

        this.errorSum += error;

        this.lastOutput = output;

        if (isRight == null) {
            return isReversed ? -output : output;
        } else {
            if (!isRight) {
                output = -output;
            }
            return isReversed ? -output : output;
        }
    }

    public double calculate(double actual) {
        return calculate(actual, this.setPoint);
    }

    public double getError(double actual, double setPoint) {
        if (wrapping) {
            double total = wrapMax - wrapMin;

            if (actual > wrapMax) {
                actual -= total;
            } else if (actual < wrapMin) {
                actual += total;
            }

            double rightError = setPoint - actual;
            double pos = setPoint - (actual + total);
            double neg = setPoint - (actual - total);
            double leftError = Math.Min(pos, neg);

            if (Math.abs(rightError) < Math.abs(leftError)) {
                isRight = true;
                return rightError;
            } else {
                isRight = false;
                return leftError;
            }
        } else {
            isRight = null;
            return setPoint - actual;
        }
    }

    public void reset() {
        this.errorSum = 0;
        this.firstRun = true;
    }

    public double getP() {
        return this.kP;
    }

    public double getI() {
        return this.kI;
    }

    public double getD() {
        return this.kD;
    }

    public double getSetpoint() {
        return this.setPoint;
    }


}
