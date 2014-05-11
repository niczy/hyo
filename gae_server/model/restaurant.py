from google.appengine.ext import ndb

class Restaurant(ndb.Model):

  name = ndb.StringProperty()
