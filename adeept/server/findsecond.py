#!/usr/bin/python3
# File name   : findline.py
# Description : line tracking 
# Website     : www.adeept.com
# E-mail      : support@adeept.com
# Author      : William
# Date        : 2019/02/23
import RPi.GPIO as GPIO
import time
import move

'''
status     = 1          #Motor rotation
forward    = 1          #Motor forward
backward   = 0          #Motor backward

left_spd   = num_import_int('E_M1:')         #Speed of the car
right_spd  = num_import_int('E_M2:')         #Speed of the car
left       = num_import_int('E_T1:')         #Motor Left
right      = num_import_int('E_T2:')         #Motor Right
'''
line_pin_right = 19
line_pin_middle = 16
line_pin_left = 20

Tr = 11
Ec = 8

'''
left_R = 15
left_G = 16
left_B = 18

right_R = 19
right_G = 21
right_B = 22

on  = GPIO.LOW
off = GPIO.HIGH

spd_ad_1 = 1
spd_ad_2 = 1
'''

def ultra_value():       #Reading distance
    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(Tr, GPIO.OUT,initial=GPIO.LOW)
    GPIO.setup(Ec, GPIO.IN)
    GPIO.output(Tr, GPIO.HIGH)
    time.sleep(0.000015)
    GPIO.output(Tr, GPIO.LOW)
    while not GPIO.input(Ec):
        pass
    t1 = time.time()
    while GPIO.input(Ec):
        pass
    t2 = time.time()
    return (t2-t1)*17000

def setup():
    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(line_pin_right,GPIO.IN)
    GPIO.setup(line_pin_middle,GPIO.IN)
    GPIO.setup(line_pin_left,GPIO.IN)
    #motor.setup()

turn = 500
parking = 17
forward = 0
left = 0
right = 0
parking = 1

def run():
    global turn,forward,left,right
    status_right = GPIO.input(line_pin_right)
    status_middle = GPIO.input(line_pin_middle)
    status_left = GPIO.input(line_pin_left)
  #  print('R%d   M%d   %d'%(status_right,status_middle,status_left)
    print(ultra_value())
    if parking == 1:
        move.move(50, 'forward', 'no', 1)
        time.sleep(2.4)
        move.move(70, 'no', 'left', 1)
        time.sleep(0.4)
        move.move(50, 'forward', 'no', 1)
        time.sleep(1.5)
        move.move(70, 'no', 'left', 1)
        time.sleep(0.4)
        move.move(50, 'forward', 'no', 1)
        time.sleep(2)
        move.move(0, 'forward', 'no', 1)
        time.sleep(1)
        move.move(50, 'forward', 'no', 1)
        time.sleep(0.5)
        move.move(70, 'no', 'left', 1)
        time.sleep(0.4)
        move.move(50, 'forward', 'no', 1)
        time.sleep(0.5)
        move.move(70, 'no', 'right', 1)
        time.sleep(0.4)
        move.move(0, 'forward', 'no', 1)
        time.sleep(7)
        move.move(70, 'no', 'left', 1)
        time.sleep(0.4)
        move.move(50, 'backward', 'no', 1)
        time.sleep(0.4)
        move.move(70, 'no', 'right', 1)
        time.sleep(0.4)
        move.move(50, 'backward', 'no', 1)
        time.sleep(0.4)
        turn=0
         #   parking=0
    elif status_middle == 1:
        move.move(45, 'forward', 'no', 1)
        forward = 1
        left = 0
        right = 0
    elif status_left == 1:
        move.move(45, 'no', 'left', 1)
        right = 1
        left = 0
        forward = 0
    elif status_right == 1:
        move.move(45, 'no', 'right', 1)
        left = 1
        right = 0
        forward = 0
    else:
        if(forward == 1):
            move.move(40, 'backward', 'no', 1)
        elif(left == 1):
            move.move(45, 'no', 'right', 1)
        else:
            move.move(45, 'no', 'left', 1)
    turn = turn + 1
    time.sleep(0.1)

if __name__ == '__main__':
    try:
        setup()
        move.setup()
        while 1:
         run()
        pass
    except KeyboardInterrupt:
        move.destroy()

