import webapp2

from controllers.index import MainPage

application = webapp2.WSGIApplication([('/', MainPage)], debug=True)
