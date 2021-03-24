package main.com.chen.Spring.factory;

import main.com.chen.Spring.entity.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory implements BeanFactory {
//    根据name取出bean且保证并发安全，选择并发包下的ConcurrentHashMap
    ConcurrentHashMap<String, BeanDefinition> beanmap=new ConcurrentHashMap<>();
    @Override

    public Object getBean(Class c) throws Exception {
//        bean的解析类，包含了要注入bean的具体信息
        BeanDefinition beanDefinition=null;
        for (Map.Entry<String,BeanDefinition> entry:beanmap.entrySet()){
            Class aClass= entry.getValue().getBeanClass();
            if (aClass==c||c.isAssignableFrom(aClass)){
                beanDefinition=entry.getValue();
            }
            if (beanDefinition==null)return null;
//   如果是 singleton 的 bean，在 BeanDefinition 对象生成时，就已经生成好了对应的实例，在 getBean 获取的时候就可以直接获取，否则就需要调用 doCreateBean 方法来即时创建一个实例
            if (!beanDefinition.isSingleton()||beanDefinition.getBean()==null){
                return doCreateBean(beanDefinition);
            }else {
                return beanDefinition.getBean();
            }

        }
        return null;
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition = beanmap.get(beanName);
        if (beanDefinition==null)return null;
        if (!beanDefinition.isSingleton()||beanDefinition.getBean()==null){
            return doCreateBean(beanDefinition);
        }else {
            return beanDefinition.getBean();
        }
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bean) throws Exception {
                 beanmap.put(name, bean);
    }

//    doCreateBean 方法是一个抽象方法，表示真正创建 Bean 实例对象的操作，留给具体的实现类来实现
    public abstract Object doCreateBean(BeanDefinition beanDefinition) throws Exception;

//    对每个解析类创建出bean实例，初始化时用到
    public void populateBeans() throws Exception {
        for(Map.Entry<String, BeanDefinition> entry : beanmap.entrySet()) {
            doCreateBean(entry.getValue());
        }
    }
}
