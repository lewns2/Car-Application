#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱에 대한 전진/후진 모듈
"""

import RPi.GPIO as GPIO # import GPIO librery
import time
import R

GPIO.setmode(GPIO.BOARD)

trig=33
echo=31

#ultrasonic sensor setting
GPIO.setup(trig,GPIO.OUT)
GPIO.setup(echo,GPIO.IN)

def getDistance():
    """거리값을 반환함"""
    GPIO.output(trig,False)
    time.sleep(0.00001)
    GPIO.output(trig,True)
    time.sleep(0.00001)
    GPIO.output(trig,False)
    while GPIO.input(echo)==0:
        pulse_start=time.time()
    while GPIO.input(echo)==1:
        pulse_end=time.time()
    pulse_duration=pulse_end-pulse_start
    distance=pulse_duration*17000
    distance=round(distance,2)
    return distance

def huddle() :
    """
    장애물이 있는경우 없을 때까지 회전한 뒤 전진, 회전, 전진을 반복하여 장애물을 피하고 라인으로 돌아가는 역할을 함.
    장애물을 발견한 경우에는 위의 말대로 알고리즘을 따르고, 없다고 판단한 경우에는 알고리즘을 따르지 않고 false 를 발견함.
    """
    chkDis = 20
    distance = getDistance()
    print "Distance : " + str(distance)
    if distance < chkDis :
        print "Stopped"
        R.stop()
        time.sleep(1)

        print "================huddle============"

        turnCount = 0
        while True :
            R.stop()
            if getDistance() > chkDis :
                break
            R.right()
            time.sleep(0.1)
            turnCount += 1


        """
         베터리 없을 떄 : 0.1
         베터리 만땅 : 0.4
        """
        R.forward()
        time.sleep(0.4)


        for i in range(0, turnCount) :
            R.stop()
            getDistance()
            R.left()
            time.sleep(0.1)

        R.forward()
        time.sleep(0.2)

        while True :
            print "FiveSensor",
            sensor = R.FiveSensor.get()
            print sensor
            if sensor == (1,1,1,1,1) :
                R.left()
                time.sleep(0.1)
                R.forward()
                time.sleep(0.1)
            else :
                break

        while True :
            print "FiveSensor",
            sensor = R.FiveSensor.get()
            print sensor
            if R.LineSensor.cmpStatus(sensor, R.LineSensor.forwardCase) :
                R.right()
                time.sleep(0.1)
                R.forward()
                time.sleep(0.1)
            else :
                break

        R.right()
        time.sleep(0.2)
        R.forward()
        time.sleep(0.2)

        R.stop()

        print "================huddle============"
        return True
    return False


