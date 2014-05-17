import webapp2
import json

from google.appengine.ext import ndb
from logic import category_logic
from logic import restaurant_logic
from util import json_encoder


'''
 Request parameter keys.
'''
RESTAURANT_UID = 'uid'
NAME = 'name'

class BaseApiHandler(webapp2.RequestHandler):

  def send_response(self, resp):
    self.response.headers['Content-Type'] = 'application/json'
    if isinstance(resp, str):
      self.response.out.write(resp)
    elif isinstance(resp, dict):
      self.response.out.write(json.dumps(resp))
    elif isinstance(resp, ndb.Model):
      self.response.out.write(json_encoder.encode(resp))

class Restaurant(BaseApiHandler):

  def post(self):
    restaurant_name = self.request.get(NAME)
    restaurant_uid = self.request.get(RESTAURANT_UID)
    restaurant = restaurant_logic.add(restaurant_uid, restaurant_name)
    self.send_response(restaurant)

class CheckRestaurantUid(BaseApiHandler):

  def get(self):
    restaurant_uid = self.request.get(RESTAURANT_UID)
    result = {"exist": restaurant_logic.check_uid_exist(restaurant_uid)}
    self.send_response(result)

class Category(BaseApiHandler):

  '''
    TODO: test
  '''
  def post(self):
    restaurant_uid = self.request.get(RESTAURANT_UID)
    category_name = self.request.get(NAME)
    category = category_logic.add(restaurant_uid, category_name)
    self.send_response(category)

class Dish(webapp2.RequestHandler):

  "TODO: add get all dishes."
  def get(self):
    pass

