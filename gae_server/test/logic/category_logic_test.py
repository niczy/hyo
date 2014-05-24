from test.model.base_model_test import ModelTestCase

from google.appengine.ext import ndb

from logic import restaurant_logic
from logic import category_logic
from logic.exceptions import RestaurantNotExistError 

from model.image import Image

class CategoryLogicTest(ModelTestCase):

  image_key = ndb.Key(Image, 'image_id')

  def test_add_and_get_categories(self):
    restaurant = restaurant_logic.add('FulinMen', 'restaurant', self.image_key) 
    category_logic.add(restaurant.key, "Category 1")
    category_logic.add(restaurant.key, "Cateogyr 2")
    categories = category_logic.get_all_by_restaurant_key(restaurant.key)
    self.assertEqual(2, len(categories))

    self.assertIsNotNone(category_logic.add('FulinMen', 'Category 3'))
    category_logic.add('FulinMen', 'Category 4')
    categories = category_logic.get_all_by_restaurant_key(restaurant.key)
    self.assertEqual(4, len(categories))

  def test_get_category_not_exist(self):
    try:
      categories = category_logic.get_all_by_restaurant_key(
          ndb.Key('Restaurant', 'id'))
      self.fail('Should throw RestaurantNotExistError')
    except RestaurantNotExistError as e:
      pass

  def test_add_category_not_exist(self):
    try:
      categories = category_logic.add(
          ndb.Key('Restaurant', 'id'), 'Category')
      self.fail('Should throw RestaurantNotExistError')
    except RestaurantNotExistError as e:
      pass


