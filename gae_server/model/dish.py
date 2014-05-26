from google.appengine.ext import ndb

from model.restaurant import Restaurant
from model.category import Category
from model.image import Image

class Dish(ndb.Model):

  "TODO: add description and image"

  restaurant_key = ndb.KeyProperty(kind = Restaurant)

  category_key = ndb.KeyProperty(kind = Category)

  name = ndb.StringProperty()

  img_key = ndb.KeyProperty(kind = Image)

