package main.com.chen.main.Controller;

import main.com.chen.main.Service.HelloService;
import main.com.chen.Spring.Annotation.Autowired;
import main.com.chen.Spring.Annotation.Controller;
import main.com.chen.Spring.Annotation.RequestMapping;
import main.com.chen.Spring.Annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class testController {
//    注入一个service bean测试是否和Spring耦合
    @Autowired
    private HelloService helloService;

    @RequestMapping("/test1")
    public void test1(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("param") String param) {
        try {
            String text = helloService.getText();
            response.getWriter().write(text + " and the param is " + param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    测试结果：Hello world and the param is abc
}
