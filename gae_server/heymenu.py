import webapp2

from controllers import pages
from controllers.api import Restaurant

application = webapp2.WSGIApplication([
  ('/', pages.Index),
  ('/api/restaurant', Restaurant),
  ('/p/new-restaurant', pages.NewRestaurant),
  ('/p/signin', pages.SignIn),
  ('/.*', pages.Restaurant)
  ], debug=True)
