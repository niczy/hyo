import base64
import re
from google.appengine.ext import ndb

from exceptions import InvalidImageData

class Image(ndb.Model):

  blob = ndb.BlobProperty()
  mimetype = ndb.StringProperty()

  @classmethod
  def from_image_data(cls, image_data):
    parts = re.split(',', image_data)
    if len(parts) != 2:
      raise InvalidImageData()
    metas = re.split(';', parts[0])
    head = re.split(':', metas[0])
    metadata = None
    if len(head) == 2 and not head[1].startswith('charset') and not head[1].startswith('base64'):
      metadata = head[1]

    return Image(blob = Image.decode_image_data(parts[1]), mimetype = metadata.encode('utf-8'))

  @staticmethod
  def decode_image_data(base64_data):
    return base64.standard_b64decode(base64_data)

