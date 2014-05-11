from model.category import Category
from model.restaurant import Restaurant

from logic.exceptions import RestaurantNotExistError

'''
restaurant_key: The entiy key of a restaurant.
'''
def get_all_by_restaurant_key(restaurant_key):
  check_restaurant_key(restaurant_key)
  return Category.query(ancestor = restaurant_key).fetch()

'''
restaurant_key: The entity key of a restaurant.
category_name: The category name.
'''
def add(restaurant_key, category_name): 
  check_restaurant_key(restaurant_key)
  category = Category(parent = restaurant_key)
  category.put()
  return category

def check_restaurant_key(restaurant_key):
  restaurant = restaurant_key.get() 
  if not restaurant:
    raise RestaurantNotExistError(restaurant_key)

