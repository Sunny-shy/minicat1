package server;

import java.io.IOException;

/**
 * @author ADMIN
 * @date 2022/6/14 20:20
 */
public class LaGouServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response)  {
        String content = "<h1>LaGouServlet get</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>LaGouServlet post</h1>";
        try {
            response.output(HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void destory() {

    }
}
