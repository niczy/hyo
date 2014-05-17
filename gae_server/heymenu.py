import webapp2

from controllers import pages
from controllers.api import Category
from controllers.api import Restaurant
from controllers.api import CheckRestaurantUid

application = webapp2.WSGIApplication([
  ('/', pages.Index),
  ('/api/restaurant', Restaurant),
  ('/api/category', Category),
  ('/api/check_restaurant_uid', CheckRestaurantUid),
  ('/p/new-restaurant', pages.NewRestaurant),
  ('/p/signin', pages.SignIn),
  ('/.*', pages.Restaurant)
  ], debug=True)
