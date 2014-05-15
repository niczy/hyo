from model.restaurant import Restaurant
from exceptions import RestaurantExistError

'''
 Add a new restaurant.
 args:
  uid: The entity id.
  name: The restaurant name. Usually the uid is the same as
    the name. In case of confliction, the user needs to pick
    another uid. The restaurant can be accessed by
    http://domain/uid
'''
def add(uid, name):
  if get_by_id(uid):
    raise RestaurantExistError(uid)
  restaurant = Restaurant(id = uid, uid = uid, name = name)
  restaurant.put()
  return restaurant;

def get_all():
  return Restaurant.query().fetch()

'''
  Checks if given uid exists.
'''
def check_uid_exist(uid):
  return get_by_id(uid) != None

def get_by_name(name):
  return Restaurant.query(Restaurant.name == name).fetch()

'''
 Returns a Restaurant instance with given key.
'''
def get_by_id(entity_id):
  return Restaurant.get_by_id(entity_id)

