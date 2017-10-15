package rcf;

import com.google.common.base.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author Michael Lieshoff
 */
public class Crawler {

    private static final Logger LOG = LogManager.getLogger(Crawler.class);

    private boolean cached;

    private File tmpDir = new File(System.getProperty("java.io.tmpdir"));

    public Crawler() {
        if (Boolean.valueOf(System.getProperty("com.bytediscover.ta.lib.web.Crawler.cached", "false"))) {
            cached = true;
        }
    }

    public Crawler(boolean cached) {
        this.cached = cached;
    }

    public String post(String url) throws IOException {
        LOG.debug("post> " + url);
        Optional<String> optional = tryGetFromCache(url);
        if (optional.isPresent()) {
            return optional.get();
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder s = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            s.append(line);
        }
        tryPutInCache(url, s.toString());
        return s.toString();
    }

    private void tryPutInCache(String url, String content) throws IOException {
        tryPutInCache(url, null, content);
    }

    private void tryPutInCache(String url, String body, String content) throws IOException {
        if (cached) {
            int hash = url.hashCode();
            if (body != null) {
                hash += body.hashCode();
            }
            if (!tmpDir.exists()) {
                tmpDir.mkdirs();
            }
            FileUtils.write(new File(tmpDir, hash + ".cache"), content);
        }
    }

    private Optional<String> tryGetFromCache(String url) throws IOException {
        return tryGetFromCache(url , null);
    }

    private Optional<String> tryGetFromCache(String url, String body) throws IOException {
        if (cached) {
            int hash = url.hashCode();
            if (body != null) {
                hash += body.hashCode();
            }
            File file = new File(tmpDir, hash  + ".cache");
            if (file.exists()) {
                LOG.debug("from cache> "  + file.getName());
                return Optional.of(FileUtils.readFileToString(file));
            }
        }
        return Optional.absent();
    }

    public String post(String url, String body) throws IOException {
        return post(url, body, "text/xml", null);
    }

    public String post(String url, String body, String contentType, Map<String, String> headers) throws IOException {
        LOG.debug("post with body> " + url);
        LOG.debug("          body>\n" + body);
        Optional<String> optional = tryGetFromCache(url, body);
        if (optional.isPresent()) {
            return optional.get();
        }
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(body);
        stringEntity.setContentType(contentType);
        request.addHeader("Content-Type", contentType);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                request.addHeader(key, value);
            }
        }
        request.setEntity(stringEntity);
        HttpResponse response = client.execute(request);
        // handle response codes
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder s = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            s.append(line);
        }
        tryPutInCache(url, body, s.toString());
        LOG.debug("          response>\n" + s.toString());
        return s.toString();
    }

    public String get(String url) throws IOException {
        LOG.debug("get> " + url);
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder s = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            s.append(line);
        }
        return s.toString();
    }

    public byte[] getBytes(String url) throws IOException {
        LOG.debug("get bytes> " + url);
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        return IOUtils.toByteArray(response.getEntity().getContent());
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Crawler().get("http://www.webservicex.net/globalweather.asmx/GetWeather?CityName=berlin&CountryName=Germany"));
    }

}

