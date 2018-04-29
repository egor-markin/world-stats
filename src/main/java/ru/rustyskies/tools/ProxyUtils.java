package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @since 29.04.2018
 * @author Egor Markin
 */
@Slf4j
@UtilityClass
public class ProxyUtils {

    public void enableSocksProxy() {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", "127.0.0.1");
        System.getProperties().put("socksProxyPort", "8888");
    }

}
