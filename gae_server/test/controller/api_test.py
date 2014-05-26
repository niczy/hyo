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

  resized_base64_data = ('/9j/4AAQSkZJRgABAQAAAQABAAD/'
                '2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSE'
                'w8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ'
                '0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIR'
                'whMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyM'
                'jIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAACA'
                'AIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAA'
                'AAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA'
                'AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0Kxw'
                'RVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RF'
                'RkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDh'
                'IWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7'
                'i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6er'
                'x8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAA'
                'AAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQ'
                'J3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMz'
                'UvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERU'
                'ZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOE'
                'hYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uL'
                'm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09'
                'fb3+Pn6/9oADAMBAAIRAxEAPwDm6KKK+cP2c//Z')

  image_data = ('data:image/png;base64,'
                'iVBORw0KGgoAAAAN'
                'SUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQ'
                'I12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y'
                '4OHwAAAABJRU5ErkJggg==')

  def testAddAndDeleteRestaurant(self):
    restaurant_uid = 'restaurant-uid';
    params = {'name': 'restaurant', 'uid': restaurant_uid, 'image_data': self.image_data}
    response = self.testapp.post('/api/restaurant', params)
    self.assertEqual(response.status_int, 200)
    self.assertEqual(response.json,
        {"logo": "agx0ZXN0YmVkLXRlc3RyCwsSBUltYWdlGAEM", "name": "restaurant", "uid": "restaurant-uid"})

    "Test getting resized image"
    response = self.testapp.get('/image/%s?height=2&width=2' % response.json['logo'])
    new_image = base64.standard_b64encode(response.body) 
    self.assertEqual(self.resized_base64_data, new_image)
    self.assertEqual(response.status_int, 200)

    "Test deleting image"
    params = {'uid': restaurant_uid}
    response = self.testapp.delete('/api/restaurant/' + restaurant_uid)
    self.assertEqual(response.status_int, 200)
    self.assertEqual(response.json, {"status": "ok"})

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

