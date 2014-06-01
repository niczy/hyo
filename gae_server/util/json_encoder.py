from datetime import datetime, date, time
from google.appengine.ext import ndb
import json


ENTITY_KEY = 'key'

class JsonEncoder(json.JSONEncoder):

  def default(self, o):
    if isinstance(o, ndb.Key):
      return self.get_value_for_key(o)
    elif  isinstance(o, ndb.Model):
      d = o.to_dict()
      if o.key:
        d['key'] = o.key.urlsafe()
      return d 
    elif isinstance(o, (datatime, data, time)):
      return str(o)
    elif o == None:
      return ""
    return o

  def get_value_for_key(self, key):
    return key.urlsafe()



json_encoder = JsonEncoder()

def encode(model):
  return json_encoder.encode(model)
