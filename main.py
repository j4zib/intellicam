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
import sqlite3

lastRun=0

def add_person():
    res = CF.person.create('test1','unknown')
    id = res['personId']
    for i in range(0,10):
        try:
            res = CF.person.add_face('wallpaper_'+str(i)+'.jpg','test1',id)
            print(res)
        except CF.CognitiveFaceException:
            continue
    res = CF.person_group.train('test1')
    print(res)
    conn = sqlite3.connect('security.db')
    conn.execute(f"INSERT INTO SECURITY (ID,NAME,SPAM) \
      VALUES (?, 'unknown', 0);",(id,))
    conn.commit()
    conn.close()
    #res = CF.person.add_face
    #print(res)

def detect():
    res=CF.face.detect('wallpaper_0.jpg')
    print (res)
    id = []
    if(len(res)!=1):
        print("Not detected")
        return
    for f in res:
        id.append(f['faceId'])
    res = CF.face.identify(id, 'test1')
    print(res)
    for f in res:
        if not f['candidates']:
            add_person();
            #print("test")
    # res = CF.person.lists('test1')
    # print(res[0])

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
dontRun=False
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
        minSize=(30, 30)
    )

    # Draw a rectangle around the faces
    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
        roi_gray = gray[y:y+h,x:x+w]
        if(dontRun==False):
            img_item = ('wallpaper_'+str(i)+'.jpg')
            cv2.imwrite(img_item,roi_gray)
    if(dontRun==False):
        i=i+1
        if(i>10):
            dontRun=True
            i=0
    if(time.time()-lastRun>30):
        dontRun=False
        print('detect running')
        detectThread = threading.Thread(target=detect)
        detectThread.start()
        lastRun=time.time() 
    # Display the resulting frame
    cv2.imshow('Video', frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# When everything is done, release the capture
video_capture.release()
cv2.destroyAllWindows()