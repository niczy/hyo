from test.controller.base_controller_test import BaseAppTest

class ApiTest(BaseAppTest):

  def testAddRestaurant(self):
    params = {'name': 'restaurant'}
    response = self.testapp.post('/api/restaurant', params)
    self.assertEqual(response.status_int, 200)
