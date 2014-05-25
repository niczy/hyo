import webapp2
import logging


from google.appengine.api import images
from google.appengine.ext import ndb

IMAGE_ID = "image_id"
HEIGHT  = "height"
WIDTH = "width"

'''
  Center crop image with height and width.
'''
class ImageHandler(webapp2.RequestHandler):

  def get(self, image_id): 
    image_key = ndb.Key(urlsafe = image_id)
    image = image_key.get()
    new_img = images.Image(image_data = image.blob)
    real_width = new_img.width
    real_height = new_img.height
    width = int(self.request.get(WIDTH, real_width))
    height = int(self.request.get(HEIGHT, real_height))
    width = width if width < real_width else real_width
    height = height if height < real_height else real_height
    center_x = new_img.width / 2.0
    center_y = new_img.height / 2.0
    ratio_x = real_width * 1.0 / width
    ratio_y = real_height * 1.0 / height
    resize_width = width
    resize_height = height
    ratio = ratio_x
    if ratio_x < ratio_y:
      resize_height = int(real_height / ratio_x)
    else:
      resize_width = int(real_width / ratio_y)
      ratio = ratio_y
    
    new_img.resize(width = resize_width, height = resize_height)
    offset_x = width / 2
    offset_y = height / 2
    left_x = (center_x - offset_x * ratio) / real_width
    top_y = (center_y - offset_y * ratio) / real_height
    right_x = (center_x + offset_x * ratio) / real_width
    bottom_y = (center_y + offset_y * ratio) / real_height
    new_img.crop(left_x, top_y, right_x, bottom_y)
    output_img = new_img.execute_transforms(output_encoding=images.JPEG)
    self.response.headers['Content-Type'] = 'image/jpeg' 
    self.response.write(output_img)

