package main.com.chen.Spring.io;

import java.net.URL;

public class ResourceLoader {
    //实际加载使用，只实现xml方式
    public Resource getResource(String location){
        //获得类加载器调用getResource方法从类路径加载
        URL url = this.getClass().getClassLoader().getResource(location);
        return new UrlResource(url);
    }
}
