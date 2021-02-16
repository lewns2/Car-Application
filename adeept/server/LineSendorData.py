#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱에 관련된 데이터 집합 모듈
"""

import R

# LineSensor
class LineSensorData :

    # This Line sensor data set
    # 0 = off = blackLine
    # 1 = on = whiteLine
    # 2 = anything
    # ML, L, M, R, MR
    sensor = (0,0,0,0,0)

    # This is callback function
    callback = None
    print "Sensor Init."

    def __init__(self, sensor, callback = None) :
        self.sensor = sensor
        self.callback = callback

if __name__ == '__main__':
    pass
