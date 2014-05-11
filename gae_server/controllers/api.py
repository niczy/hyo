import webapp2

from logic import restaurant_logic

class Restaurant(webapp2.RequestHandler):

  def post(self):
    restaurant_name = self.request.get('name')
    restaurant = restaurant_logic.add(restaurant_name)
