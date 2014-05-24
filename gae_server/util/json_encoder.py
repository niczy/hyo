from datetime import datetime, date, time
from google.appengine.ext import ndb
import json

class JsonEncoder(json.JSONEncoder):

  def default(self, o):
    if isinstance(o, ndb.Key):
      return self.get_value_for_key(o)
    elif  isinstance(o, ndb.Model):
      return o.to_dict()
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
