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
 * ServletContainerInitializer驱动Spring容器和SpringMVC容器启动流程分析
 *
 * SpringServletContainerInitializer 在Servlet容器启动时取得WebApplicationInitializer的
 * 实现类,这里即是自定义的AnnotationDrivenWebApplicationInitializer 他的继承层次结构为:
 *
 * @see org.springframework.web.WebApplicationInitializer
 *     @see org.springframework.web.context.AbstractContextLoaderInitializer
 *         @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
 *             @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
 *
 *
 * Servlet容器启动时取得WebApplicationInitializer接口的实现类 处理后 调用onStartup(servletContext)方法
 * 即调用@see {@link org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext) }
 * <pre>
 *     super.onStartup(servletContext);
 *     registerDispatcherServlet(servletContext);
 * </pre>
 * @see {@link org.springframework.web.context.AbstractContextLoaderInitializer#onStartup(javax.servlet.ServletContext)}
 * <pre>
 *     registerContextLoaderListener(servletContext);
 * </pre>
 * @see {@link org.springframework.web.context.AbstractContextLoaderInitializer#registerContextLoaderListener(javax.servlet.ServletContext)}
 * <pre>
 *     // 首先创建Spring根容器
 *     WebApplicationContext rootAppContext = createRootApplicationContext();
 *     if (rootAppContext != null) {
 *         // 注册Servlet上下文监听器 在Servlet容器启动的时候初始化Spring容器
 *         // @see {@link org.springframework.web.context.ContextLoaderListener}
 *         // @see {@link org.springframework.web.context.ContextLoader#initWebApplicationContext(javax.servlet.ServletContext)}
 *         ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
 *         listener.setContextInitializers(getRootApplicationContextInitializers());
 *         servletContext.addListener(listener);
 *     }
 * </pre>
 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#createRootApplicationContext()
 * <pre>
 *     // 获取配置类 即这里的AnnotationDrivenWebApplicationInitializer#getRootConfigClasses
 *     Class<?>[] configClasses = getRootConfigClasses();
 *     if (!ObjectUtils.isEmpty(configClasses)) {
 *         AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
 *         context.register(configClasses);
 *         return context;
 *     }
 * </pre>
 * 根容器创建后 注册DispatcherServerlet
 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#registerDispatcherServlet(javax.servlet.ServletContext)
 *
 * 1.实现创建Servlet容器
 *    @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#createServletApplicationContext()
 * 2.创建DispatcherServlet
 *   @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#createDispatcherServlet(org.springframework.web.context.WebApplicationContext)
 * 3.注册ApplicationContextInitializer 这里没有
 *   @see org.springframework.web.servlet.FrameworkServlet#setContextInitializers(org.springframework.context.ApplicationContextInitializer[])
 * 4.将创建的DispatcherServlet注册到Servlet上下文
 *   <pre>
 *       ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
 *   </pre>
 * 5.设置和其他注册
 *   <pre>
 *       registration.setLoadOnStartup(1);
 *       // 即这里提供的 AnnotationDrivenWebApplicationInitializer#getServletMappings()
 *       registration.addMapping(getServletMappings());
 *       registration.setAsyncSupported(isAsyncSupported());
 *
 *       Filter[] filters = getServletFilters();
 *       if (!ObjectUtils.isEmpty(filters)) {
 *           for (Filter filter : filters) {
 *               registerServletFilter(servletContext, filter);
 *           }
 *       }
 *       // 这里可以注册一些自定义的servlet组件
 *       customizeRegistration(registration);
 *   </pre>
 *
 * 综上 ServletContainerInitializer驱动Spring-Web应用启动的流程为:
 * 1.获取根容器配置类 创建根容器 并注册Servlet上下文监听器
 * 2.获取Servlet容器配置 创建Spring MVC容器
 * 3.注册Spring MVC的DispatcherServlet到Servlet应用上下文 DispatcherServlet初始化时驱动Spring MVC容器初始化
 * 4.Servlet容器启动时通过ServletContextListenner驱动Spring根容器首先启动 然后DispatcherServlet初始化时驱动Spring MVC容器启动
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
        return new Class[]{WebMVCApplicationContextConfiguration.class};
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
