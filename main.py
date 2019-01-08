import cv2
import sys
import logging as log
import datetime as dt
from time import sleep
import cognitive_face as CF
import sqlite3
from urllib.request import pathname2url
import os
import time
import threading

lastRun=0

def detect():
    res=CF.face.detect('wallpaper_0.jpg')
    print (res)

cascPath = "haarcascade_frontalface_alt2.xml"
faceCascade = cv2.CascadeClassifier(cascPath)
log.basicConfig(filename='webcam.log',level=log.INFO)
Key = '75de9702425d4736808ff89a57e72f7a'
BASE_URL = 'https://westcentralus.api.cognitive.microsoft.com/face/v1.0'
CF.BaseUrl.set(BASE_URL)
CF.Key.set(Key)
video_capture = cv2.VideoCapture(0)
anterior = 0

i=0
while True:
    if not video_capture.isOpened():
        print('Unable to load camera.')
        sleep(5)
        pass

    # Capture frame-by-frame
    ret, frame = video_capture.read()

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    faces = faceCascade.detectMultiScale(
        gray,
        scaleFactor=1.1,
        minNeighbors=5,
        minSize=(60, 60)
    )

    # Draw a rectangle around the faces
    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
        roi_gray = gray[y:y+h,x:x+w]
        img_item = ('wallpaper_'+str(i)+'.jpg')
        cv2.imwrite(img_item,roi_gray)

    if(time.time()-lastRun>30):
        print('detect running')
        #detectThread = threading.Thread(target=detect)
        #detectThread.start()
        lastRun=time.time() 
    # Display the resulting frame
    cv2.imshow('Video', frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# When everything is done, release the capture
video_capture.release()
cv2.destroyAllWindows()