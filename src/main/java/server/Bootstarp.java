package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ADMIN
 * @date 2022/6/14 0:04
 * 1）提供服务，接收请求（Socket通信）
 * 2）请求信息封装成Request对象（Response对象）
 * 3）客户端请求资源，资源分为静态资源（html）和动态资源（Servlet）
 * 4）资源返回给客户端浏览器
 */
public class Bootstarp {


    /**
     * 定义socket监听的端口号8086
     */
    private int port = 8086;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    //线城池参数
    public long keepAliveTime = 100L;
   public BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);

    public ThreadFactory threadFactory = Executors.defaultThreadFactory();
    public RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();


    /**
     * minicat启动需要的初始化的工作
     */
    public void start() throws IOException {
        //完成minicat1.0版本(浏览器访问http://localhost:8086)返回固定字符串
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>minicat start on port: " + port);
//        while (true) {
//            Socket socket = serverSocket.accept();
//            //有了  socket ,接收到请求
//            String data = "Hello Minicat!";
//            String responseText = HttpProtocolUtil.getHttpHeader200(data.length())+data;
//            OutputStream outputStream = socket.getOutputStream();
//            outputStream.write(responseText.getBytes(StandardCharsets.UTF_8));
//            socket.close();
//        }
        //完成minicat 2.0版本 封装request和response对象,返回html静态文件
//        while (true) {
//            Socket socket = serverSocket.accept();
//            //获取输入流
//            InputStream inputStream = socket.getInputStream();
//            //封装request和response对象
//            Request request = new Request(inputStream);
//            Response response = new Response(socket.getOutputStream());
//            response.outputHtml(request.getUrl());
//            socket.close();
//        }
//完成minicat 3.0版本
        // 可以请求动态资源(servlet)
        //加载web.xml,初始化servlet
        loadServlet();
//        while (true) {
//            Socket socket = serverSocket.accept();
//            InputStream inputStream = socket.getInputStream();
//            Request request = new Request(inputStream);
//            Response response = new Response(socket.getOutputStream());
//            String url = request.getUrl();
//            //判断是否动静态资源
//            if (servletMap.get(url) == null) {
//                response.outputHtml(url);
//            } else {
//                HttpServlet httpServlet = servletMap.get(url);
//                try {
//                    httpServlet.service(request,response);
////                    loadServlet();
//                } catch (Exception e ) {
//                    throw new RuntimeException(e);
//                }
//            }
//            socket.close();
//        }
        //多线程改造
//        while (true) {
//            Socket socket = serverSocket.accept();
//            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
//            requestProcessor.start();
//        }
        //使用多线程,线程池
        //定义一个线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 120L, TimeUnit.SECONDS,workQueue,threadFactory,rejectedExecutionHandler);
        while (true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
            threadPoolExecutor.execute(requestProcessor);
//            requestProcessor.start();
        }
    }


    private Map<String, HttpServlet> servletMap = new HashMap<>(0);

    /**
     * 加载web.xml,初始化servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.selectNodes("//servlet");
            for (Element element : list) {
                Element servletName = (Element) element.selectSingleNode("servlet-name");
                String servletNameStringValue = servletName.getStringValue();
                Element servletClass = (Element) element.selectSingleNode("servlet-class");
                String servletClassStringValue = servletClass.getStringValue();
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletNameStringValue + "']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClassStringValue).newInstance());

            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Minicat 的主程序  程序入口
     *
     * @param args
     */
    public static void main(String[] args) {
        Bootstarp bootstarp = new Bootstarp();
        try {
            bootstarp.start();
        } catch (IOException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }
}
