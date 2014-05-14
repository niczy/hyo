from google.appengine.ext import ndb

'''
The restaurant model.
Usually the entity key is the name of the restaurant.
In case the entity key confilicts with existing restaurant, 
the user needs to pick another uid.
'''
class Restaurant(ndb.Model):

  name = ndb.StringProperty()
