package main.com.chen.main.Service.ServiceImpl;

import main.com.chen.Spring.Annotation.*;
import main.com.chen.main.Service.HelloService;
import main.com.chen.main.Service.WrapService;

@Component(name = "WrapService")
public class WrapServiceImpl implements WrapService {
    @Autowired
    private HelloService helloService;

    @Override
    public void doIt() {
        helloService.doSomething();
    }
}
