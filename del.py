import cognitive_face as CF


Key = '75de9702425d4736808ff89a57e72f7a'
BASE_URL = 'https://westcentralus.api.cognitive.microsoft.com/face/v1.0'
CF.BaseUrl.set(BASE_URL)
CF.Key.set(Key)

res = CF.person.delete('test1','2c0e5103-c8db-4bd0-93f9-1208df04dde7')
res = CF.person_group.train('test1')
print (res)