import cognitive_face as CF


Key = '75de9702425d4736808ff89a57e72f7a'
BASE_URL = 'https://westcentralus.api.cognitive.microsoft.com/face/v1.0'
CF.BaseUrl.set(BASE_URL)
CF.Key.set(Key)

res = CF.person.delete('test1','ed982e5a-0213-4128-be13-e2af6940fa15')
res = CF.person_group.train('test1')
print (res)
