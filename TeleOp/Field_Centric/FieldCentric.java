package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

//Tells the robot that it's in teleop, and the name that will show up on the drivers hub
@TeleOp(name="FieldCentric", group="FTCLessons")
// Makes the program not show up on the drivers hub, comment this out if you want to use the code
@Disabled
public class FieldCentric extends OpMode {
    private static DcMotor leftFront;
    private static DcMotor leftBack;
    private static DcMotor rightFront;
    private static DcMotor rightBack;

    public void caclulateDrive(double forward, double slide, double turn, double heading) {
        double Temp = forward * Math.cos(heading) + slide * Math.sin(heading);
        slide = (-forward * Math.sin(heading)) + slide * Math.cos(heading);
        forward = Temp;

        double leftFrontPower = forward + slide + turn;
        double leftBackPower = forward - slide + turn;
        double rightFrontPower = forward - slide - turn;
        double rightBackPower = forward + slide - turn;

        double biggestInput = Math.max(Math.abs(rightFrontPower), Math.abs(leftFrontPower));
        biggestInput = Math.max(biggestInput, Math.abs(leftBackPower));
        biggestInput = Math.max(biggestInput, Math.abs(rightBackPower));

        if (biggestInput > 1) {
            leftFrontPower = leftFrontPower / biggestInput;
            leftBackPower = leftBackPower / biggestInput;
            rightFrontPower = rightFrontPower / biggestInput;
            rightBackPower = rightBackPower / biggestInput;
        }

        return [leftFrontPower, leftBackPower, rightFrontPower, rightBackPower]

    }
  
    // performs once when you hit the init button on the drivers hub
    @Override
    public void init() {
        // Finds the hardware who's configured name matches the name in the string
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        // Displays on the drivers hub when it finishes initialization
        telemetry.addData("status", "Initialized");
    }

    // called constantly while the program is running
    @Override
    public void loop() {
        double forwards = -gamepad1.left_stick_y;
        double horizontal = gamepad1.left_stick_x;
        double turning = gamepad1.right_stick_x;

        double leftFrontPower = forwards + horizontal + turning;
        double leftBackPower = forwards - horizontal + turning;
        double rightFrontPower = forwards - horizontal - turning;
        double rightBackPower = forwards + horizontal - turning;

        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);

        telemetry.addData("Motors", "leftFrontWheel:" + leftFront.getPower());
        telemetry.addData("Motors", "leftBackWheel:" + leftBack.getPower());
        telemetry.addData("Motors", "rightFrontWheel:" + rightFront.getPower());
        telemetry.addData("Motors", "rightBackWheel:" + rightBack.getPower());
        telemetry.addData("Input", "left y:" + gamepad1.left_stick_y +
                ", left x:" + gamepad1.left_stick_x +
                ", right x:" + gamepad1.right_stick_x);
    }
}
