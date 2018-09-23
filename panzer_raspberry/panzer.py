import RPi.GPIO as GPIO
import time

def setMotor(pin1, val1, pin2, val2):
    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(pin1, GPIO.OUT)
    GPIO.setup(pin2, GPIO.OUT)
    if val1 == 1:
        GPIO.output(pin1, GPIO.HIGH)
    else:
        GPIO.output(pin1, GPIO.LOW)

    if val2 == 1:
        GPIO.output(pin2, GPIO.HIGH)
    else:
        GPIO.output(pin2, GPIO.LOW)
