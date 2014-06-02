from google.appengine.ext import ndb


from logic import check_get_cateogry 
from logic import check_get_restaurant
from model.category import Category
from model.dish import Dish
from model.restaurant import Restaurant
from exceptions import DishNotExistError


def get_all_by_category_key(category_key):
  check_get_cateogry(category_key)
  return Dish.query(Dish.category_key == category_key).fetch()

def get_all_by_restaurant_uid(restaurant_uid):
  restaurant_key = ndb.Key(Restaurant, restaurant_uid)
  check_get_restaurant(restaurant_key)
  return Dish.query(Dish.restaurant_key == restaurant_key).fetch()

def add(restaurant_uid, category_name, dish_name, image_key = None):
  categories = Category.query(ndb.AND(
      Category.restaurant_key == ndb.Key(Restaurant, restaurant_uid),
      Category.name == category_name)).fetch()
  "TODO: check if category exist. Throw exception if so."
  return add_by_category_key(categories[0].key, dish_name, image_key)

def delete_by_id(dish_id):
  dish = Dish.get_by_id(dish_id)
  delete_by_key(dish.key)

def delete_by_key(dish_key):
  dish = dish_key.get()
  if not dish:
    raise DishNotExistError()
  dish_key.delete()
  return dish

def add_by_category_key(category_key, name, image_key = None):
  category = check_get_cateogry(category_key)
  dish = Dish(restaurant_key = category.restaurant_key, category_key = category_key, name = name, img_key = image_key)
  dish.put()
  return dish
