import logging
import webapp2
from controllers.templates import JINJA_ENVIRONMENT

from logic import category_logic
from logic import dish_logic
from util import json_encoder

class BasePage(webapp2.RequestHandler):

  def render(self, template_name, model = None):
    template = JINJA_ENVIRONMENT.get_template(template_name)
    if model == None:
      self.response.write(template.render())
    else:
      self.response.write(template.render(model))


class Index(BasePage):

  def get(self):
    self.render('index.html')


'''
  Displays a restaruant's menu.
'''
class Restaurant(BasePage):

  def get(self, restaurant_uid):
    "TODO: get restaurant uid from the path."
    logging.info(restaurant_uid)
    categories = category_logic.get_all_by_restaurant_uid(restaurant_uid)
    dishes = dish_logic.get_all_by_restaurant_uid(restaurant_uid)
    self.render('restaurant.html',
        {"categories": json_encoder.encode(categories),
          "dishes": json_encoder.encode(dishes),
          "restaurant_uid": restaurant_uid})


class NewRestaurant(BasePage):

  def get(self):
    self.render('new_restaurant.html')


class SignIn(BasePage):

  def get(self):
    self.render('signin.html')




