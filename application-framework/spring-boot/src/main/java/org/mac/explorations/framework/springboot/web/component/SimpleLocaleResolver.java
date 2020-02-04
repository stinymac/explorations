/*
 *          (          (
 *          )\ )  (    )\   )  )     (
 *  (  (   (()/( ))\( ((_| /( /((   ))\
 *  )\ )\   ((_))((_)\ _ )(_)|_))\ /((_)
 * ((_|(_)  _| (_))((_) ((_)__)((_|_))
 * / _/ _ \/ _` / -_|_-< / _` \ V // -_)
 * \__\___/\__,_\___/__/_\__,_|\_/ \___|
 *
 * 东隅已逝，桑榆非晚。(The time has passed,it is not too late.)
 * 虽不能至，心向往之。(Although I can't, my heart is longing for it.)
 *
 */

package org.mac.explorations.framework.springboot.web.component;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @auther mac
 * @date 2020-02-02 18:51
 */
@Component("localeResolver")
public class SimpleLocaleResolver implements LocaleResolver {

    public static final String LOCALE_PARAMETER_NAME = "locale";
    public static final String LANGUAGE_COUNTRY_CODE_SPLITTER = "_";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String localeParameter = request.getParameter(LOCALE_PARAMETER_NAME);

        String[] codes = StringUtils.delimitedListToStringArray(localeParameter,LANGUAGE_COUNTRY_CODE_SPLITTER);

        if(codes != null && codes.length > 1 ) {

            String language = codes[0];
            String country = codes[1];

            return new Locale(language, country);
        }

        return request.getLocale();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        /** @see org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver#setLocale(HttpServletRequest, HttpServletResponse, Locale)*/
        throw new UnsupportedOperationException(
                "Cannot change HTTP accept header - use a different locale resolution strategy");
    }
}
