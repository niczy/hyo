import unittest
from google.appengine.api import memcache
from google.appengine.ext import testbed


from base_model_test import ModelTestCase
from model.image import Image 

class ImageTestCase(ModelTestCase):

  base64_data = ('iVBORw0KGgoAAAAN'
                'SUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQ'
                'I12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y'
                '4OHwAAAABJRU5ErkJggg==') 

  image_data = ('data:image/png;base64,'
                'iVBORw0KGgoAAAAN'
                'SUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQ'
                'I12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y'
                '4OHwAAAABJRU5ErkJggg==')

  def testInsertEntity(self):
    image = Image.from_image_data(self.image_data)
    image.put()
    self.assertEqual('image/png', image.mimetype)
    self.assertEqual(1, len(image.query().fetch(2)))

  def testDecode(self):
    decoded_data = Image.decode_image_data(self.base64_data)
    f = open('image.png', 'r')
    self.assertEqual(f.read(), decoded_data)

if __name__ == '__main__':
  unittest.main()
