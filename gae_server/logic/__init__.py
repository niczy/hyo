
from google.appengine.ext import ndb

from model.restaurant import Restaurant
from logic.exceptions import RestaurantNotExistError
from logic.exceptions import CategoryNotExistError
  
def check_get_restaurant(restaurant_key):
  restaurant = restaurant_key.get() 
  if not restaurant:
    raise RestaurantNotExistError(restaurant_key)
  return restaurant

def check_get_restaurant_by_uid(restaurant_uid):
  restaurant_key = ndb.Key(Restaurant, restaurant_uid)
  return check_get_restaurant(restaurant_key)

def check_get_cateogry(category_key):
  category = category_key.get() 
  if not category:
    raise CategoryNotExistError(category_key)
  return category


