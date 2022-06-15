package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 获取静态资源文件的绝对路径
 *
 * @author ADMIN
 * @date 2022/6/14 12:34
 */
public class StaticResourceUtil {
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        return absolutePath.replaceAll("\\\\", "/") + path;
    }

    /**
     * 读取静态资源文件,通过输出流输出
     *
     * @param inputStream
     * @param outputStream
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count=0;
        while (count == 0) {
            count = inputStream.available();
        }
        int resourceSize = count;
        //输出http请求头
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes(StandardCharsets.UTF_8));
        //读取内容输出
        //已读取的
        long written = 0;
        //计划每次缓冲的
        int byteSize = 1024;
        byte[] bytes = new byte[byteSize];
        while (written < resourceSize) {
            //说明剩余未读取大小不足1024 ,就按真实长度处理
            if (written+byteSize>resourceSize) {
                byteSize = (int) (resourceSize - written);
                //剩余文件长度
                bytes = new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written += byteSize;
        }
    }
}
