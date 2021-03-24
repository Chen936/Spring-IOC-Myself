package main.com.chen.Spring.entity;

public class BeanDefinition {
    private Object bean; // 实例化后的对象
    private Class beanClass;
    private String beanClassName;
    private Boolean singleton; // 是否是单例模式
    private PropertyValues propertyValues; // Bean的属性集

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
        try {
            this.beanClass= Class.forName(beanClassName);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(Boolean singleton) {
        this.singleton = singleton;
    }

    public PropertyValues getPropertyValues() {
        if (propertyValues==null)
            propertyValues=new PropertyValues();
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
