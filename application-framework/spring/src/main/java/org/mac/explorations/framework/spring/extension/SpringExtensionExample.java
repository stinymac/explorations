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

package org.mac.explorations.framework.spring.extension;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.AbstractApplicationEventMulticaster;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * Spring 扩展
 *
 * 1.BeanDefinitionRegistryPostProcessor
 *   @see BeanDefinitionRegistryPostProcessor
 *  它是BeanFactoryPostProcessor的子接口 在所有Bean定义信息将要被加载，Bean实例还未创建之前执行
 *  这里可以修改已注册的ean定义
 *  //Modify the application context's internal bean definition registry after its standard initialization.
 *  这里回调传入的Bean注册对象BeanDefinitionRegistry 而 BeanFactoryPostProcessor 传入的是BeanFactory
 *
 * 2.BeanFactoryPostProcessor
 *   @see BeanFactoryPostProcessor
 *   在BeanFactory标准初始化之后调用，来定制和修改BeanFactory的内容；此时所有的Bean定义已经保存加载到BeanFactory
 *
 * 3.BeanPostProcessor
 *   @see BeanPostProcessor
 *   Bean创建对象初始化前后调用
 *
 * 4.ApplicationListener
 *   @see ApplicationListener
 *   事件发生时调用
 *
 *   Spring事件模型使用
 *
 *   1).监听容器事件
 *
 *     实现ApplicationListener接口指定监听的事件 并将监听实现Bean注册到容器
 *     或在普通方法上加@EventListener注解并指定监听的事件的类型
 *
 *   2).发布自定义事件
 *
 *     先容器注册自定义事件的监听器
 *     使用ApplicationContext的publishEvent(ApplicationEvent event)方法发布自定义事件
 *
 * Spring 扩展相关源码分析
 *
 * 上下文初始化主要流程为
 * <pre>
 *     // Allows post-processing of the bean factory in context subclasses.
 *     postProcessBeanFactory(beanFactory);
 *
 *     // Invoke factory processors registered as beans in the context.
 *     invokeBeanFactoryPostProcessors(beanFactory);
 *
 *     // Register bean processors that intercept bean creation.
 *     registerBeanPostProcessors(beanFactory);
 *
 *     // Initialize message source for this context.
 *     initMessageSource();
 *
 *     // Initialize event multicaster for this context.
 *     initApplicationEventMulticaster();
 *
 *     // Initialize other special beans in specific context subclasses.
 *     onRefresh();
 *
 *     // Check for listener beans and register them.
 *     registerListeners();
 *
 *     // Instantiate all remaining (non-lazy-init) singletons.
 *     finishBeanFactoryInitialization(beanFactory);
 *
 *     // Last step: publish corresponding event.
 *     finishRefresh();
 * </pre>
 *
 * 其中invokeBeanFactoryPostProcessors(beanFactory)即调用注册的BeanFactoryPostProcessor
 *     <pre>
 *         PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
 *     </pre>
 *     getBeanFactoryPostProcessors()取全部已注册的BeanFactoryPostProcessor
 *     (但当前为空 这里实际是取BeanFactoryPostProcessor的容器-一个List)
 *     @see {@link org.springframework.context.support.PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory, java.util.List)}
 *     通过多次(实现了PriorityOrdered、实现了Ordered 和普通的未实现排序接口的)取出已
 *     注册的BeanDefinitionRegistryPostProcessor(取出后调用getBean()实例化)后排序并调用处理器方法
 *     @see org.springframework.context.support.PostProcessorRegistrationDelegate#invokeBeanDefinitionRegistryPostProcessors(java.util.Collection, BeanDefinitionRegistry)
 *     <pre>
 *         // 即依次调用每个BeanDefinitionRegistryPostProcessor处理器的处理方法
 *         for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
 *             postProcessor.postProcessBeanDefinitionRegistry(registry);
 *         }
 *     </pre>
 *     最后依次调用这些处理器的postProcessBeanFactory()方法
 *     (因为BeanDefinitionRegistryPostProcessor是BeanFactoryPostProcessor的子接口)
 *
 *     接下来按与BeanDefinitionRegistryPostProcessor同样的逻辑 多次取出BeanFactoryPostProcessor
 *     的注册定义信息(取出后调用getBean()实例化)后依次调用其处理方法(即postProcessBeanFactory())
 *
 * registerBeanPostProcessors(beanFactory); 注册BeanPostProcessor的逻辑与BeanFactoryPostProcessor
 * 类似 即按是否实现PriorityOrdered、实现了Ordered 和普通的分类并初始化注册到容器中
 * @see org.springframework.context.support.PostProcessorRegistrationDelegate#registerBeanPostProcessors(ConfigurableListableBeanFactory, org.springframework.context.support.AbstractApplicationContext)
 *    <pr>
 *        for (BeanPostProcessor postProcessor : postProcessors) {
 *            beanFactory.addBeanPostProcessor(postProcessor);
 *        }
 *    </pr>
 *
 *    BeanPostProcessor实例注册到容器后 容器初始化执行到finishBeanFactoryInitialization(beanFactory);
 *    的时候开始初始化一般的单例Bean 在执行初始化的前后(设置Bean的属性在这之前
 *        <pre>
 *            populateBean(beanName, mbd, instanceWrapper);
 *            exposedObject = initializeBean(beanName, exposedObject, mbd);
 *        </pre>
 *    )调用BeanPostProcessor
 *    @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean(String, Object, org.springframework.beans.factory.support.RootBeanDefinition)
 *    <pre>
 *        Object wrappedBean = bean;
 *        if (mbd == null || !mbd.isSynthetic()) {
 *            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
 *        }
 *
 *        try {
 *            invokeInitMethods(beanName, wrappedBean, mbd);
 *        }
 *        catch (Throwable ex) {
 *         ......
 *        }
 *        if (mbd == null || !mbd.isSynthetic()) {
 *            wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
 *        }
 *    </pre>
 *
 * 事件处理的相关逻辑为 在初始化容器时执行 initApplicationEventMulticaster(); 初始化事件广播器
 * @see org.springframework.context.support.AbstractApplicationContext#initApplicationEventMulticaster()
 *
 *     <pre>
 *         // 初始化已注册的广播器
 *         if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
 *             this.applicationEventMulticaster =
 *             beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
 *         }
 *         else { // 或创建并注册SimpleApplicationEventMulticaster到容器
 *             this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
 *             beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
 *         }
 *     </pre>
 *     默认使用的是SimpleApplicationEventMulticaster
 *     @see org.springframework.context.event.SimpleApplicationEventMulticaster
 *
 *     接着注册监听器
 *     <pre>
 *         String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
 *         for (String listenerBeanName : listenerBeanNames) {
 *             getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
 *         }
 *     </pre>
 *     这里的注册是向广播器的applicationListenerBeans容器中(此时Listener Bean 还未初始化)
 *     @see AbstractApplicationEventMulticaster.ListenerRetriever#applicationListenerBeans
 *     添加保存ListenerBean的beanName
 *
 *     然后在完成容器refresh的时候发布容器refreshed完成事件 publishEvent(new ContextRefreshedEvent(this));
 *     @see org.springframework.context.event.SimpleApplicationEventMulticaster#multicastEvent(ApplicationEvent, org.springframework.core.ResolvableType)
 *     向取出全部的listener 因为这个时候listener以完成成初始化并在容器中
 *     @see AbstractApplicationEventMulticaster#retrieveApplicationListeners(org.springframework.core.ResolvableType, Class, AbstractApplicationEventMulticaster.ListenerRetriever)
 *     取出后依次调用listener 的listen方法
 *     <pre>
 *         for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
 *             if (executor != null) {
 *                 // 使用执行器异步调用
 *                 executor.execute(() -> invokeListener(listener, event));
 *             }
 *             else {
 *                 // 直接调用
 *                 invokeListener(listener, event);
 *             }
 *         }
 *     </pre>
 *
 *     自定义事件的发布一般即是在对应的逻辑处理处手动调用容器的事件发布方法publishEvent()
 *
 * @auther mac
 * @date 2020-01-15
 */
@Configuration
@Import({
        SimpleBeanFactoryPostProcessor.class,
        SimpleBeanDefinitionRegistryPostProcessor.class,
        SimpleBeanPostProcessor.class,
        SimpleApplicationListener.class,
        PlainObservableService.class,
        PlainObservableServiceFinishedListener.class
})
public class SpringExtensionExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringExtensionExample.class);
        PlainObservableService plainObservableService = context.getBean(PlainObservableService.class);
        plainObservableService.doService();
    }
}

class SimpleBeanDefinitionRegistryPostProcessor  implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        System.err.println("SimpleBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry (BeanDefinitionRegistry registry)->");
        registry.registerBeanDefinition("plainBean", BeanDefinitionBuilder.rootBeanDefinition(Object.class).getBeanDefinition());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.err.println("SimpleBeanDefinitionRegistryPostProcessor.postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) ->"+beanFactory.getBeanDefinitionCount());
    }
}

class SimpleBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        System.err.println("-----------------------SimpleBeanFactoryPostProcessor.postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)---------------------------");
        /*
        String[] names =  beanFactory.getBeanDefinitionNames();
        for (String beanName : names) {
            System.err.println(beanFactory.getBeanDefinition(beanName));
            System.err.println("--------------------------------------------------");
        }
        */
    }
}

class SimpleBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.err.println("beanName:"+beanName+"--->bean:"+bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.err.println("beanName:"+beanName+"--->bean:"+bean);
        return bean;
    }
}

class SimpleApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("ContextRefreshedEvent:"+event);
    }
}

class PlainObservableService implements ApplicationContextAware {

    private ApplicationContext context;

    public void doService() {
        System.out.println("PlainObservableService.doService()...");
        context.publishEvent(new PlainObservableServiceFinishedEvent("PlainObservableServiceFinishedEvent"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}

class PlainObservableServiceFinishedEvent extends ApplicationEvent {
    public PlainObservableServiceFinishedEvent(Object source) {
        super(source);
    }
}

class PlainObservableServiceFinishedListener {

    @EventListener(classes={PlainObservableServiceFinishedEvent.class})
    public void onPlainObservableServiceFinished(ApplicationEvent event) {
        System.out.println("PlainObservableServiceFinishedEvent:"+event);
    }
}