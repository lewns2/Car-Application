#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱에 대한 전진/후진 모듈
"""

"""
편의를 위해 R.py 만 import 하면 많은 Method 를 쓸 수 있게 구축하였습니다.
RPi.GPIO를 가져오면서 GPIO로 새 이름 지정합니다.
"""
import R
import RPi.GPIO as GPIO
from time import sleep

"""
에러 메시지가 아닌 이상 경고 메시지는 나타나지 않도록 한다
BOARD 모드 BCM 모드 중 BOARD 모드를 선택한다.
좌우측 모터 드라이버와 연결되는 GPIO 핀 6개를 할당한다.
"""
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

MotorLeft_A = 12
MotorLeft_B = 11
MotorLeft_PWM = 35

MotorRight_A = 15
MotorRight_B = 13
MotorRight_PWM = 37


def LeftMotor(val):
    """
    LeftMotor의 val 인자를 이용해서 Left모터를 조절한다.
    val값이 1이면 LeftMotor 전진 0이면 후진한다.
    """
    if val == 0:
        GPIO.output(MotorLeft_A, GPIO.HIGH)
        GPIO.output(MotorLeft_B, GPIO.LOW)
        GPIO.output(MotorLeft_PWM,GPIO.HIGH)
        GPIO.output(MotorRight_PWM,GPIO.HIGH)
    elif val == 1:
        GPIO.output(MotorLeft_A, GPIO.LOW)
        GPIO.output(MotorLeft_B, GPIO.HIGH)
        GPIO.output(MotorLeft_PWM,GPIO.HIGH)
        GPIO.output(MotorRight_PWM,GPIO.HIGH)
    else:
        print("Config Error")

def RightMotor(val):
    """
    RightMotor의 val 인자를 이용해서 Right모터를 조절한다.
    val값이 1이면 RightMotor 전진 0이면 후진한다.
    """
    if val == 0:
        GPIO.output(MotorRight_A, GPIO.LOW)
        GPIO.output(MotorRight_B, GPIO.HIGH)
        GPIO.output(MotorLeft_PWM,GPIO.HIGH)
        GPIO.output(MotorRight_PWM,GPIO.HIGH)
    elif val== 1:
        GPIO.output(MotorRight_A, GPIO.HIGH)
        GPIO.output(MotorRight_B, GPIO.LOW)
        GPIO.output(MotorLeft_PWM,GPIO.HIGH)
        GPIO.output(MotorRight_PWM,GPIO.HIGH)
    else:
        print("Config Error")


GPIO.setup(MotorLeft_A, GPIO.OUT)
GPIO.setup(MotorLeft_B, GPIO.OUT)
GPIO.setup(MotorLeft_PWM, GPIO.OUT)


GPIO.setup(MotorRight_A, GPIO.OUT)
GPIO.setup(MotorRight_B, GPIO.OUT)
GPIO.setup(MotorRight_PWM, GPIO.OUT)


LeftPwm = GPIO.PWM(MotorLeft_PWM, 10)
RightPwm = GPIO.PWM(MotorRight_PWM, 10)


def go_forward_any(speed):
    """LeftMotor, RightMotor를 호출해서 스피드에 따라 전진한다."""
    LeftMotor(1)
    RightMotor(1)
    LeftPwm.ChangeDutyCycle(speed)
    RightPwm.ChangeDutyCycle(speed)


def forward() :
    """출력하고 전진한다"""
    print ("Forward")
    go_forward_any(R.speed)

def backward() :
    """출력하고 후진한다"""
    print ("Backward")
    go_backward_any(R.speed)


def stop():
    """멈춘다"""
    GPIO.output(MotorLeft_PWM, GPIO.LOW)
    GPIO.output(MotorRight_PWM, GPIO.LOW)
    LeftPwm.ChangeDutyCycle(0)
    RightPwm.ChangeDutyCycle(0)


def pwm_setup():
    """초기화"""
    LeftPwm.start(0)
    RightPwm.start(0)

def pwm_low():
    GPIO.output(MotorLeft_PWM, GPIO.LOW)
    GPIO.output(MotorRight_PWM, GPIO.LOW)
    LeftPwm.ChangeDutyCycle(0)
    RightPwm.ChangeDutyCycle(0)
    GPIO.cleanup()


