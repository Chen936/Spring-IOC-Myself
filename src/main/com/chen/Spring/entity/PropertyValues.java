package main.com.chen.Spring.entity;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
    private final List<PropertyValue> propertyValueList=new ArrayList<>();
//PropertyValues 实际上是一个 List，表示一组属性的定义，内部存储的对象是 PropertyValue 对象，表示一个属性定义和其对应的注入属性：
    public PropertyValues(){};
    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }
    public void addPropertyValue(PropertyValue propertyValue){
        propertyValueList.add(propertyValue);
    }
}
