from datetime import datetime, date, time
from google.appengine.ext import ndb
import json

class JsonEncoder(json.JSONEncoder):

  def default(self, o):
    if isinstance(o, ndb.Key):
      o = o.get()

    if isinstance(o, ndb.Model):
      return o.to_dict()
    elif isinstance(o, (datatime, data, time)):
      return str(o)

json_encoder = JsonEncoder()

def encode(model):
  return json_encoder.encode(model)
