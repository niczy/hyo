import unittest
from google.appengine.api import memcache
from google.appengine.ext import testbed


from base_model_test import ModelTestCase
from model.category import Category 

class CategoryTestCase(ModelTestCase):

  def testInsertEntity(self):
    category = Category()
    category.put()
    self.assertEqual(1, len(Category.query().fetch(2)))

if __name__ == '__main__':
  unittest.main()
