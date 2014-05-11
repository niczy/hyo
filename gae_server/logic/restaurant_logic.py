from model.restaurant import Restaurant

def create(name):
  restaurant = Restaurant(name = name)
  restaurant.put()
  return restaurant;

def get_all():
  return Restaurant.query().fetch()

def get_by_name(name):
  return Restaurant.query(Restaurant.name == name).fetch()

def get_by_id(key):
  return Restaurant.get_by_id(key)

