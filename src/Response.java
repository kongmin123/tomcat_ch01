
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/*
    HTTP Response = Status-Line
      *(( general-header | response-header | entity-header ) CRLF)
      CRLF
      [ message-body ]
      Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
 */

/**
 * HTTP响应
 */
public class Response {
    /**
     * 私有静态常量int BUFFER_SIZE
     */
    private static final int BUFFER_SIZE = 1024;

    /**
     * HTTP请求
     */
    Request request;

    /**
     * 输出
     */
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    /**
     * 设置HTTP请求
     *
     * @param request
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * 发送静态资源
     *
     * @throws IOException
     */
    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;

        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            System.out.println("======= 发送至浏览器[客户端] 的静态资源 :\n" + file.getPath());

            // 文件存在
            if (file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            }
            // 文件未找到
            else {
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

//    public void setRequest(Request request) {
//
//    }
}
