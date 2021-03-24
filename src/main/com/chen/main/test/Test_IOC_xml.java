package main.com.chen.main.test;

import main.com.chen.Spring.context.ApplicationContext;
import main.com.chen.Spring.context.ClassPathXmlApplicationContext;
import main.com.chen.main.Service.HelloService;
import main.com.chen.main.Service.ServiceImpl.WrapServiceImpl;
import main.com.chen.main.Service.WrapService;

public class Test_IOC_xml {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("main/resources/application.xml");
        WrapServiceImpl wrapService = (WrapServiceImpl) applicationContext.getBean("WrapService");
        wrapService.doIt();
        HelloService helloWorldService = (HelloService) applicationContext.getBean("HelloService");
        HelloService helloWorldService2 = (HelloService) applicationContext.getBean("HelloService");
        System.out.println("prototype验证：" + (helloWorldService == helloWorldService2));
        WrapService wrapService2 = (WrapService) applicationContext.getBean("WrapService");
        System.out.println("singleton验证：" + (wrapService == wrapService2));

    }
}
