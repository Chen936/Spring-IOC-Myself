package main.com.chen.Spring.context;

public interface ApplicationContext {
    public Object getBean(Class c) throws Exception;
    public Object getBean(String beanName) throws Exception;
}
