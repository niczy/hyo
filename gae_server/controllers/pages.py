import webapp2
from controllers.templates import JINJA_ENVIRONMENT

class BasePage(webapp2.RequestHandler):

  def render(self, template_name):
    template = JINJA_ENVIRONMENT.get_template(template_name)
    self.response.write(template.render())


class Index(BasePage):

  def get(self):
    self.render('index.html')


class Restaurant(BasePage):

  def get(self):
    self.render('restaurant.html')


class NewRestaurant(BasePage):

  def get(self):
    self.render('new_restaurant.html')


class SignIn(BasePage):

  def get(self):
    self.render('signin.html')




