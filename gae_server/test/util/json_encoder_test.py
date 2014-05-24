import unittest

from model.restaurant import Restaurant
from util import json_encoder

class ModelTestCase(unittest.TestCase):

  def testEncode(self):
    restaurant = Restaurant(name = 'test')
    restaruants = [restaurant, restaurant]
    self.assertEqual('[{"logo": null, "name": "test", "uid": null}, {"logo": null, "name": "test", "uid": null}]',
        json_encoder.encode(restaruants))
