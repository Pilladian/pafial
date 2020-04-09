# Python 3.8

import cv2
import numpy
import random

# Path where to save the images
PATH = ""

# boolean which is true, if left mouse button is pressed
drawing = False
# image file, which will be recognized
input_image = []
shown_image = []

# image variables
size = 500
thickness = 1
amount_squares = 20

# mode
mode = 'd'


# mouse callback function
# Functionality
#	- d = black -> not reachable
# 	- b = red   -> starting point
# 	- d = blue  -> destination point
#	- space for saving the image
def draw(event, xCoordinate, yCoordinate, flags, param):
    global drawing

    if event == cv2.EVENT_LBUTTONDOWN:
        if mode == 'd':
            cv2.circle(shown_image, (xCoordinate, yCoordinate), 5, (0, 0, 0), -1)
            cv2.circle(input_image, (xCoordinate, yCoordinate), 5, (0, 0, 0), -1)
        elif mode == 'b':
            cv2.circle(shown_image, (xCoordinate, yCoordinate), 5, (0, 0, 255), -1)
            cv2.circle(input_image, (xCoordinate, yCoordinate), 5, (0, 0, 255), -1)
        elif mode == 'e':
            cv2.circle(shown_image, (xCoordinate, yCoordinate), 5, (255, 0, 0), -1)
            cv2.circle(input_image, (xCoordinate, yCoordinate), 5, (255, 0, 0), -1)
        drawing = True
    elif event == cv2.EVENT_MOUSEMOVE:
        if drawing:
            cv2.circle(shown_image, (xCoordinate, yCoordinate), 5, (0, 0, 0), -1)
            cv2.circle(input_image, (xCoordinate, yCoordinate), 5, (0, 0, 0), -1)
    elif event == cv2.EVENT_LBUTTONUP:
        drawing = False


def create_image(counter):
    # create image
    mul = size / amount_squares
    for a in range(1, amount_squares + 1):
        xMin = int(a * mul - mul)
        xMax = int(a * mul)

        for b in range(1, amount_squares + 1):
            yMin = int(b * mul - mul)
            yMax = int(b * mul)

            black = False
            blue = False
            red = False
            for x in range(xMin, xMax):
                for y in range(yMin, yMax):
                    if input_image[x][y][0] == 0 and input_image[x][y][2] == 0:
                        black = True
                        break
                    elif input_image[x][y][0] == 255 and input_image[x][y][2] == 0:
                        blue = True
                        break
                    elif input_image[x][y][0] == 0 and input_image[x][y][2] == 255:
                        red = True
                        break
            if black:
                for x in range(xMin, xMax):
                    for y in range(yMin, yMax):
                        input_image[x][y] = [0, 0, 0]
            elif blue:
                for x in range(xMin, xMax):
                    for y in range(yMin, yMax):
                        input_image[x][y] = [255, 0, 0]
            elif red:
                for x in range(xMin, xMax):
                    for y in range(yMin, yMax):
                        input_image[x][y] = [0, 0, 255]

    cv2.imwrite(PATH + "env_" + counter + ".png", input_image)


# create blank_image
def create_grid():
    global shown_image, input_image

    # empty image
    input_image = 255 * numpy.ones(shape=[size, size, 3], dtype=numpy.uint8)
    shown_image = 255 * numpy.ones(shape=[size, size, 3], dtype=numpy.uint8)
    # create 15x15 grid on image
    for x in range(int(size / amount_squares), size, int(size / amount_squares)):
        cv2.line(shown_image, (x, 0), (x, size), (0, 0, 0), thickness)
    for y in range(int(size / amount_squares), size, int(size / amount_squares)):
        cv2.line(shown_image, (0, y), (size, y), (0, 0, 0), thickness)


# initialize blank image and mouse listener
create_grid()
cv2.namedWindow("Pafial - Create Environments")
cv2.setMouseCallback("Pafial - Create Environments", draw)

# counter
count = 2

while True:
    cv2.imshow("Pafial - Create Environments", shown_image)
    key = cv2.waitKey(20) & 0xFF
    if key == ord(' '):
        create_image(str(count))
        create_grid()
        count += 1
    elif key == ord('d'):
        mode = 'd'
    elif key == ord('b'):
        mode = 'b'
    elif key == ord('e'):
        mode = 'e'
    elif key == 27:
        break

cv2.destroyAllWindows()
