package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author ADMIN
 * @date 2022/6/15 0:33
 */
public class RequestProcessor extends Thread {
    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.servletMap = servletMap;
        this.socket = socket;
    }

    public RequestProcessor() {

    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            String url = request.getUrl();
            //判断是否动静态资源
            if (servletMap.get(url) == null) {
                response.outputHtml(url);
            } else {
                HttpServlet httpServlet = servletMap.get(url);
                httpServlet.service(request, response);
            }
            socket.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}
