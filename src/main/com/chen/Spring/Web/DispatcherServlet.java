package main.com.chen.Spring.Web;

import main.com.chen.Spring.Annotation.Controller;
import main.com.chen.Spring.Annotation.RequestMapping;
import main.com.chen.Spring.context.ClassPathXmlApplicationContext;
import main.com.chen.Spring.entity.BeanDefinition;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class DispatcherServlet extends HttpServlet {
    private ClassPathXmlApplicationContext xmlApplicationContext;
    private Properties properties=new Properties();
    private List<String> classNames = new ArrayList<>();
//    hashset存不同class类
    private HashSet<Class> classes = new HashSet<>();
    //存放url和方法的映射
    private Map<String, Method> handlerMapping = new HashMap<>();
//    存放url和类的映射
    private Map<String, Object> controllerMap = new HashMap<>();
    @Override
    //调用init（）方法完成初始化工作
    public void init(ServletConfig config) throws ServletException {
        //Servlet 启动时会自动解析 web.xml 文件封装成 ServletConfig 类
        try {
//            初始化Spring容器读取配置文件，用于扫描目标包下所有的Controller并实例化
            xmlApplicationContext = new ClassPathXmlApplicationContext("application-annotation.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //读取启动配置，解析properties文件，存储到变量properties中
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //将包中所有类扫描并存到classNames中
        doScanner(properties.getProperty("scanPackage"));
//        遍历包下所有类找出Controller注解添加到容器里
        try {
            doInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        扫描对应Controller找出url应当由哪个方法处理
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        if (classes.isEmpty())return;
        try {
            for (Class<?> clazz:classes){
                String baseUrl="";
                if (clazz.isAnnotationPresent(RequestMapping.class)){
                    baseUrl=clazz.getAnnotation(RequestMapping.class).value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method:methods
                     ) {
                    if (method.isAnnotationPresent(RequestMapping.class)){
                        String url=baseUrl+method.getAnnotation(RequestMapping.class).value();
//                        进行类上和方法上的url拼接，确定完整请求url
                        url=(baseUrl+"/"+url).replaceAll("/+","/");
                        handlerMapping.put(url,method);
                        //存入url和controller类的映射
                        controllerMap.put(url,xmlApplicationContext.getBean(clazz));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doInstance() throws ClassNotFoundException {
        if (classNames.isEmpty()) return;
        for (String className:classNames){
            try {
//                反射获取类
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)){
                    classes.add(clazz);
                    BeanDefinition definition = new BeanDefinition();
                    definition.setSingleton(true);
                    definition.setBeanClassName(clazz.getName());
                    xmlApplicationContext.addNewBeanDefinition(clazz.getName(),definition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
//                手动刷新bean的配置，如果遇到新加的就会初始化
                xmlApplicationContext.refreshBeanFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    private void doScanner(String scanPackage) {
        //把所有的.替换成/
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        //获取File对象
        File dir = new File(url.getFile());
        for (File file:dir.listFiles()) {
            if (file.isDirectory()){
                //如果是目录递归扫描直到是文件
                doScanner(scanPackage+"."+file.getName());
            }else {
                String classname=scanPackage+"."+file.getName().replace(".class","");
                classNames.add(classname);
            }

        }
    }

    private void doLoadConfig(String location) {
        //把web.xml中的contextConfigLocation对应value值的文件加载到流里面
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(location);

        try {
            //加载Properties文件里的内容
            properties.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( resource!=null ) {
                try {//释放资源
                    resource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//    一个请求到来时到达 doGet() 和 doPost() 方法,这里实现一个 doDispatch() 方法来进行自定义处理
    public void doDispatcher(HttpServletRequest request, HttpServletResponse response)throws Exception{
        if (handlerMapping.isEmpty())return;
//        首先分离出请求的 URL 和请求参数，找到对应的方法后通过反射调用
        String url=request.getRequestURI();
        String contextPath = request.getContextPath();
//        把虚拟路径删掉留下符合要求的请求url（匹配requestmapping注解值）
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        //没有找到返回页面错误
        if (!handlerMapping.containsKey(url)) {
            response.getWriter().write("404 NOT FOUND!");
            return;
        }
        Method method = handlerMapping.get(url);
        Class<?>[] parameterTypes = method.getParameterTypes();
        //获得请求参数的map
        Map<String, String[]> parameterMap = request.getParameterMap();
//        反射调用方法传参的方式，是通过一个 Object 数组的方式传入参数的，按照方法定义参数的顺序，将值存放在数组中，在反射调用时将数组传入即可
        Object[] paramValues=new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            String requestParam = parameterTypes[i].getSimpleName(); //非全限定类名
            if (requestParam.equals("HttpServletRequest")){
                paramValues[i]=request;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")){
                paramValues[i]=response;
                continue;
            }
//            将 request 域中获取到的参数作为方法参数存入 paramValues 数组
            if (requestParam.equals("String")){
                for (Map.Entry<String,String[]> entry:parameterMap.entrySet()){
                    String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    paramValues[i]=value;
                }
            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//       直接调用dodispatch即可
        try{
            doDispatcher(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500:Server Exception");
            e.printStackTrace();
        }
    }
}
