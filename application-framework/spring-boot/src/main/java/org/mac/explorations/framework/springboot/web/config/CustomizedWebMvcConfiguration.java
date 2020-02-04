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

package org.mac.explorations.framework.springboot.web.config;

import org.mac.explorations.framework.springboot.web.interceptor.SimpleAccessControlInterceptor;
import org.springframework.boot.web.server.AbstractConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @auther mac
 * @date 2020-02-02 15:58
 */
@Configuration
public class CustomizedWebMvcConfiguration implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/main.html").setViewName("main");
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index").setViewName("login");
        registry.addViewController("/sign").setViewName("login");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SimpleAccessControlInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/sign","/","/index","/**/favicon.ico","/asserts/**","/webjars/**","/index.html");
    }

    @Bean
    public WebServerFactoryCustomizer iWebServerFactoryCustomizer() {
        return factory -> {
            if (factory instanceof AbstractConfigurableWebServerFactory){
                AbstractConfigurableWebServerFactory webServerFactory = (AbstractConfigurableWebServerFactory)factory;
                webServerFactory.setPort(8888);
            }
        };
    }

    /**注册Servlet相关组件*/
    //@Bean
   /* public ServletRegistrationBean<PlainHttpServlet> servletBean(ApplicationContext applicationContext){
        ServletRegistrationBean<PlainHttpServlet> servletRegistrationBean = new ServletRegistrationBean<>();
        servletRegistrationBean.addUrlMappings("/servlet");
        servletRegistrationBean.setServlet(applicationContext.getBean(PlainHttpServlet.class));
        return servletRegistrationBean;
    }*/

    //@Bean
    /*public FilterRegistrationBean<SimpleFilter> filterBean(ApplicationContext applicationContext){
        FilterRegistrationBean<SimpleFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.addUrlPatterns("/servlet");
        filterRegistrationBean.setFilter(applicationContext.getBean(SimpleFilter.class));
        return filterRegistrationBean;
    }*/

    //@Bean
    /*public ServletListenerRegistrationBean<SimpleServletContextListener> listenerRegistrationBean(ApplicationContext applicationContext){
        ServletListenerRegistrationBean<SimpleServletContextListener> listenerRegistrationBean = new ServletListenerRegistrationBean();
        listenerRegistrationBean.setListener(applicationContext.getBean(SimpleServletContextListener.class));
        return listenerRegistrationBean;
    }*/
}
