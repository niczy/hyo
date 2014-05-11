import unittest
from google.appengine.api import memcache
from google.appengine.ext import testbed


from base_model_test import ModelTestCase
from model.dish import Dish 

class DishTestCase(ModelTestCase):

  def testInsertEntity(self):
    dish = Dish()
    dish.put()
    self.assertEqual(1, len(Dish.query().fetch(2)))

if __name__ == '__main__':
  unittest.main()
