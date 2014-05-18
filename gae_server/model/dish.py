from google.appengine.ext import ndb

from model.restaurant import Restaurant
from model.category import Category

class Dish(ndb.Model):

  "TODO: add description and image"

  restaurant_key = ndb.KeyProperty(kind = Restaurant)

  category_key = ndb.KeyProperty(kind = Category)

  name = ndb.StringProperty()
