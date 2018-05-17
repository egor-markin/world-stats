package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @since 17.05.2018
 * @author Egor Markin
 */
@Slf4j
@UtilityClass
public class HttpUtils {

    public void getPageByUrl(String url, Consumer<InputStream> consumer) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            consumer.accept(entity.getContent());

            EntityUtils.consume(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    public String getPageByUrlAsString(String url) {
        AtomicReference<String> result = new AtomicReference<>();
        getPageByUrl(url, inputStream -> {
            try {
                result.set(IOUtils.toString(inputStream, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return result.get();
    }
}
