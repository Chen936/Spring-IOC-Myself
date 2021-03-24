package main.com.chen.main.Service.ServiceImpl;

import main.com.chen.Spring.Annotation.Component;
import main.com.chen.Spring.Annotation.Scope;
import main.com.chen.Spring.Annotation.Value;
import main.com.chen.main.Service.HelloService;

@Component(name = "HelloService")
@Scope("prototype")
public class HelloServiceImpl implements HelloService {
    @Value("hello world")
    private String text;

    @Override
    public void doSomething() {
        System.out.println(text);
    }

    @Override
    public String getText() {
        return text;
    }

}
