#coding:utf-8
"""
    @ Author : JANGTAEJIN ( jtjisgod@gmail.com )
    @ Date   : 2017-11-13
    @ Body   : 전체적인 변수 저장
"""

import Run
import Huddle
import Turn
import LineSensor
import FiveSensor

# MostLeft, Left, Middle, Right, MostRight
lineHandle = {
    'ML' : 0,
    'L' : 1,
    'M' : 2,
    'R' : 3,
    'MR' : 4
}

forward = Run.forward
backward = Run.backward
right = Turn.right
left = Turn.left
smallLeft = Turn.smallLeft
smallRight = Turn.smallRight
stop = Run.stop
huddle = Huddle.huddle

speed = 100 # Foward, Backward speed


