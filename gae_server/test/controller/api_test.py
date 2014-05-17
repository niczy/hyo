import json
from test.controller.base_controller_test import BaseAppTest

from logic import restaurant_logic

class ApiTest(BaseAppTest):

  def testAddRestaurant(self):
    params = {'name': 'restaurant', 'uid': 'restaurant-uid'}
    response = self.testapp.post('/api/restaurant', params)
    self.assertEqual(response.status_int, 200)
    self.assertEqual(json.dumps(response.json),
        '{"name": "restaurant", "uid": "restaurant-uid"}')

  def testCheckRestaurantUid(self):
    response = self.testapp.get('/api/check_restaurant_uid?uid=notexist')
    self.assertEqual(response.status_int, 200)
    self.assertEqual(json.dumps(response.json),
        '{"exist": false}')

    restaurant_logic.add('restaurant_uid', 'reataurant_name')
    response = self.testapp.get('/api/check_restaurant_uid?uid=restaurant_uid')
    self.assertEqual(response.status_int, 200)
    self.assertEqual(json.dumps(response.json),
        '{"exist": true}')

  def testAddCategory(self):
    restaurant_uid = 'uid'
    category_name = 'name'
    restaurant_logic.add(restaurant_uid, 'restaurant_name')
    params = {'uid': restaurant_uid, 'name': category_name}
    response = self.testapp.post('/api/category', params)
    self.assertEqual(response.status_int, 200)
    self.assertEqual(json.dumps(response.json),
        '{"restaurant_key": {"name": "restaurant_name", "uid": "uid"}, "name": "name"}')


