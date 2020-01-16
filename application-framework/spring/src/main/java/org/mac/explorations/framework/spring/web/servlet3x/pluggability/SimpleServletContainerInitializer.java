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

package org.mac.explorations.framework.spring.web.servlet3x.pluggability;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Servlet3.0插件支持
 *
 * Servlet3.0开始 约定Jar文件中ServletContainerInitializer的实现类在其
 * META-INF/services/javax.servlet.ServletContainerInitializer文件中指定
 *
 * 当Servlet容器启动时扫描应用中的Jar查找javax.servlet.ServletContainerInitializer文件
 * 读取ServletContainerInitializer的实现
 *
 * 对于ServletContainerInitializer的实现类可以用@HandlesTypes注解来指定需要容器加载的类
 * 容器启动时 传入@HandlesTypes指定的类型的全部实现类到Set<Class<?>> c
 * <pre>
 *     public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
 *
 *     }
 * </pre>
 *
 * 如果ServletContainerInitializer实现类没有@HandlesTypes注解，或如果没有匹配任何指定的 HandlesType，
 * 那么它会为每个应用使用 null 值的集合调用一次。这将允许 initializer 基于应用中可用的资源决定是否需要初
 * 始化 Servlet/Filter。
 *
 * @auther mac
 * @date 2020-01-16
 */
@HandlesTypes({PlainCustomServlet.class})
public class SimpleServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        if (!c.isEmpty()) {
            for (Class cls : c) {
                System.err.println("Class:"+cls.getName());
                if (PlainCustomServlet.class.isAssignableFrom(cls)) {
                    String name = cls.getSimpleName();
                    System.err.println("Simple Name:"+name);
                    ServletRegistration.Dynamic srd = ctx.addServlet(name,cls);
                    srd.addMapping("/"+name);
                }
            }
        }
        // 监听器
        ctx.addListener(SimpleServletContextListener.class);
        // 拦截器
        FilterRegistration.Dynamic frd = ctx.addFilter("simple-filter",new SimpleFilter());
        frd.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true,"/*");
    }
}
