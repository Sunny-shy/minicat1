package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 根据outstream封装response
 * 该对象提供核心方法,输出html
 * @author ADMIN
 * @date 2022/6/14 9:37
 */
public class Response {
    private OutputStream outputStream;

    public Response() {

    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     *
     * @param path url,随后要根据url来获取到静态资源的绝对路径,进一步根据绝对路径读取该静态资源文件,
     *             最终通过输出流输出
     */
    public void outputHtml(String path) throws IOException {
        //获取静态资源文件的绝对路径
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);
        //输入静态资源文件
        File file = new File(absoluteResourcePath);
        if (file.exists()&&file.isFile()) {
            //读取静态静态资源,输出静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
        }else {
            //输出404
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }

    public void output(String content) throws IOException {
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));

    }
}
