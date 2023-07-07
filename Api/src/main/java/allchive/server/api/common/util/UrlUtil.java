package allchive.server.api.common.util;

import allchive.server.core.helper.SpringEnvironmentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static allchive.server.core.consts.AllchiveConst.*;

@Component
public class UrlUtil {
    private static SpringEnvironmentHelper springEnvironmentHelper;

    @Autowired
    private UrlUtil(SpringEnvironmentHelper springEnvironmentHelper) {
        UrlUtil.springEnvironmentHelper = springEnvironmentHelper;
    }

    public static String toAssetUrl(String key) {
        if (springEnvironmentHelper.isProdProfile()) {
            return PROD_ASSET_URL + key;
        }
        return STAGING_ASSET_URL + key;
    }

    public static String convertUrlToKey(String url) {
        if (validateUrl(url)) {
            return url.split("/", 4)[3];
        }
        return url;
    }

    private static Boolean validateUrl(String url) {
        return url.contains(STAGING_ASSET_URL) || url.contains(PROD_ASSET_URL) || url.contains(S3_ASSET_URL);
    }

}
