from test.model.base_model_test import ModelTestCase

from google.appengine.ext import ndb
from logic import restaurant_logic
from logic import category_logic
from logic import dish_logic
from logic.exceptions import CategoryNotExistError

class DishLogicTest(ModelTestCase):

  def test_add_and_get_dishes(self):
    restaurant = restaurant_logic.create('restaurant')
    category = category_logic.add(restaurant.key, "category")
    dish_logic.add(category.key, 'Dish1')
    dish_logic.add(category.key, 'Dish2')
    dishes = dish_logic.get_all_by_category_key(category.key)
    self.assertEqual(2, len(dishes))

  def test_category_not_exist(self):
    non_exist_key = ndb.Key('Category', 'id')
    try:
      dishes = dish_logic.get_all_by_category_key(non_exist_key)
      self.fail('Shoud throw CategoryNotExistError')
    except CategoryNotExistError:
      pass

    try:
      dish = dish_logic.add(non_exist_key, 'dish name')
      self.fail('Shoud throw CategoryNotExistError')
    except CategoryNotExistError:
      pass