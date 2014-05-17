from google.appengine.ext import ndb

from model.restaurant import Restaurant

class Category(ndb.Model):

  name = ndb.StringProperty()

  restaurant_key = ndb.KeyProperty(kind = Restaurant)
