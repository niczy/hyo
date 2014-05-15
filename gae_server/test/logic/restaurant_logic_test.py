import unittest

from test.model.base_model_test import ModelTestCase
from logic.exceptions import RestaurantExistError

from logic import restaurant_logic
from util import json_encoder

class RestaurantLogicTestCase(ModelTestCase):

  def testInsertRestaurant(self):
    restaurant_logic.add('FuLinMen', 'restaurant')
    restaurants = restaurant_logic.get_all()
    self.assertEqual(1, len(restaurants))
    self.assertEqual('{"name": "restaurant", "uid": "FuLinMen"}',
        json_encoder.encode(restaurants[0]))

  def testGetByName(self):
    restaurant_logic.add('fulinmen', 'fulinmen') 
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

  def testGetById(self):
    restaurant_logic.add('fulinmenid', 'fulinmen') 
    self.assertTrue(restaurant_logic.check_uid_exist('fulinmenid'))
    self.assertFalse(restaurant_logic.check_uid_exist('fulinmen'))

  def testAddingRestaurantWhenIdExis(self):
    restaurant_logic.add('fulinmen', 'fulinmen')
    try:
      restaurant_logic.add('fulinmen', 'fulinmen')
      self.fail('Should throw RestaurantExistError1')
    except RestaurantExistError:
      pass

    
    
