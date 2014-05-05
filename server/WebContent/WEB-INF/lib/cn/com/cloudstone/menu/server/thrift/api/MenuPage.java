/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package cn.com.cloudstone.menu.server.thrift.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 菜品页
 */
public class MenuPage implements org.apache.thrift.TBase<MenuPage, MenuPage._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MenuPage");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField LAYOUT_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("layoutType", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField GOODS_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("goodsList", org.apache.thrift.protocol.TType.LIST, (short)3);

  public int id; // required
  /**
   * 
   * @see PageLayoutType
   */
  public PageLayoutType layoutType; // required
  public List<Goods> goodsList; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ID((short)1, "id"),
    /**
     * 
     * @see PageLayoutType
     */
    LAYOUT_TYPE((short)2, "layoutType"),
    GOODS_LIST((short)3, "goodsList");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ID
          return ID;
        case 2: // LAYOUT_TYPE
          return LAYOUT_TYPE;
        case 3: // GOODS_LIST
          return GOODS_LIST;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ID_ISSET_ID = 0;
  private BitSet __isset_bit_vector = new BitSet(1);

  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.LAYOUT_TYPE, new org.apache.thrift.meta_data.FieldMetaData("layoutType", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, PageLayoutType.class)));
    tmpMap.put(_Fields.GOODS_LIST, new org.apache.thrift.meta_data.FieldMetaData("goodsList", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Goods.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MenuPage.class, metaDataMap);
  }

  public MenuPage() {
  }

  public MenuPage(
    int id,
    PageLayoutType layoutType,
    List<Goods> goodsList)
  {
    this();
    this.id = id;
    setIdIsSet(true);
    this.layoutType = layoutType;
    this.goodsList = goodsList;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MenuPage(MenuPage other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.id = other.id;
    if (other.isSetLayoutType()) {
      this.layoutType = other.layoutType;
    }
    if (other.isSetGoodsList()) {
      List<Goods> __this__goodsList = new ArrayList<Goods>();
      for (Goods other_element : other.goodsList) {
        __this__goodsList.add(new Goods(other_element));
      }
      this.goodsList = __this__goodsList;
    }
  }

  public MenuPage deepCopy() {
    return new MenuPage(this);
  }

  @Override
  public void clear() {
    setIdIsSet(false);
    this.id = 0;
    this.layoutType = null;
    this.goodsList = null;
  }

  public int getId() {
    return this.id;
  }

  public MenuPage setId(int id) {
    this.id = id;
    setIdIsSet(true);
    return this;
  }

  public void unsetId() {
    __isset_bit_vector.clear(__ID_ISSET_ID);
  }

  /** Returns true if field id is set (has been assigned a value) and false otherwise */
  public boolean isSetId() {
    return __isset_bit_vector.get(__ID_ISSET_ID);
  }

  public void setIdIsSet(boolean value) {
    __isset_bit_vector.set(__ID_ISSET_ID, value);
  }

  /**
   * 
   * @see PageLayoutType
   */
  public PageLayoutType getLayoutType() {
    return this.layoutType;
  }

  /**
   * 
   * @see PageLayoutType
   */
  public MenuPage setLayoutType(PageLayoutType layoutType) {
    this.layoutType = layoutType;
    return this;
  }

  public void unsetLayoutType() {
    this.layoutType = null;
  }

  /** Returns true if field layoutType is set (has been assigned a value) and false otherwise */
  public boolean isSetLayoutType() {
    return this.layoutType != null;
  }

  public void setLayoutTypeIsSet(boolean value) {
    if (!value) {
      this.layoutType = null;
    }
  }

  public int getGoodsListSize() {
    return (this.goodsList == null) ? 0 : this.goodsList.size();
  }

  public java.util.Iterator<Goods> getGoodsListIterator() {
    return (this.goodsList == null) ? null : this.goodsList.iterator();
  }

  public void addToGoodsList(Goods elem) {
    if (this.goodsList == null) {
      this.goodsList = new ArrayList<Goods>();
    }
    this.goodsList.add(elem);
  }

  public List<Goods> getGoodsList() {
    return this.goodsList;
  }

  public MenuPage setGoodsList(List<Goods> goodsList) {
    this.goodsList = goodsList;
    return this;
  }

  public void unsetGoodsList() {
    this.goodsList = null;
  }

  /** Returns true if field goodsList is set (has been assigned a value) and false otherwise */
  public boolean isSetGoodsList() {
    return this.goodsList != null;
  }

  public void setGoodsListIsSet(boolean value) {
    if (!value) {
      this.goodsList = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((Integer)value);
      }
      break;

    case LAYOUT_TYPE:
      if (value == null) {
        unsetLayoutType();
      } else {
        setLayoutType((PageLayoutType)value);
      }
      break;

    case GOODS_LIST:
      if (value == null) {
        unsetGoodsList();
      } else {
        setGoodsList((List<Goods>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return Integer.valueOf(getId());

    case LAYOUT_TYPE:
      return getLayoutType();

    case GOODS_LIST:
      return getGoodsList();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case ID:
      return isSetId();
    case LAYOUT_TYPE:
      return isSetLayoutType();
    case GOODS_LIST:
      return isSetGoodsList();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof MenuPage)
      return this.equals((MenuPage)that);
    return false;
  }

  public boolean equals(MenuPage that) {
    if (that == null)
      return false;

    boolean this_present_id = true;
    boolean that_present_id = true;
    if (this_present_id || that_present_id) {
      if (!(this_present_id && that_present_id))
        return false;
      if (this.id != that.id)
        return false;
    }

    boolean this_present_layoutType = true && this.isSetLayoutType();
    boolean that_present_layoutType = true && that.isSetLayoutType();
    if (this_present_layoutType || that_present_layoutType) {
      if (!(this_present_layoutType && that_present_layoutType))
        return false;
      if (!this.layoutType.equals(that.layoutType))
        return false;
    }

    boolean this_present_goodsList = true && this.isSetGoodsList();
    boolean that_present_goodsList = true && that.isSetGoodsList();
    if (this_present_goodsList || that_present_goodsList) {
      if (!(this_present_goodsList && that_present_goodsList))
        return false;
      if (!this.goodsList.equals(that.goodsList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(MenuPage other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    MenuPage typedOther = (MenuPage)other;

    lastComparison = Boolean.valueOf(isSetId()).compareTo(typedOther.isSetId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id, typedOther.id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLayoutType()).compareTo(typedOther.isSetLayoutType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLayoutType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.layoutType, typedOther.layoutType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGoodsList()).compareTo(typedOther.isSetGoodsList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGoodsList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.goodsList, typedOther.goodsList);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    org.apache.thrift.protocol.TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == org.apache.thrift.protocol.TType.STOP) { 
        break;
      }
      switch (field.id) {
        case 1: // ID
          if (field.type == org.apache.thrift.protocol.TType.I32) {
            this.id = iprot.readI32();
            setIdIsSet(true);
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // LAYOUT_TYPE
          if (field.type == org.apache.thrift.protocol.TType.I32) {
            this.layoutType = PageLayoutType.findByValue(iprot.readI32());
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 3: // GOODS_LIST
          if (field.type == org.apache.thrift.protocol.TType.LIST) {
            {
              org.apache.thrift.protocol.TList _list12 = iprot.readListBegin();
              this.goodsList = new ArrayList<Goods>(_list12.size);
              for (int _i13 = 0; _i13 < _list12.size; ++_i13)
              {
                Goods _elem14; // required
                _elem14 = new Goods();
                _elem14.read(iprot);
                this.goodsList.add(_elem14);
              }
              iprot.readListEnd();
            }
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    // check for required fields of primitive type, which can't be checked in the validate method
    validate();
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    oprot.writeFieldBegin(ID_FIELD_DESC);
    oprot.writeI32(this.id);
    oprot.writeFieldEnd();
    if (this.layoutType != null) {
      oprot.writeFieldBegin(LAYOUT_TYPE_FIELD_DESC);
      oprot.writeI32(this.layoutType.getValue());
      oprot.writeFieldEnd();
    }
    if (this.goodsList != null) {
      oprot.writeFieldBegin(GOODS_LIST_FIELD_DESC);
      {
        oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, this.goodsList.size()));
        for (Goods _iter15 : this.goodsList)
        {
          _iter15.write(oprot);
        }
        oprot.writeListEnd();
      }
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("MenuPage(");
    boolean first = true;

    sb.append("id:");
    sb.append(this.id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("layoutType:");
    if (this.layoutType == null) {
      sb.append("null");
    } else {
      sb.append(this.layoutType);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("goodsList:");
    if (this.goodsList == null) {
      sb.append("null");
    } else {
      sb.append(this.goodsList);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

}
