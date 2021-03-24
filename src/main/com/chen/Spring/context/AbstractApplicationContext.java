package main.com.chen.Spring.context;

import main.com.chen.Spring.factory.BeanFactory;

public abstract class AbstractApplicationContext implements ApplicationContext{
    /*
    * 仿照 Spring，编写一个抽象类 AbstractApplicationContext，来实现 ApplicationContext 接口，实现一些通用的方法。
    * 在Spring 中，ApplicationContext 实现 BeanFactory 接口的方式，是在 ApplicationContext 对象的内部，
    * 保存了一个 BeanFactory 对象的实例，
    * 实质上类似一种代理模式。
    * */
    BeanFactory beanFactory;

    @Override
    public Object getBean(Class c) throws Exception {
        return beanFactory.getBean(c);
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return beanFactory.getBean(beanName);
    }
}
