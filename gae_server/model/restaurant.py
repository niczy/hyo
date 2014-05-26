from google.appengine.ext import ndb

from image import Image

'''
The restaurant model.
Usually the entity key is the name of the restaurant.
In case the entity key confilicts with existing restaurant, 
the user needs to pick another uid.
'''
class Restaurant(ndb.Model):

  '''
    UID, the same as the entity key. Used to serilized to json response.
  '''
  uid = ndb.StringProperty()

  name = ndb.StringProperty()

  logo = ndb.KeyProperty(kind = Image)
