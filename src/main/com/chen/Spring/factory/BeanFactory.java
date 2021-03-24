package main.com.chen.Spring.factory;

import main.com.chen.Spring.entity.BeanDefinition;

public interface BeanFactory {
    public Object getBean(Class c) throws Exception;
    public Object getBean(String beanName) throws Exception;
//    定义向工厂中注册bean的定义
    public void registerBeanDefinition(String name, BeanDefinition bean) throws Exception;

}
