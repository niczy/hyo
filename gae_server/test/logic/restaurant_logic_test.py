import unittest

from logic import restaurant_logic

from test.model.base_model_test import ModelTestCase

class RestaurantLogicTestCase(ModelTestCase):

  def testInsertRestaurant(self):
    restaurant_logic.add('restaurant')
    restaurants = restaurant_logic.get_all()
    self.assertEqual(1, len(restaurants))

