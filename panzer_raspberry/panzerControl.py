import sys
from panzer import setMotor

def forward():
    print('FORWARD')
    setMotor(16, 1, 18, 0)

def backward():
    print('BACKWARD')
    setMotor(16, 0, 18, 1)

def stop():
    print('STOP')
    setMotor(16, 0, 18, 0)

def left():
    print('LEFT')
    setMotor(22, 0, 24, 1)

def right():
    print('RIGHT')
    setMotor(22, 1, 24, 0)

def middle():
    print('MIDDLE')
    setMotor(22, 0, 24, 0)

def main(command):

    print(command)

    if command == "forward":
        forward()
    elif command == "backward":
        backward()
    elif command == "stop":
        stop()
    elif command == "left":
        left()
    elif command == "right":
        right()
    elif command == "middle":
        middle()
    else:
        print('NOTHING HAPPENED')

if __name__ == "__main__":
    main(sys.argv[1])
