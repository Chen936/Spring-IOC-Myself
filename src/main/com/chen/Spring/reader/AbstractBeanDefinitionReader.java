package main.com.chen.Spring.reader;

import main.com.chen.Spring.entity.BeanDefinition;
import main.com.chen.Spring.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

//抽取公共方法
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    private Map<String, BeanDefinition> registry;   //registry 这个 Map，用来存储 Bean 的名称和 BeanDefinition 的映射。在完全读取完毕后会将 registry 返回出去，供 BeanFactory 使用
private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = new HashMap<>();
        this.resourceLoader = resourceLoader;
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
