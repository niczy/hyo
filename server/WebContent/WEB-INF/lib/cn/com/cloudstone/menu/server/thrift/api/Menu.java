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
 * 菜单
 */
public class Menu implements org.apache.thrift.TBase<Menu, Menu._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Menu");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("name", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PAGES_FIELD_DESC = new org.apache.thrift.protocol.TField("pages", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField MENU_LOGO_FIELD_DESC = new org.apache.thrift.protocol.TField("menuLogo", org.apache.thrift.protocol.TType.STRING, (short)4);

  public int id; // required
  public String name; // required
  public List<MenuPage> pages; // required
  public String menuLogo; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ID((short)1, "id"),
    NAME((short)2, "name"),
    PAGES((short)3, "pages"),
    MENU_LOGO((short)4, "menuLogo");

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
        case 2: // NAME
          return NAME;
        case 3: // PAGES
          return PAGES;
        case 4: // MENU_LOGO
          return MENU_LOGO;
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
    tmpMap.put(_Fields.NAME, new org.apache.thrift.meta_data.FieldMetaData("name", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PAGES, new org.apache.thrift.meta_data.FieldMetaData("pages", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, MenuPage.class))));
    tmpMap.put(_Fields.MENU_LOGO, new org.apache.thrift.meta_data.FieldMetaData("menuLogo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Menu.class, metaDataMap);
  }

  public Menu() {
  }

  public Menu(
    int id,
    String name,
    List<MenuPage> pages,
    String menuLogo)
  {
    this();
    this.id = id;
    setIdIsSet(true);
    this.name = name;
    this.pages = pages;
    this.menuLogo = menuLogo;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Menu(Menu other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.id = other.id;
    if (other.isSetName()) {
      this.name = other.name;
    }
    if (other.isSetPages()) {
      List<MenuPage> __this__pages = new ArrayList<MenuPage>();
      for (MenuPage other_element : other.pages) {
        __this__pages.add(new MenuPage(other_element));
      }
      this.pages = __this__pages;
    }
    if (other.isSetMenuLogo()) {
      this.menuLogo = other.menuLogo;
    }
  }

  public Menu deepCopy() {
    return new Menu(this);
  }

  @Override
  public void clear() {
    setIdIsSet(false);
    this.id = 0;
    this.name = null;
    this.pages = null;
    this.menuLogo = null;
  }

  public int getId() {
    return this.id;
  }

  public Menu setId(int id) {
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

  public String getName() {
    return this.name;
  }

  public Menu setName(String name) {
    this.name = name;
    return this;
  }

  public void unsetName() {
    this.name = null;
  }

  /** Returns true if field name is set (has been assigned a value) and false otherwise */
  public boolean isSetName() {
    return this.name != null;
  }

  public void setNameIsSet(boolean value) {
    if (!value) {
      this.name = null;
    }
  }

  public int getPagesSize() {
    return (this.pages == null) ? 0 : this.pages.size();
  }

  public java.util.Iterator<MenuPage> getPagesIterator() {
    return (this.pages == null) ? null : this.pages.iterator();
  }

  public void addToPages(MenuPage elem) {
    if (this.pages == null) {
      this.pages = new ArrayList<MenuPage>();
    }
    this.pages.add(elem);
  }

  public List<MenuPage> getPages() {
    return this.pages;
  }

  public Menu setPages(List<MenuPage> pages) {
    this.pages = pages;
    return this;
  }

  public void unsetPages() {
    this.pages = null;
  }

  /** Returns true if field pages is set (has been assigned a value) and false otherwise */
  public boolean isSetPages() {
    return this.pages != null;
  }

  public void setPagesIsSet(boolean value) {
    if (!value) {
      this.pages = null;
    }
  }

  public String getMenuLogo() {
    return this.menuLogo;
  }

  public Menu setMenuLogo(String menuLogo) {
    this.menuLogo = menuLogo;
    return this;
  }

  public void unsetMenuLogo() {
    this.menuLogo = null;
  }

  /** Returns true if field menuLogo is set (has been assigned a value) and false otherwise */
  public boolean isSetMenuLogo() {
    return this.menuLogo != null;
  }

  public void setMenuLogoIsSet(boolean value) {
    if (!value) {
      this.menuLogo = null;
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

    case NAME:
      if (value == null) {
        unsetName();
      } else {
        setName((String)value);
      }
      break;

    case PAGES:
      if (value == null) {
        unsetPages();
      } else {
        setPages((List<MenuPage>)value);
      }
      break;

    case MENU_LOGO:
      if (value == null) {
        unsetMenuLogo();
      } else {
        setMenuLogo((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return Integer.valueOf(getId());

    case NAME:
      return getName();

    case PAGES:
      return getPages();

    case MENU_LOGO:
      return getMenuLogo();

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
    case NAME:
      return isSetName();
    case PAGES:
      return isSetPages();
    case MENU_LOGO:
      return isSetMenuLogo();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Menu)
      return this.equals((Menu)that);
    return false;
  }

  public boolean equals(Menu that) {
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

    boolean this_present_name = true && this.isSetName();
    boolean that_present_name = true && that.isSetName();
    if (this_present_name || that_present_name) {
      if (!(this_present_name && that_present_name))
        return false;
      if (!this.name.equals(that.name))
        return false;
    }

    boolean this_present_pages = true && this.isSetPages();
    boolean that_present_pages = true && that.isSetPages();
    if (this_present_pages || that_present_pages) {
      if (!(this_present_pages && that_present_pages))
        return false;
      if (!this.pages.equals(that.pages))
        return false;
    }

    boolean this_present_menuLogo = true && this.isSetMenuLogo();
    boolean that_present_menuLogo = true && that.isSetMenuLogo();
    if (this_present_menuLogo || that_present_menuLogo) {
      if (!(this_present_menuLogo && that_present_menuLogo))
        return false;
      if (!this.menuLogo.equals(that.menuLogo))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(Menu other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Menu typedOther = (Menu)other;

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
    lastComparison = Boolean.valueOf(isSetName()).compareTo(typedOther.isSetName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.name, typedOther.name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPages()).compareTo(typedOther.isSetPages());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPages()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pages, typedOther.pages);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMenuLogo()).compareTo(typedOther.isSetMenuLogo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMenuLogo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.menuLogo, typedOther.menuLogo);
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
        case 2: // NAME
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.name = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 3: // PAGES
          if (field.type == org.apache.thrift.protocol.TType.LIST) {
            {
              org.apache.thrift.protocol.TList _list16 = iprot.readListBegin();
              this.pages = new ArrayList<MenuPage>(_list16.size);
              for (int _i17 = 0; _i17 < _list16.size; ++_i17)
              {
                MenuPage _elem18; // required
                _elem18 = new MenuPage();
                _elem18.read(iprot);
                this.pages.add(_elem18);
              }
              iprot.readListEnd();
            }
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 4: // MENU_LOGO
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.menuLogo = iprot.readString();
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
    if (this.name != null) {
      oprot.writeFieldBegin(NAME_FIELD_DESC);
      oprot.writeString(this.name);
      oprot.writeFieldEnd();
    }
    if (this.pages != null) {
      oprot.writeFieldBegin(PAGES_FIELD_DESC);
      {
        oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, this.pages.size()));
        for (MenuPage _iter19 : this.pages)
        {
          _iter19.write(oprot);
        }
        oprot.writeListEnd();
      }
      oprot.writeFieldEnd();
    }
    if (this.menuLogo != null) {
      oprot.writeFieldBegin(MENU_LOGO_FIELD_DESC);
      oprot.writeString(this.menuLogo);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Menu(");
    boolean first = true;

    sb.append("id:");
    sb.append(this.id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("name:");
    if (this.name == null) {
      sb.append("null");
    } else {
      sb.append(this.name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("pages:");
    if (this.pages == null) {
      sb.append("null");
    } else {
      sb.append(this.pages);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("menuLogo:");
    if (this.menuLogo == null) {
      sb.append("null");
    } else {
      sb.append(this.menuLogo);
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

