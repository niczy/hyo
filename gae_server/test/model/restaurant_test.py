import unittest
from google.appengine.api import memcache
from google.appengine.ext import testbed

from model.restaurant import Restaurant

class RestaurantTestCase(unittest.TestCase):

  def setUp(self):
    self.testbed = testbed.Testbed()
    self.testbed.activate()
    self.testbed.init_datastore_v3_stub()
    self.testbed.init_memcache_stub()

  def testDown(self):
    self.testbed.deactivate()

  def testInsertEntity(self):
    restaurant = Restaurant()
    restaurant.put()
    self.assertEqual(1, len(Restaurant.query().fetch(2)))

if __name__ == '__main__':
  unittest.main()
