package server;

import java.io.IOException;
import java.io.InputStream;

/**
 * 把请求信息封装为Request对象(根据InputStream)
 * @author ADMIN
 * @date 2022/6/14 9:36
 */
public class Request {
    /**
     * 请求方式
     */

    private String method;
    /**
     * 请求url
     */
    private String url;
    /**
     * 其他信息从输入流取出来
     */
    private InputStream inputStream;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request()  {

    }
        /**
         * 构造器 输入流解析
         */
    public Request(InputStream inputStream) throws IOException {
            this.inputStream = inputStream;
            //网络原因 available可能为空
            int count = 0;
            while (count == 0) {
                count = inputStream.available();
                ;
            }
            byte[] bytes = new byte[count];
            inputStream.read(bytes);
            String inputStr = new String(bytes);
            //获取第一行
            String firstLineStr = inputStr.split("\\n")[0];
            //   firstLineStr  GET / HTTP/1.1
            String[] firstLineSpilt = firstLineStr.split(" ");
            this.method = firstLineSpilt[0];
            this.url = firstLineSpilt[1];
            System.out.println("method==>" + method);
            System.out.println("url==>" + url);
        }
    }
