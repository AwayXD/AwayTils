package xyz.awayxd.awaytils.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;

public final class HttpUtils {
    public static final HttpUtils INSTANCE = new HttpUtils();
    private static final String DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";

    private HttpUtils() {}

    private HttpURLConnection make(String url, String method, String agent) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        if (!(urlConnection instanceof HttpURLConnection)) {
            throw new IllegalArgumentException("Connection is not an HttpURLConnection");
        }
        HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
        httpConnection.setRequestMethod(method);
        httpConnection.setConnectTimeout(2000);
        httpConnection.setReadTimeout(10000);
        httpConnection.setRequestProperty("User-Agent", agent);
        httpConnection.setInstanceFollowRedirects(true);
        httpConnection.setDoOutput(true);
        return httpConnection;
    }

    public String request(String url, String method, String agent) throws IOException {
        if (url == null || method == null || agent == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        HttpURLConnection connection = make(url, method, agent);
        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        StringBuilder content = new StringBuilder();
        char[] buffer = new char[1024];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            content.append(buffer, 0, read);
        }
        reader.close();
        return content.toString();
    }

    public static String get(String url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        return INSTANCE.request(url, "GET", DEFAULT_AGENT);
    }

    public static void download(String url, File file) throws IOException {
        if (url == null || file == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        HttpURLConnection connection = INSTANCE.make(url, "GET", DEFAULT_AGENT);
        InputStream inputStream = connection.getInputStream();
        FileUtils.copyInputStreamToFile(inputStream, file);
        inputStream.close();
    }

    static {
        HttpURLConnection.setFollowRedirects(true);
    }
}
