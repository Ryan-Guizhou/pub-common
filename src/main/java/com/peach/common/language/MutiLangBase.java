package com.peach.common.language;

import cn.hutool.setting.dialect.Props;
import com.peach.common.context.CurrentContext;
import com.peach.common.util.CaffeineUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/6/21 1:22
 */
@Slf4j
public class MutiLangBase {


    /**
     * 响应多语言 方法已过期 请使用方法{@link #getResponseMultiLangBase(String)}
     *
     * @param keyWord  keyWord
     * @param language language
     * @return String
     */
    @Deprecated
    public static String getResponseMultiLangBase(String keyWord, String language) {

        return getMultiLangBase(keyWord, language, "i18n.response");
    }

    public static String getResponseMultiLangBase(String keyWord) {

        return getMultiLangBase(keyWord, CurrentContext.getLanguage(), "i18n.response");
    }

    @Deprecated
    public static String getCommonMultiLangBase(String keyWord, String language) {

        return getMultiLangBase(keyWord, language, "i18n.common");
    }

    public static String getCommonMultiLangBase(String keyWord) {
        return getMultiLangBase(keyWord, CurrentContext.getLanguage(), "i18n.common");
    }

    private static String getMultiLangBase(String keyWord, String language, String type) {
        String country = Locale.SIMPLIFIED_CHINESE.getCountry();

        if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(language)) {
            country = Locale.SIMPLIFIED_CHINESE.getCountry();
        } else if (Locale.US.getLanguage().equals(language)) {
            country = Locale.US.getCountry();
        } else if (Locale.JAPAN.getLanguage().equals(language)) {
            country = Locale.JAPAN.getCountry();
        } else {
            language = Locale.SIMPLIFIED_CHINESE.getLanguage();
        }
        String lan = null;
        try {
            String fileName = type.replace(".", "/") + "_" + language + "_" + country + ".properties";
            Props i18nProps = CaffeineUtil.get(type, type + "." + language, key -> new Props(fileName, StandardCharsets.UTF_8));
            if (i18nProps != null) {
                lan = (String) i18nProps.get(keyWord);
            }
        } catch (Exception e) {
            log.error("getMultiLangBase error" + e.getMessage());
        }
        return lan;
    }
}
