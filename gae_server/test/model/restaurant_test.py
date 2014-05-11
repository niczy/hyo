import unittest
from google.appengine.api import memcache
from google.appengine.ext import testbed


from base_model_test import ModelTestCase
from model.restaurant import Restaurant
from util import json_encoder

class RestaurantTestCase(ModelTestCase):

  def testInsertEntity(self):
    restaurant = Restaurant()
    restaurant.put()
    self.assertEqual(1, len(Restaurant.query().fetch(2)))

  def testSerialization(self):
    restaurant = Restaurant(name = 'Restaurant')
    self.assertEqual('{"name": "Restaurant"}', json_encoder.encode(restaurant))

if __name__ == '__main__':
  unittest.main()
