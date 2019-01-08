import cv2
import sys
import logging as log
import datetime as dt
from time import sleep
import cognitive_face as CF
import sqlite3
import os
import time
import threading
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from google.cloud import storage

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
    doc_ref = db.collection(u'users').document(id)
    doc_ref.set({
    u'name': u'unknown',
    u'spam': 0,
    u'rand': 0,
    u'time': time.time(),
    u'id': str(id)
    })
    client = storage.Client()
    bucket = client.get_bucket('intellicam-b8bc8.appspot.com')
    blob = bucket.blob('%s.jpg'%id)
    blob.upload_from_filename('main.jpg')
def detect():
    time.sleep(1)
    res=CF.face.detect('wallpaper_5.jpg')
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
            add_person()
        else:
            id=f['candidates'][0]['personId']
            doc_ref = db.collection(u'users').document(id)
            docs = doc_ref.get()
            doc_ref.update({
            u'name': docs.to_dict()['name'],
            u'spam': docs.to_dict()['spam'],
            u'time': time.time(),
            u'rand': docs.to_dict()['rand']+1,
            u'id': str(id)
            })

            #do something
            #print("test")
    # res = CF.person.lists('test1')
    # print(res[0])


cred = credentials.Certificate('intellicam-b8bc8-62957c6eaffa.json')
firebase_admin.initialize_app(cred)
db = firestore.client()

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
    frameColor = cv2.cvtColor(frame,cv2.COLOR_BGR2BGRA)

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
            img_item = ('main'+'.jpg')
            cv2.imwrite(img_item,frameColor)
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
