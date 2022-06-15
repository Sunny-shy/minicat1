package server;

/**
 * @author ADMIN
 * @date 2022/6/14 0:20
 */
public class HttpProtocolUtil {
    /**
     * 为响应码200 请求头信息
     */
    public static String getHttpHeader200(long contentLength) {
        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/html;charset=utf-8 \n" +
                "Content-Length: " + contentLength + " \n" +
                "\r\n";
    }
    /**
     * 为响应码404 请求头信息
     */
    public static String getHttpHeader404() {
        String str404="<h1>404 not found</h1>";
        return "HTTP/1.1 404 not found \n" +
                "Content-Type: text/html;charset=utf-8 \n" +
                "Content-Length: " + str404.getBytes().length + " \n" +
                "\r\n" +str404;
    }
}
