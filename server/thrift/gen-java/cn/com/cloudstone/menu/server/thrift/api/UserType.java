/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package cn.com.cloudstone.menu.server.thrift.api;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum UserType implements org.apache.thrift.TEnum {
  Waiter(0),
  Admin(1),
  Customer(2);

  private final int value;

  private UserType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static UserType findByValue(int value) { 
    switch (value) {
      case 0:
        return Waiter;
      case 1:
        return Admin;
      case 2:
        return Customer;
      default:
        return null;
    }
  }
}
