import webapp2
import json
import logging

from google.appengine.ext import ndb
from logic import category_logic
from logic import dish_logic
from logic import restaurant_logic
from model.image import Image
from util import json_encoder

'''
 Request parameter keys.
'''
RESTAURANT_UID = 'uid'
CATEGORY_NAME = 'category_name'
NAME = 'name'
RESTAURANT_IMAGE = 'restaurant_image'
IMAGE_DATA = 'image_data'

class BaseApiHandler(webapp2.RequestHandler):

  def send_response(self, resp):
    self.response.headers['Content-Type'] = 'application/json'
    if isinstance(resp, str):
      self.response.out.write(resp)
      return
    elif isinstance(resp, dict):
      self.response.out.write(json.dumps(resp))
      return

    self.response.out.write(json_encoder.encode(resp))


class Restaurant(BaseApiHandler):

  '''
    Creates a new restaurant.
    args:
      uid: The restaurant uid.
      name: The restaurant name.
  '''
  def post(self):
    restaurant_name = self.request.get(NAME)
    restaurant_uid = self.request.get(RESTAURANT_UID)
    restaurant_image_data = self.request.get(IMAGE_DATA)
    image = Image.from_image_data(restaurant_image_data) 
    image.put()
    restaurant = restaurant_logic.add(restaurant_uid, restaurant_name, image.key)
    self.send_response(restaurant)

  def delete(self, restaurant_uid):
    logging.info("restaurant uid is %s" % restaurant_uid)
    restaurant_logic.delete(restaurant_uid)
    self.send_response({"status": "ok"})

class CheckRestaurantUid(BaseApiHandler):

  '''
    Checks if given UID is avaliable.
    args:
      uid: The uid to be checked.
  '''
  def get(self):
    restaurant_uid = self.request.get(RESTAURANT_UID)
    result = {"exist": restaurant_logic.check_uid_exist(restaurant_uid)}
    self.send_response(result)

class Category(BaseApiHandler):

  '''
    Creates a new category in the given restaurant.
    args:
      uid: The uid of a restaurant.
      name: Name of the category.
  '''
  def post(self):
    restaurant_uid = self.request.get(RESTAURANT_UID)
    category_name = self.request.get(NAME)
    category = category_logic.add(restaurant_uid, category_name)
    self.send_response(category)

  def get(self):
    restaurant_uid = self.request.get(RESTAURANT_UID)
    categories = category_logic.get_all_by_restaurant_uid(restaurant_uid)
    self.send_response(categories)

class Dish(BaseApiHandler):

  "TODO: add get all dishes."
  def get(self):
    restaurant_uid = self.request.get(RESTAURANT_UID)
    category_name = self.request.get(CATEGORY_NAME)
    dishes = None
    if restaurant_uid:
      if category_name:
        "TODO: get by restaurant_uid and category name"
        dishes = dish_logic.get_all_by_restaurant_uid(restaurant_uid)
      else:
        dishes = dish_logic.get_all_by_restaurant_uid(restaurant_uid)
    self.send_response(dishes)

  '''
    Adds a new dish.
    args:
      uid: The restaurant uid.
      category_name: The category name.
      name: The dish name.
  '''
  def post(self):
    restaurant_uid = self.request.get(RESTAURANT_UID)
    category_name = self.request.get(CATEGORY_NAME)
    dish_name = self.request.get(NAME)
    dish_image_data = self.request.get(IMAGE_DATA)
    img_key = None
    if dish_image_data:
      image = Image.from_image_data(dish_image_data) 
      image.put()
      img_key = image.key
    dish = dish_logic.add(restaurant_uid, category_name, dish_name, img_key)
    self.send_response(dish)

