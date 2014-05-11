class EntityNotExistError(Exception):
  
  def __init__(self, entity_key):
    self.entity_key = entity_key

  def __str__(self):
    return repr(self.entity_key)

class RestaurantNotExistError(EntityNotExistError):
  pass

class CategoryNotExistError(EntityNotExistError):
  pass
