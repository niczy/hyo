import webapp2

from controllers.index import MainPage
from controllers.api import Restaurant

application = webapp2.WSGIApplication([
  ('/', MainPage),
  ('/api/restaurant', Restaurant)], debug=True)
