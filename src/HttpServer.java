
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Http服务器
 */
public class HttpServer {

    /**
        WEB_ROOT is the directory where our HTML and other files reside.
        For this package, WEB_ROOT is the "webroot" directory under the working directory.
        The working directory is the location in the file system from where the java command was invoked.
     */
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "webroot";

    // 关机 命令
    private static final String SHUTDOWN_COMMAND = "/SHUNTDOWN";

    // 收到的关机命令
    private boolean shutdown = false;

    /**
     * 主方法
     * @param args
     */
    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    /**
     * 等待
     */
    public void await() {
        System.out.println("============== HTTP服务器 运行中...... ==================");

        ServerSocket serverSocket = null;
        int port = 8080; // 端口
        int backlog = 1; // 请求传入连接队列的最大长度。
        String host = "127.0.0.1"; // 服务器ip
        try {
            // 1 创建一个ServerSocket实例，然后进入一个while循环
            serverSocket = new ServerSocket(port,backlog, InetAddress.getByName(host));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // 循环等待一个请求
        while (!shutdown){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;

            try {
                // 2 当从8080端口接收到HTTP请求后，ServerSocket类的accept()方法返回，等待结束
                socket = serverSocket.accept();
                // 2.1 从accept()方法返回的Socket实例中获取InputStream对象 和 OutputStream对象
                input = socket.getInputStream();
                output = socket.getOutputStream();

                // 3 创建一个Request对象，并调用其parse()方法来解析HTTP请求的原始数据
                // 创建请求对象和解析
                Request request = new Request(input);
                request.parse();

                // 4 创建一个Response对象，并分别调用其setRequest()方法和sendStaticResource()方法
                // 创建响应对象
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();

                // 5 关闭套接字
                // 关闭套接字
                socket.close();

                // 5.1 调用Request类的getUri()方法来测试HTTP请求的URI是否是关机命令
                // 检查上一个URI是否是关机命令
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
