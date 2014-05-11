import unittest

from test.model.base_model_test import ModelTestCase

from logic import restaurant_logic
from util import json_encoder

class RestaurantLogicTestCase(ModelTestCase):

  def testInsertRestaurant(self):
    restaurant_logic.add('restaurant')
    restaurants = restaurant_logic.get_all()
    self.assertEqual(1, len(restaurants))
    self.assertEqual('{"name": "restaurant"}', json_encoder.encode(restaurants[0]))

