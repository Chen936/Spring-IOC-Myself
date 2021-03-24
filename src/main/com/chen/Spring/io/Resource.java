package main.com.chen.Spring.io;

import java.io.InputStream;

public interface Resource {
    //抽象出资源的概念，即输入流
    InputStream getInputStream() throws Exception;
}
