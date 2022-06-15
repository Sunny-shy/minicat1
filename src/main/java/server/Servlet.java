package server;

import java.io.IOException;

/**
 * @author ADMIN
 * @date 2022/6/14 20:12
 */
public interface Servlet {
    void init()throws Exception;
    void destory()throws Exception;
    void service(Request request, Response response) throws Exception;
}
