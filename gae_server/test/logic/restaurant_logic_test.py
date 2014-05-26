import unittest

from google.appengine.ext import ndb
from test.model.base_model_test import ModelTestCase

from logic.exceptions import RestaurantExistError
from logic import restaurant_logic
from model.image import Image
from util import json_encoder

class RestaurantLogicTestCase(ModelTestCase):

  image_key = ndb.Key(Image, 'image_id')

  def testInsertAndDeleteRestaurant(self):
    restaurant_uid = 'FuLinMen'
    restaurant_logic.add(restaurant_uid, 'restaurant', self.image_key)
    restaurants = restaurant_logic.get_all()
    self.assertEqual(1, len(restaurants))

    restaurant_logic.delete(restaurant_uid)
    restaurants = restaurant_logic.get_all()
    self.assertEqual(0, len(restaurants))


  def testGetByName(self):
    restaurant_logic.add('fulinmen', 'fulinmen', self.image_key) 
    restaurants = restaurant_logic.get_by_name('fulinmen')
    self.assertEqual(1, len(restaurants))
    restaurant = restaurants[0]
    self.assertEqual('fulinmen', restaurant.name)

  def testGetById(self):
    restaurant_logic.add('fulinmenid', 'fulinmen', self.image_key) 
    restaurant = restaurant_logic.get_by_id('fulinmenid')
    self.assertEqual('fulinmen', restaurant.name)

    restaurant = restaurant_logic.get_by_id('notexist')
    self.assertIsNone(restaurant)

  def testGetById(self):
    restaurant_logic.add('fulinmenid', 'fulinmen', self.image_key) 
    self.assertTrue(restaurant_logic.check_uid_exist('fulinmenid'))
    self.assertFalse(restaurant_logic.check_uid_exist('fulinmen'))

  def testAddingRestaurantWhenIdExis(self):
    restaurant_logic.add('fulinmen', 'fulinmen', self.image_key)
    try:
      restaurant_logic.add('fulinmen', 'fulinmen', self.image_key)
      self.fail('Should throw RestaurantExistError1')
    except RestaurantExistError:
      pass

