import webapp2
import json

from logic import restaurant_logic
from util import json_encoder

class Restaurant(webapp2.RequestHandler):

  def post(self):
    restaurant_name = self.request.get('name')
    restaurant_uid = self.request.get('uid')
    restaurant = restaurant_logic.add(restaurant_uid, restaurant_name)
    self.response.headers['Content-Type'] = 'application/json'
    self.response.out.write(json_encoder.encode(restaurant))

class CheckRestaurantUid(webapp2.RequestHandler):

  def get(self):
    restaurant_uid = self.request.get('uid')
    self.response.headers['Content-Type'] = 'application/json'
    result = {"exist": restaurant_logic.check_uid_exist(restaurant_uid)}
    self.response.out.write(json.dumps(result))

