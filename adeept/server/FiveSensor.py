#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱에 대한 센서 초기화 및 현재 값 들고오기
"""

import RPi.GPIO as GPIO

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

# 포트설정
leftmostled=16
leftlessled=18
centerled=22
rightlessled=40
rightmostled=32

# 설정
GPIO.setup(leftmostled, GPIO.IN)
GPIO.setup(leftlessled, GPIO.IN)
GPIO.setup(centerled,   GPIO.IN)
GPIO.setup(rightmostled, GPIO.IN)
GPIO.setup(rightlessled, GPIO.IN)

"""
    센서 현재값 리턴
"""
def get() :
    return (GPIO.input(leftmostled), GPIO.input(leftlessled), GPIO.input(centerled), GPIO.input(rightlessled), GPIO.input(rightmostled))

