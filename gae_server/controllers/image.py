import webapp2

from google.appengine.ext import ndb

IMAGE_ID = "image_id"
class ImageHandler(webapp2.RequestHandler):

  def get(self, image_id): 
    image_key = ndb.Key(urlsafe = image_id)
    image = image_key.get()
    self.response.headers['Content-Type'] = image.mimetype.encode('utf-8') 
    self.response.write(image.blob)

