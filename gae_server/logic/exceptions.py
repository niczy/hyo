class EntityNotExistError(Exception):
  
  def __init__(self, entity_key):
    self.entity_key = entity_key

  def __str__(self):
    return repr(self.entity_key)

class EntityExistError(Exception):
  
  def __init__(self, entity_key):
    self.entity_key = entity_key

  def __str__(self):
    return repr(self.entity_key)

class InvalidInputError(Exception):
  pass



'''
  Raised when trying to fetch a restaurant with a key which 
  doesn't exist.
'''
class RestaurantNotExistError(EntityNotExistError):
  pass

'''
  Raised when trying to fetch a Category with a key which 
  doesn't exist.
'''
class CategoryNotExistError(EntityNotExistError):
  pass

class DishNotExistError(EntityNotExistError):
  pass

'''
  Raised when the new Restaurant uid exists.
'''
class RestaurantExistError(EntityExistError):
  pass
