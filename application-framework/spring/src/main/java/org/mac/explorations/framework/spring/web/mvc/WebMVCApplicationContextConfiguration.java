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

package org.mac.explorations.framework.spring.web.mvc;

import org.mac.explorations.framework.spring.web.mvc.async.SimpleAsyncHandlerInterceptor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringMVC配置
 *
 * 1.开启@EnableWebMvc
 *   <pre>
 *       @Import(DelegatingWebMvcConfiguration.class)
 *       public @interface EnableWebMvc {
 *       }
 *   </pre>
 *   @see org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
 *
 *   即注册了SpringMVC的默认配置 并自动注入已注册的自定义配置
 *   @see org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration#setConfigurers(java.util.List)
 *   <pr>
 *       @Autowired(required = false)
 *       public void setConfigurers(List<WebMvcConfigurer> configurers) {
 *           if (!CollectionUtils.isEmpty(configurers)) {
 *               this.configurers.addWebMvcConfigurers(configurers);
 *           }
 *       }
 *   </pr>
 *
 * 2.实现@see {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer}接口
 *   自定义相关配置
 *
 * @auther mac
 * @date 2020-01-16
 */
@ComponentScan(
        basePackages = {"org.mac.explorations.framework.spring.web.mvc"},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})}
)
@EnableWebMvc
public class WebMVCApplicationContextConfiguration  implements WebMvcConfigurer {

    //视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    //静态资源访问
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SimpleAsyncHandlerInterceptor()).addPathPatterns("/**");
    }
}
