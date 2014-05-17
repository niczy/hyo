from google.appengine.ext import ndb

from model.category import Category
from model.restaurant import Restaurant

from logic.exceptions import InvalidInputError
from logic import check_get_restaurant

'''
restaurant_key: The entiy key of a restaurant.
'''
def get_all_by_restaurant_key(restaurant_key):
  check_get_restaurant(restaurant_key)
  return Category.query(Category.restaurant_key == restaurant_key).fetch()

'''
restaurant_key: The entity key of a restaurant.
category_name: The category name.
'''
def add(restaurant, category_name): 
  if isinstance(restaurant, str) or isinstance(restaurant, unicode):
    restaurant = ndb.Key(Restaurant, restaurant)
  check_get_restaurant(restaurant)
  category = Category(restaurant_key = restaurant, name = category_name)
  return category.put().get()
  
