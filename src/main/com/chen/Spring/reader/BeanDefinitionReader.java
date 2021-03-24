package main.com.chen.Spring.reader;

public interface BeanDefinitionReader {
//    读取器接口，用于读取beanDefinition
    void loadBeanDefinition(String location) throws Exception;
}
