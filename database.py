import sqlite3

connect = sqlite3.connect('security.db')
# connect.execute('''CREATE TABLE SECURITY
#          (ID TEXT PRIMARY KEY     NOT NULL,
#          NAME           TEXT    NOT NULL,
#          SPAM            INT     NOT NULL
#          );''')
id='c93795a5-cfe4-42d7-b96b-088e380e964e'
r = connect.execute('DELETE FROM SECURITY WHERE ID = ? ',(id,))
#for row in r:
#    print (row)
connect.commit()
print("Database created sucessfully")
connect.close()