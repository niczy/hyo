import unittest

from test.model.base_model_test import ModelTestCase
from logic.exceptions import RestaurantExistError

from logic import restaurant_logic
from util import json_encoder

class RestaurantLogicTestCase(ModelTestCase):

  def testInsertRestaurant(self):
    restaurant_logic.add('FuLinMen3', 'restaurant')
    restaurants = restaurant_logic.get_all()
    self.assertEqual(1, len(restaurants))
    self.assertEqual('{"name": "restaurant"}', json_encoder.encode(restaurants[0]))

  def testGetByName(self):
    restaurant_logic.add('fulinmen2', 'fulinmen') 
    restaurants = restaurant_logic.get_by_name('fulinmen')
    self.assertEqual(1, len(restaurants))
    restaurant = restaurants[0]
    self.assertEqual('fulinmen', restaurant.name)

  def testGetById(self):
    restaurant_logic.add('fulinmenid', 'fulinmen') 
    restaurant = restaurant_logic.get_by_id('fulinmenid')
    self.assertEqual('fulinmen', restaurant.name)

    restaurant = restaurant_logic.get_by_id('notexist')
    self.assertIsNone(restaurant)

  def testAddingRestaurantWhenIdExis(self):
    restaurant_logic.add('fulinmen', 'fulinmen')
    try:
      restaurant_logic.add('fulinmen', 'fulinmen')
      self.fail('Should throw RestaurantExistError1')
    except RestaurantExistError:
      pass

    
    
