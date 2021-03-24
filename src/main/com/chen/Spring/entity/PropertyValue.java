package main.com.chen.Spring.entity;

public class PropertyValue {

    private final String name;
    private final Object value;  //若value是对象引用则为beanReference实例

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
