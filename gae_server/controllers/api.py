import webapp2
import json

from google.appengine.ext import ndb
from logic import restaurant_logic
from util import json_encoder

class BaseApiHandler(webapp2.RequestHandler):

  def sendResponse(self, resp):
    self.response.headers['Content-Type'] = 'application/json'
    if isinstance(resp, str):
      self.response.out.write(resp)
    elif isinstance(resp, dict):
      self.response.out.write(json.dumps(resp))
    elif isinstance(resp, ndb.Model):
      self.response.out.write(json_encoder.encode(resp))

class Restaurant(BaseApiHandler):

  def post(self):
    restaurant_name = self.request.get('name')
    restaurant_uid = self.request.get('uid')
    restaurant = restaurant_logic.add(restaurant_uid, restaurant_name)
    self.sendResponse(restaurant)

class CheckRestaurantUid(BaseApiHandler):

  def get(self):
    restaurant_uid = self.request.get('uid')
    result = {"exist": restaurant_logic.check_uid_exist(restaurant_uid)}
    self.sendResponse(result)

class Dish(webapp2.RequestHandler):

  def get(self):
    pass

