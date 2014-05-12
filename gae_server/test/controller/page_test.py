import json
from test.controller.base_controller_test import BaseAppTest

class PageTest(BaseAppTest):

  def testBasicPage(self):
    self.check_page('/')
    self.check_page('/p/new-restaurant')
    self.check_page('/p/signin')

  def check_page(self, path):
    response = self.testapp.get(path)
    self.assertEqual(response.status_int, 200)
