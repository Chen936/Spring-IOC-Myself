package main.com.chen.Spring.context;

import main.com.chen.Spring.entity.BeanDefinition;
import main.com.chen.Spring.factory.AbstractBeanFactory;
import main.com.chen.Spring.factory.AutowiredCapableBeanFactory;
import main.com.chen.Spring.io.ResourceLoader;
import main.com.chen.Spring.reader.Xml_Annotation_BeanDefinitionReader;

import java.util.Map;

//具体实现类所要做的，创建一个 BeanDefinitionReader 读取配置文件，
// 并且将读取到的配置存到 BeanFactory 中，并且由 BeanFactory 创建对应的实例对象即可
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    //对象锁保证并发安全
    private final Object startupShutdownMonitor = new Object();
    private String location;
    public ClassPathXmlApplicationContext(String location) throws Exception {
        super();  //父类构造方法
        this.location = location;
        refresh(); //调用 refresh 方法，用于刷新 bean，即第一次从文件中读取并解析注入
    }

    public void refresh() throws Exception {
        synchronized (startupShutdownMonitor) {
            AbstractBeanFactory beanFactory = obtainBeanFactory();  //责读取并解析文件生成 BeanDefinition，并形成 beanFactory
            prepareBeanFactory(beanFactory);                   //根据 BeanDefinition 实例化对象，调用了beanFactory 的 populateBeans 方法
            this.beanFactory = beanFactory;
        }
    }

    private AbstractBeanFactory obtainBeanFactory() throws Exception {
        Xml_Annotation_BeanDefinitionReader xmlBeanDefinitionReader = new Xml_Annotation_BeanDefinitionReader(new ResourceLoader());
        xmlBeanDefinitionReader.loadBeanDefinition(location);
        AbstractBeanFactory autowiredCapableBeanFactory = new AutowiredCapableBeanFactory();
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : xmlBeanDefinitionReader.getRegistry().entrySet()
        ) {
            //将读取的文件注册进工厂
            autowiredCapableBeanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
        return autowiredCapableBeanFactory;
    }

    private void prepareBeanFactory(AbstractBeanFactory beanFactory) throws Exception {
        beanFactory.populateBeans();
    }
    public void addNewBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
//        添加新的bean解析类，调用reader的注入属性方法并注册进beanfactory
        Xml_Annotation_BeanDefinitionReader.processAnnotationProperty(beanDefinition.getBeanClass(), beanDefinition);
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }

    public void refreshBeanFactory() throws Exception {
//        手动刷新，满足添加bean后的加载需要
        prepareBeanFactory((AbstractBeanFactory) beanFactory);
    }
}
