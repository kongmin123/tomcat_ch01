
import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP请求
 */
public class Request {
    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    /**
     * 解析HTTP请求中的原始数据
     * 调用私有方法parseUri() 来解析HTTP请求的URI
     */
    public void parse() {
        // Read a set of characters from the socket
        StringBuffer requestStringBuffer = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];

        try {
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            requestStringBuffer.append((char) buffer[j]);
        }
        String requestString = requestStringBuffer.toString();
        System.out.println(requestString);
        uri = parseUri(requestString);
    }

    /**
     * 解析HTTP请求的URI，并存储至变量uri
     * @param requestString
     * @return
     */
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1){
            index2 = requestString.indexOf(' ', index1+1);
            if (index2 > index1){
                return requestString.substring(index1 +1, index2);
            }
        }
        return null;
    }

    /**
     * 返回HTTP请求的URI
     * @return
     */
    public String getUri() {
        return uri;
    }
}
