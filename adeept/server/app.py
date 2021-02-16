#coding:utf-8
"""
    @ Author :
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱을 위한 메인 코드
"""

import R
import FiveSensor
import time
import sys
import RPi.GPIO as GPIO
import socket 
import threading





def main() :
    """
    Main 함수
    전체적인 실행을 담당하고 있음
    """
    R.Run.pwm_setup()
    R.LineSensor.init()
    chk = 0
    try:
        while True:
            sensor = FiveSensor.get()
            print("")
            print(sensor)
            if R.huddle() == True :
                continue
            R.LineSensor.chkStatus(sensor)()
            time.sleep(0.05)
            """베터리 만땅 = 0.06"""
            """베터리 없음 = 0.03"""
            # time.sleep(0.03)
            # R.stop()
            chk += 1
    except KeyboardInterrupt:
        GPIO.cleanup()


if __name__ == '__main__':
    main()
