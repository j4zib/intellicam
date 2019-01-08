from google.cloud import storage
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import requests
# Use a service account
cred = credentials.Certificate('intellicam-b8bc8-62957c6eaffa.json')
firebase_admin.initialize_app(cred)

client = storage.Client()
bucket = client.get_bucket('intellicam-b8bc8.appspot.com')
blob = bucket.blob('adfgdffa.jpg')
blob.upload_from_filename('wallpaper_1.jpg')
