from google.appengine.ext import ndb

class Dish(ndb.Model):

  name = ndb.StringProperty()
