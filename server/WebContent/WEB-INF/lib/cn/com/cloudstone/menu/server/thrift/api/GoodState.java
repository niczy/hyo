/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package cn.com.cloudstone.menu.server.thrift.api;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

/**
 * 商品状态。写这个的那个人乱写的名字。
 * 对应的状态按顺序分别是
 * "已点","上过","外带","等叫","已退"
 */
public enum GoodState implements org.apache.thrift.TEnum {
  Ordered(0),
  Servered(1),
  Takeout(2),
  Waiting(3),
  Canceled(4);

  private final int value;

  private GoodState(int value) {
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
  public static GoodState findByValue(int value) { 
    switch (value) {
      case 0:
        return Ordered;
      case 1:
        return Servered;
      case 2:
        return Takeout;
      case 3:
        return Waiting;
      case 4:
        return Canceled;
      default:
        return null;
    }
  }
}