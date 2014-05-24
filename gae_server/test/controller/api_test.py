import json
import base64
from test.controller.base_controller_test import BaseAppTest

from logic import category_logic
from logic import dish_logic
from logic import restaurant_logic

class ApiTest(BaseAppTest):

  base64_data = ('iVBORw0KGgoAAAAN'
                'SUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQ'
                'I12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y'
                '4OHwAAAABJRU5ErkJggg==') 

  image_data = ('data:image/png;base64,'
                'iVBORw0KGgoAAAAN'
                'SUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQ'
                'I12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y'
                '4OHwAAAABJRU5ErkJggg==')

  def testAddRestaurant(self):
    params = {'name': 'restaurant', 'uid': 'restaurant-uid', 'image_data': self.image_data}
    response = self.testapp.post('/api/restaurant', params)
    self.assertEqual(response.status_int, 200)
    self.assertEqual(json.dumps(response.json),
        '{"logo": "agx0ZXN0YmVkLXRlc3RyCwsSBUltYWdlGAEM", "name": "restaurant", "uid": "restaurant-uid"}')
    response = self.testapp.get('/image/%s' % response.json['logo'])
    new_image = base64.standard_b64encode(response.body) 
    self.assertEqual(self.base64_data, new_image)
    self.assertEqual(response.status_int, 200)

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
        '{"restaurant_key": "agx0ZXN0YmVkLXRlc3RyEwsSClJlc3RhdXJhbnQiA3VpZAw", "name": "name"}')

  def testGetCategories(self):
    restaurant_uid = 'uid'
    restaurant_logic.add(restaurant_uid, 'fulinmen')
    category_logic.add(restaurant_uid, 'category 1')
    category_logic.add(restaurant_uid, 'category 2')
    response = self.testapp.get('/api/category?uid=%s' % restaurant_uid)
    self.assertEqual(response.status_int, 200)
    self.assertEqual(json.dumps(response.json), '[{"restaurant_key": "agx0ZXN0YmVkLXRlc3RyEwsSClJlc3RhdXJhbnQiA3VpZAw", "name": "category 1"}, {"restaurant_key": "agx0ZXN0YmVkLXRlc3RyEwsSClJlc3RhdXJhbnQiA3VpZAw", "name": "category 2"}]')

  def testAddDish(self):
    restaurant_uid = 'uid'
    restaurant_logic.add(restaurant_uid, 'fulinmen')
    category_logic.add(restaurant_uid, 'category 1')
    params = {'uid': restaurant_uid, 'category_name': 'category 1', 'name': 'dish 1'}
    response = self.testapp.post('/api/dish', params)
    self.assertEqual(response.status_int, 200)
    self.assertEqual(json.dumps(response.json), '{"category_key": "agx0ZXN0YmVkLXRlc3RyDgsSCENhdGVnb3J5GAEM", "restaurant_key": "agx0ZXN0YmVkLXRlc3RyEwsSClJlc3RhdXJhbnQiA3VpZAw", "name": "dish 1"}')

  def testGetDishes(self):
    restaurant_uid = 'uid'
    category_name = 'soup'
    restaurant_logic.add(restaurant_uid, 'fulinmen')
    category_logic.add(restaurant_uid, category_name)
    dish_logic.add(restaurant_uid, category_name, 'Dish 1')
    dish_logic.add(restaurant_uid, category_name, 'Dish 2')
    response = self.testapp.get('/api/dish?uid=%s' % restaurant_uid)
    self.assertEqual(response.status_int, 200)
    self.assertEqual(json.dumps(response.json), '[{"category_key": "agx0ZXN0YmVkLXRlc3RyDgsSCENhdGVnb3J5GAEM", "restaurant_key": "agx0ZXN0YmVkLXRlc3RyEwsSClJlc3RhdXJhbnQiA3VpZAw", "name": "Dish 1"}, {"category_key": "agx0ZXN0YmVkLXRlc3RyDgsSCENhdGVnb3J5GAEM", "restaurant_key": "agx0ZXN0YmVkLXRlc3RyEwsSClJlc3RhdXJhbnQiA3VpZAw", "name": "Dish 2"}]')

