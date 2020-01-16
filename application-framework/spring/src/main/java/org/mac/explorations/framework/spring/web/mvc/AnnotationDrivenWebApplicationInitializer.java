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

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 注解驱动的spring-webmvc的使用
 *
 * Servlet3.0开始提供了ServletContainerInitializer用于注册Servlet组件
 * spring-web对其的实现为
 * @see /spring-web-5.2.2.RELEASE.jar!/META-INF/services/javax.servlet.ServletContainerInitializer
 * @see org.springframework.web.SpringServletContainerInitializer
 *
 * <pre>
 *     // 指定Servlet容器启动时传入的类
 *     @HandlesTypes(WebApplicationInitializer.class)
 *     public class SpringServletContainerInitializer implements ServletContainerInitializer {
 *
 *         List<WebApplicationInitializer> initializers = new LinkedList<>();
 *         ......
 *
 *         if (!waiClass.isInterface() &&
 *             !Modifier.isAbstract(waiClass.getModifiers()) &&
 *             WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
 *              // 获取到全部的WebApplicationInitializer实现类并创建实例
 *              initializers.add((WebApplicationInitializer)
 *              ReflectionUtils.accessibleConstructor(waiClass).newInstance());
 *         }
 *         ......
 *         // 排序
 *         AnnotationAwareOrderComparator.sort(initializers);
 *
 *         // 依次调用每个WebApplicationInitializer的onStartup(servletContext)方法
 *         // @see {@link org.springframework.web.WebApplicationInitializer}
 *
 *         for (WebApplicationInitializer initializer : initializers) {
 *             initializer.onStartup(servletContext);
 *         }
 *     }
 * </pre>
 *
 * 因此要实现注解驱动spring-webmvc即需要实现WebApplicationInitializer 而spring为WebApplicationInitializer
 * 提供了抽象实现 因此一般继承AbstractAnnotationConfigDispatcherServletInitializer提供配置类class实例即可
 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
 *
 * =====================================================================================================================
 *
 * ServletContainerInitializer驱动SpringMVC容器和Spring容器启动流程分析
 *
 *
 *
 * @auther mac
 * @date 2020-01-16
 */
public class AnnotationDrivenWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ApplicationContextConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebApplicationContextConfiguration.class};
    }

    /**
     * /:拦截所有请求;包括静态资源，但是不包括*.jsp;
     * /*:拦截所有请求;包括*.jsp;
     *
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
