from google.appengine.ext import ndb


from logic import check_get_cateogry 
from logic import check_get_restaurant
from model.category import Category
from model.dish import Dish
from model.restaurant import Restaurant


def get_all_by_category_key(category_key):
  check_get_cateogry(category_key)
  return Dish.query(Dish.category_key == category_key).fetch()

def get_all_by_restaurant_uid(restaurant_uid):
  restaurant_key = ndb.Key(Restaurant, restaurant_uid)
  check_get_restaurant(restaurant_key)
  return Dish.query(Dish.restaurant_key == restaurant_key).fetch()

def add(restaurant_uid, category_name, dish_name):
  categories = Category.query(ndb.AND(
      Category.restaurant_key == ndb.Key(Restaurant, restaurant_uid),
      Category.name == category_name)).fetch()
  return add_by_category_key(categories[0].key, dish_name)

def add_by_category_key(category_key, name):
  category = check_get_cateogry(category_key)
  dish = Dish(restaurant_key = category.restaurant_key, category_key = category_key, name = name)
  dish.put()
  return dish
