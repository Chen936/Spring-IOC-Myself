package main.com.chen.Spring.factory;

import main.com.chen.Spring.entity.BeanDefinition;
import main.com.chen.Spring.entity.BeanReference;
import main.com.chen.Spring.entity.PropertyValue;

import java.lang.reflect.Field;
//自动注入属性的beanfactory-具体实现类
public class AutowiredCapableBeanFactory extends AbstractBeanFactory {
    @Override
    public Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
        if (beanDefinition.isSingleton()&&beanDefinition.getBean()!=null) {
            return beanDefinition.getBean();
        }
        Object bean=beanDefinition.getBeanClass().newInstance();
        if(beanDefinition.isSingleton()) {
            beanDefinition.setBean(bean);
        }
        applyPropertyValues(bean, beanDefinition);
        return bean;
    }
    void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
//        PropertyValues 实际上是一个 List，表示一组属性的定义，内部存储的对象是 PropertyValue 对象，
//        表示一个属性定义和其对应的注入属性：
        for(PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()) {
            Field field = bean.getClass().getDeclaredField(propertyValue.getName());
            Object value = propertyValue.getValue();
            if(value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) propertyValue.getValue();
                // 优先按照自定义名称匹配
                BeanDefinition refDefinition = beanmap.get(beanReference.getName());
                if(refDefinition != null) {
//                    如果发现某个 value 是 BeanReference 类型，即引用类型的，并且在之前没有被创建过，
//                    就需要再次调用 doCreateBean 方法来创建。实现递归注入引用类型及其属性
                    if(!refDefinition.isSingleton() || refDefinition.getBean() == null) {
                        value = doCreateBean(refDefinition);
                    } else {
                        value = refDefinition.getBean();
                    }
                } else {
                    // 按照类型匹配，返回第一个匹配的，主要用到反射创建类
                    Class clazz = Class.forName(beanReference.getName());
                    for(BeanDefinition definition : beanmap.values()) {
                        if(clazz.isAssignableFrom(definition.getBeanClass())) {
                            if(!definition.isSingleton() || definition.getBean() == null) {
                                value = doCreateBean(definition);
                            } else {
                                value = definition.getBean();
                            }
                        }
                    }
                }

            }
            if(value == null) {
                throw new RuntimeException("无法注入");
            }
//           防止无法访问private属性
            field.setAccessible(true);
            field.set(bean, value);
        }
    }
    }

