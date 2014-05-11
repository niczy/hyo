import unittest
import webtest
from google.appengine.api import memcache
from google.appengine.ext import testbed

from heymenu import application


class BaseAppTest(unittest.TestCase):

  def setUp(self):
    self.testbed = testbed.Testbed()
    self.testbed.activate()
    self.testbed.init_datastore_v3_stub()
    self.testbed.init_memcache_stub()
    self.testapp = webtest.TestApp(application)

  def testHelloWorld(self):
    response = self.testapp.get('/')
    self.assertEqual(response.status_int, 200)

