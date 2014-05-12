import webapp2

from logic import restaurant_logic
from util import json_encoder

class Restaurant(webapp2.RequestHandler):

  def post(self):
    restaurant_name = self.request.get('name')
    restaurant = restaurant_logic.add(restaurant_name)
    self.response.headers['Content-Type'] = 'application/json'
    self.response.out.write(json_encoder.encode(restaurant))
