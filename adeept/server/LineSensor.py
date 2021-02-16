Skip to content
Search or jump to…

Pull requests
Issues
Marketplace
Explore
 
@lewns2 
Code Issues 0 Pull requests 0 Projects 0 Wiki Security Pulse Community
RaspberryPi-Car-Line-Trace/LineSensor.py
@tmp2147483647 tmp2147483647 Modify docstring
12da8e1 on 27 Nov 2017
@tmp2147483647@jtjisgod
Executable File  113 lines (91 sloc)  2.56 KB
  
#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱에 관련된 데이터 집합 모듈
"""

import R
from LineSensorData import LineSensorData
sensorData = []
lastSensor = (2,2,2,2,2)

# 전진할 값
forwardCase = (
    (1,0,2,0,1),
    (0,0,2,0,0),
    (0,1,2,1,0),
    (1,1,0,1,1)
)

# 좌회전 값
leftCase = (
    (0,1,1,1,1),
    (1,0,1,1,1),
    # (1,0,0,1,1),
    # (0,0,1,1,1),
);

# 왼쪽 미세조종 값
smallLeftCase = (
    # (0,0,1,1,1),
    # (1,0,1,1,1),
    # (1,0,0,1,1),
);


def init() :
    """
    ineSensor 초기화
    기본적인 값을 초기화하여 case에 대한 callback 함수를 함께 LineSensorData 에 담아 sensordata 에 추가함
    """

    print("Init!")
    # Insert test case like under!

    # Foward
    for fc in forwardCase :
        sensorData.append(LineSensorData(fc, R.forward))

    for lc in leftCase :
        sensorData.append(LineSensorData(lc, R.left))
        sensorData.append(LineSensorData(lc[-1::-1], R.right))

    for slc in smallLeftCase :
        sensorData.append(LineSensorData(slc, R.smallLeft))
        sensorData.append(LineSensorData(slc[-1::-1], R.smallRight))


    #elif LineSensorData[0] + [LineSensorData[1] < LineSensorData[3] + LineSensorData[4]:

def chkStatus(sensor) :
    """센서값을 받아 검색 한 뒤 올바른 callback 함수를 반환해줌"""
    global lastSensor

    if sensor == (1,1,1,1,1) :
        # return R.forward
        print "Called Last Sensor"
        sensor = lastSensor;

    lastSensor = sensor
    cbFunc = R.forward

    for data in sensorData :
        score = 0
        for i in range(0, len(data.sensor)) :
            if data.sensor[i] == 2:
                score += 1
                continue
            if sensor[i] == data.sensor[i] :
                score += 1
                continue
        if score == 5 :
            cbFunc = data.callback
            break
    return cbFunc

def cmpStatus(sensor, sensorData) :
    """센서값이 센서값의 모음들에 들어가는지 체크하여 반환"""
    cbFunc = R.forward
    for data in sensorData :
        score = 0
        for i in range(0, len(data)) :
            if data[i] == 2:
                score += 1
                continue
            if sensor[i] == data[i] :
                score += 1
                continue
        if score == 5 :
            return True
            break
    return False



def emptyFunc() :
    """빈 함수, 전진 함수를 반환함"""
    print "EMpty"
    return R.forward

if __name__ == '__main__':
    pass
© 2019 GitHub, Inc.
Terms
Privacy
Security
Status
Help
Contact GitHub
Pricing
API
Training
Blog
About
Skip to content
Search or jump to…

Pull requests
Issues
Marketplace
Explore
 
@lewns2 
Code Issues 0 Pull requests 0 Projects 0 Wiki Security Pulse Community
RaspberryPi-Car-Line-Trace/LineSensor.py
@tmp2147483647 tmp2147483647 Modify docstring
12da8e1 on 27 Nov 2017
@tmp2147483647@jtjisgod
Executable File  113 lines (91 sloc)  2.56 KB
  
#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱에 관련된 데이터 집합 모듈
"""

import R
from LineSensorData import LineSensorData
sensorData = []
lastSensor = (2,2,2,2,2)

# 전진할 값
forwardCase = (
    (1,0,2,0,1),
    (0,0,2,0,0),
    (0,1,2,1,0),
    (1,1,0,1,1)
)

# 좌회전 값
leftCase = (
    (0,1,1,1,1),
    (1,0,1,1,1),
    # (1,0,0,1,1),
    # (0,0,1,1,1),
);

# 왼쪽 미세조종 값
smallLeftCase = (
    # (0,0,1,1,1),
    # (1,0,1,1,1),
    # (1,0,0,1,1),
);


def init() :
    """
    ineSensor 초기화
    기본적인 값을 초기화하여 case에 대한 callback 함수를 함께 LineSensorData 에 담아 sensordata 에 추가함
    """

    print("Init!")
    # Insert test case like under!

    # Foward
    for fc in forwardCase :
        sensorData.append(LineSensorData(fc, R.forward))

    for lc in leftCase :
        sensorData.append(LineSensorData(lc, R.left))
        sensorData.append(LineSensorData(lc[-1::-1], R.right))

    for slc in smallLeftCase :
        sensorData.append(LineSensorData(slc, R.smallLeft))
        sensorData.append(LineSensorData(slc[-1::-1], R.smallRight))


    #elif LineSensorData[0] + [LineSensorData[1] < LineSensorData[3] + LineSensorData[4]:

def chkStatus(sensor) :
    """센서값을 받아 검색 한 뒤 올바른 callback 함수를 반환해줌"""
    global lastSensor

    if sensor == (1,1,1,1,1) :
        # return R.forward
        print "Called Last Sensor"
        sensor = lastSensor;

    lastSensor = sensor
    cbFunc = R.forward

    for data in sensorData :
        score = 0
        for i in range(0, len(data.sensor)) :
            if data.sensor[i] == 2:
                score += 1
                continue
            if sensor[i] == data.sensor[i] :
                score += 1
                continue
        if score == 5 :
            cbFunc = data.callback
            break
    return cbFunc

def cmpStatus(sensor, sensorData) :
    """센서값이 센서값의 모음들에 들어가는지 체크하여 반환"""
    cbFunc = R.forward
    for data in sensorData :
        score = 0
        for i in range(0, len(data)) :
            if data[i] == 2:
                score += 1
                continue
            if sensor[i] == data[i] :
                score += 1
                continue
        if score == 5 :
            return True
            break
    return False



def emptyFunc() :
    """빈 함수, 전진 함수를 반환함"""
    print "EMpty"
    return R.forward

if __name__ == '__main__':
    pass
© 2019 GitHub, Inc.
Terms
Privacy
Security
Status
Help
Contact GitHub
Pricing
API
Training
Blog
About
Skip to content
Search or jump to…

Pull requests
Issues
Marketplace
Explore
 
@lewns2 
Code Issues 0 Pull requests 0 Projects 0 Wiki Security Pulse Community
RaspberryPi-Car-Line-Trace/LineSensor.py
@tmp2147483647 tmp2147483647 Modify docstring
12da8e1 on 27 Nov 2017
@tmp2147483647@jtjisgod
Executable File  113 lines (91 sloc)  2.56 KB
  
#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱에 관련된 데이터 집합 모듈
"""

import R
from LineSensorData import LineSensorData
sensorData = []
lastSensor = (2,2,2,2,2)

# 전진할 값
forwardCase = (
    (1,0,2,0,1),
    (0,0,2,0,0),
    (0,1,2,1,0),
    (1,1,0,1,1)
)

# 좌회전 값
leftCase = (
    (0,1,1,1,1),
    (1,0,1,1,1),
    # (1,0,0,1,1),
    # (0,0,1,1,1),
);

# 왼쪽 미세조종 값
smallLeftCase = (
    # (0,0,1,1,1),
    # (1,0,1,1,1),
    # (1,0,0,1,1),
);


def init() :
    """
    ineSensor 초기화
    기본적인 값을 초기화하여 case에 대한 callback 함수를 함께 LineSensorData 에 담아 sensordata 에 추가함
    """

    print("Init!")
    # Insert test case like under!

    # Foward
    for fc in forwardCase :
        sensorData.append(LineSensorData(fc, R.forward))

    for lc in leftCase :
        sensorData.append(LineSensorData(lc, R.left))
        sensorData.append(LineSensorData(lc[-1::-1], R.right))

    for slc in smallLeftCase :
        sensorData.append(LineSensorData(slc, R.smallLeft))
        sensorData.append(LineSensorData(slc[-1::-1], R.smallRight))


    #elif LineSensorData[0] + [LineSensorData[1] < LineSensorData[3] + LineSensorData[4]:

def chkStatus(sensor) :
    """센서값을 받아 검색 한 뒤 올바른 callback 함수를 반환해줌"""
    global lastSensor

    if sensor == (1,1,1,1,1) :
        # return R.forward
        print "Called Last Sensor"
        sensor = lastSensor;

    lastSensor = sensor
    cbFunc = R.forward

    for data in sensorData :
        score = 0
        for i in range(0, len(data.sensor)) :
            if data.sensor[i] == 2:
                score += 1
                continue
            if sensor[i] == data.sensor[i] :
                score += 1
                continue
        if score == 5 :
            cbFunc = data.callback
            break
    return cbFunc

def cmpStatus(sensor, sensorData) :
    """센서값이 센서값의 모음들에 들어가는지 체크하여 반환"""
    cbFunc = R.forward
    for data in sensorData :
        score = 0
        for i in range(0, len(data)) :
            if data[i] == 2:
                score += 1
                continue
            if sensor[i] == data[i] :
                score += 1
                continue
        if score == 5 :
            return True
            break
    return False



def emptyFunc() :
    """빈 함수, 전진 함수를 반환함"""
    print "EMpty"
    return R.forward

if __name__ == '__main__':
    pass
© 2019 GitHub, Inc.
Terms
Privacy
Security
Status
Help
Contact GitHub
Pricing
API
Training
Blog
About
Skip to content
Search or jump to…

Pull requests
Issues
Marketplace
Explore
 
@lewns2 
Code Issues 0 Pull requests 0 Projects 0 Wiki Security Pulse Community
RaspberryPi-Car-Line-Trace/LineSensor.py
@tmp2147483647 tmp2147483647 Modify docstring
12da8e1 on 27 Nov 2017
@tmp2147483647@jtjisgod
Executable File  113 lines (91 sloc)  2.56 KB
  
#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 라인트레이싱에 관련된 데이터 집합 모듈
"""

import R
from LineSensorData import LineSensorData
sensorData = []
lastSensor = (2,2,2,2,2)

# 전진할 값
forwardCase = (
    (1,0,2,0,1),
    (0,0,2,0,0),
    (0,1,2,1,0),
    (1,1,0,1,1)
)

# 좌회전 값
leftCase = (
    (0,1,1,1,1),
    (1,0,1,1,1),
    # (1,0,0,1,1),
    # (0,0,1,1,1),
);

# 왼쪽 미세조종 값
smallLeftCase = (
    # (0,0,1,1,1),
    # (1,0,1,1,1),
    # (1,0,0,1,1),
);


def init() :
    """
    ineSensor 초기화
    기본적인 값을 초기화하여 case에 대한 callback 함수를 함께 LineSensorData 에 담아 sensordata 에 추가함
    """

    print("Init!")
    # Insert test case like under!

    # Foward
    for fc in forwardCase :
        sensorData.append(LineSensorData(fc, R.forward))

    for lc in leftCase :
        sensorData.append(LineSensorData(lc, R.left))
        sensorData.append(LineSensorData(lc[-1::-1], R.right))

    for slc in smallLeftCase :
        sensorData.append(LineSensorData(slc, R.smallLeft))
        sensorData.append(LineSensorData(slc[-1::-1], R.smallRight))


    #elif LineSensorData[0] + [LineSensorData[1] < LineSensorData[3] + LineSensorData[4]:

def chkStatus(sensor) :
    """센서값을 받아 검색 한 뒤 올바른 callback 함수를 반환해줌"""
    global lastSensor

    if sensor == (1,1,1,1,1) :
        # return R.forward
        print "Called Last Sensor"
        sensor = lastSensor;

    lastSensor = sensor
    cbFunc = R.forward

    for data in sensorData :
        score = 0
        for i in range(0, len(data.sensor)) :
            if data.sensor[i] == 2:
                score += 1
                continue
            if sensor[i] == data.sensor[i] :
                score += 1
                continue
        if score == 5 :
            cbFunc = data.callback
            break
    return cbFunc

def cmpStatus(sensor, sensorData) :
    """센서값이 센서값의 모음들에 들어가는지 체크하여 반환"""
    cbFunc = R.forward
    for data in sensorData :
        score = 0
        for i in range(0, len(data)) :
            if data[i] == 2:
                score += 1
                continue
            if sensor[i] == data[i] :
                score += 1
                continue
        if score == 5 :
            return True
            break
    return False



def emptyFunc() :
    """빈 함수, 전진 함수를 반환함"""
    print "EMpty"
    return R.forward

if __name__ == '__main__':
    pass
© 2019 GitHub, Inc.
Terms
Privacy
Security
Status
Help
Contact GitHub
Pricing
API
Training
Blog
About

