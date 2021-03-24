package main.com.chen.Spring.io;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlResource implements Resource {
    private URL url;   //是统一资源定位符，可以表示网络路径，也可以表示本地的文件路径
    public  UrlResource(URL url){
        this.url=url;
    }
    @Override
    public InputStream getInputStream() throws Exception {
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return urlConnection.getInputStream();
    }
}
