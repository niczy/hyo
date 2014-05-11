from model.category import Category
from model.dish import Dish

from logic.exceptions import CategoryNotExistError

def get_all_by_category_key(category_key):
  check_category_key(category_key)
  return Dish.query(ancestor = category_key).fetch()

def add(category_key, name):
  check_category_key(category_key)
  dish = Dish(parent = category_key, name = name)
  dish.put()
  return dish

def check_category_key(category_key):
  category = category_key.get() 
  if not category:
    raise CategoryNotExistError(category_key)

