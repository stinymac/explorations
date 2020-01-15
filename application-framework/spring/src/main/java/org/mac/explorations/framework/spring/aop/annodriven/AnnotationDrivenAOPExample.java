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

package org.mac.explorations.framework.spring.aop.annodriven;

import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * AOP:[动态代理] 指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式；
 *
 * 注解驱动的 Spring AOP 的使用示例:
 *
 * 1、导入aop模块；Spring AOP：(spring-aspects)
 * 2、定义一个业务逻辑类(PlainMathematicalCalculationsService)；在业务逻辑运行的时候将日志进行打印(方法之前、方法运行结束、方法出现异常)
 * 3、定义一个日志切面类(LogAspects)：切面类里面的方法需要动态感知PlainMathematicalCalculationsService.divide(int int)运行到哪里然后执行；
 * 	 通知方法：
 * 			前置通知(@Before)：logStart：在目标方法(divide)运行之前运行
 * 			后置通知(@After)：logEnd：在目标方法(divide)运行结束之后运行（无论方法正常结束还是异常结束）
 * 			返回通知(@AfterReturning)：logReturn：在目标方法(divide)正常返回之后运行
 * 			异常通知(@AfterThrowing)：logException：在目标方法(divide)出现异常以后运行
 * 			环绕通知(@Around)：动态代理，手动推进目标方法运行[joinPoint.proceed()]
 *
 * 4、给切面类的目标方法标注何时何地运行(通知注解)；
 *
 * 5、将切面类(切面类上加一个注解：@Aspect 告诉Spring哪个类是切面类)和业务逻辑类（目标方法所在类）都加入到容器中;
 *
 * 6、给配置类中加 @EnableAspectJAutoProxy [开启基于注解的aop模式]
 *
 * =====================================================================================================================
 * Spring AOP 初始化及切面植入
 *
 * 基于注解的AOP需要开启@see {@link EnableAspectJAutoProxy}
 * <pre>
 *     @Import(AspectJAutoProxyRegistrar.class)
 *     public @interface EnableAspectJAutoProxy {
 *         ......
 *     }
 * </pre>
 *
 * 注解@EnableAspectJAutoProxy向容器中导入了@see {@link org.springframework.context.annotation.AspectJAutoProxyRegistrar}
 * 这个类是@see {@link org.springframework.context.annotation.ImportBeanDefinitionRegistrar}接口的实现 其向容器中注册了name
 * 为@see {@link org.springframework.aop.config.AopConfigUtils#AUTO_PROXY_CREATOR_BEAN_NAME}值为
 * @see {@link org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator}的Bean定义
 * 并设置AutoProxyCreator定义的属性 proxyTargetClass 和 exposeProxy
 * @see org.springframework.context.annotation.AspectJAutoProxyRegistrar#registerBeanDefinitions
 * (org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)
 *
 *
 * AnnotationAwareAspectJAutoProxyCreator 定义注册:
 * // (escalate - 提升, apc - AutoProxyCreator)
 * @see org.springframework.aop.config.AopConfigUtils#registerOrEscalateApcAsRequired
 * (java.lang.Class, org.springframework.beans.factory.support.BeanDefinitionRegistry, java.lang.Object)
 *
 * <pre>
 *     // 如果已注册定义查看已注册的AUTO_PROXY_CREATOR的优先级@see {@link org.springframework.aop.config.AopConfigUtils#APC_PRIORITY_LIST}
 *     // 如果小于 AnnotationAwareAspectJAutoProxyCreator
 *     // 则设置定义的Name为 apcDefinition.setBeanClassName(cls.getName());
 *
 *     // 没有注册则注册 AnnotationAwareAspectJAutoProxyCreator
 *     RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
 *     beanDefinition.setSource(source);
 *     beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
 *     beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
 *     registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
 *     return beanDefinition;
 * </pre>
 *
 * 综上得出 @EnableAspectJAutoProxy 注解向容器中注册了 AnnotationAwareAspectJAutoProxyCreator 类的Bean定义信息
 *
 *
 * AnnotationAwareAspectJAutoProxyCreator 向容器中注册
 *
 *
 * AnnotationAwareAspectJAutoProxyCreator的层次结构
 * @see /explorations/application-framework/spring/src/main/resources/images/AnnotationAwareAspectJAutoProxyCreator.png
 *
 * 通过AnnotationAwareAspectJAutoProxyCreator的层次结构可以得出它是BeanPostProcessor
 * (SmartInstantiationAwareBeanPostProcessor,InstantiationAwareBeanPostProcessor - 即实现Bean实例化前后和初始化前后处理)、
 * BeanFactoryAware的实现,并支持@see {@link org.springframework.aop.framework.ProxyConfig}
 * 和 @see {@link org.springframework.aop.framework.ProxyProcessorSupport}
 *
 * 其父类依次为
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator
 *     @see org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator
 *         @see org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator
 *
 * 容器初始化流程中 @see {@link org.springframework.context.support.AbstractApplicationContext#refresh()}
 * <pre>
 *     ......
 *     // Register bean processors that intercept bean creation.
 *     registerBeanPostProcessors(beanFactory);
 *     ......
 *     // Instantiate all remaining (non-lazy-init) singletons.
 *     finishBeanFactoryInitialization(beanFactory);
 * </pre>
 * 向容器中注册全部的BeanPostProcessor 那么AnnotationAwareAspectJAutoProxyCreator作为BeanPostProcessor的实现
 * 也会在这个时候注册到容器中
 *
 * 注册BeanPostProcessor
 *
 * @see org.springframework.context.support.PostProcessorRegistrationDelegate#registerBeanPostProcessors
 * (ConfigurableListableBeanFactory, AbstractApplicationContext)
 *
 * 1) 从BeanFactory中获取所有已注册定义的BeanPostProcessors的name 这里是
 *    0 = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor"
 *    1 = "org.springframework.context.annotation.internalCommonAnnotationProcessor"
 *    2 = "org.springframework.aop.config.internalAutoProxyCreator"
 *
 * 2) 向BeanFactoty中添加一个BeanPostProcessor检查器
 *    <pre>
 *        beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));
 *    </pre>
 *    添加一个BeanPostProcessor
 *    @see org.springframework.beans.factory.support.AbstractBeanFactory#addBeanPostProcessor(org.springframework.beans.factory.config.BeanPostProcessor)
 *
 * 3) 对这些BeanPostProcessor的定义进行分类 即
 *    <pre>
 *        List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
 *        List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
 *        List<String> orderedPostProcessorNames = new ArrayList<>();
 *        List<String> nonOrderedPostProcessorNames = new ArrayList<>();
 *    </pre>
 *    分类的过程中如果BeanPostProcessor实现了PriorityOrdered接口 则会调用getBean实例化BeanPostProcessor
 *    <pre>
 *        if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
 *            BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
 *            ......
 *        }
 *    </pre>
 *
 * 4) 对priorityOrderedPostProcessors排序 并注册priorityOrderedPostProcessors到容器
 *
 * 5) 对orderedPostProcessors调用getBean实例化后 排序 注册到容器
 *    AnnotationAwareAspectJAutoProxyCreator 是实现了Ordered的BeanPostProcessor
 *    因此会在这里初始化
 *
 *         AnnotationAwareAspectJAutoProxyCreator作为BeanPostProcessor注册 并完成实例化 初始化
 *
 *         在创建Bean的过程中 @see {@link AbstractAutowireCapableBeanFactory#createBean(String, org.springframework.beans.factory.support.RootBeanDefinition, Object[])}
 *          会先调用 @see {@link AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation(String, org.springframework.beans.factory.support.RootBeanDefinition)}
 *         尝试返回代理对象
 *         <pre>
 *             // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
 *             Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
 *             if (bean != null) {
 *                return bean;
 *             }
 *         </pre>
 *
 *         AnnotationAwareAspectJAutoProxyCreator实现了InstantiationAwareBeanPostProcessor接口 因此会在这里
 *         开始创建代理对象 但此时AnnotationAwareAspectJAutoProxyCreator本身还未完成实例化 其会首先完成自身的初始化
 *         其本身对象创建后 执行@see {@link AbstractAutowireCapableBeanFactory#populateBean(String, org.springframework.beans.factory.support.RootBeanDefinition, org.springframework.beans.BeanWrapper)}
 *         这里会再次调用BeanPostProcessor 但AnnotationAwareAspectJAutoProxyCreator此时还没有加入到
 *          @see {@link org.springframework.beans.factory.support.AbstractBeanFactory#beanPostProcessors}中
 *         接下来初始化AnnotationAwareAspectJAutoProxyCreator @see {@link AbstractAutowireCapableBeanFactory#initializeBean(String, Object, org.springframework.beans.factory.support.RootBeanDefinition)}
 *         调用invokeAwareMethods方法设置Aware对应的组件 @see {@link AbstractAutowireCapableBeanFactory#invokeAwareMethods(String, Object)}
 *         AnnotationAwareAspectJAutoProxyCreator 实现了BeanClassLoaderAware 和 BeanFactoryAware
 *
 *         AnnotationAwareAspectJAutoProxyCreator初始化BeanFactory 会执行@see {@link org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator#initBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)}
 *         和会执行@see {@link org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator#initBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)}
 *
 *         AnnotationAwareAspectJAutoProxyCreator 本身完成初始化后注册到容器中
 *
 *         以上过程完成了AnnotationAwareAspectJAutoProxyCreator本身作为容器中的一个Bean的初始化过程
 *
 * 6) 对非排序的BeanPostProcessor调用getBean实例化后 注册到容器
 *
 * 7) 再次注册internalPostProcessors到容器
 *
 * 8) 注册ApplicationListenerDetector到容器
 *
 * BeanPostProcessor向容器中注册(初始化并注册)完成后 现在AnnotationAwareAspectJAutoProxyCreator作为BeanPostProcessor
 * 以存在于容器中 容器初始执行 finishBeanFactoryInitialization是AnnotationAwareAspectJAutoProxyCreator就可以开始处理
 * 目标对象和切面类之间的AOP了
 * <pre>
 *     ......
 *     // Instantiate all remaining (non-lazy-init) singletons.
 *     finishBeanFactoryInitialization(beanFactory);
 * </pre>
 *
 * @see org.springframework.context.support.AbstractApplicationContext#finishBeanFactoryInitialization(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#preInstantiateSingletons()
 * preInstantiateSingleton时Bean定义为工厂Bean则调用工厂Bean的处理方法,否则调用getBean
 * 这里的需要被代理的Bean为PlainMathematicalCalculationsService调用getBean开始实例化
 * @see org.springframework.beans.factory.support.AbstractBeanFactory#doGetBean(String, Class, Object[], boolean)
 *
 * 1)、先从单例对象的缓存中取值 没有继续向下执行
 *
 * 2)、检查是否已经在创建当前Bean 没有继续向下执行
 *
 * 3)、如果有父BeanFactory并且当前Bean定义不再当前BeanFactory中 检查父BeanFactory是否有对应定义 并创建Bean 否则向下执行
 *
 * 4)、将当前Bean标记为已创建
 *
 * 5)、获取当前Bean定义并检查Bean的定义
 *
 * 6)、获取当前Bean的依赖 如果有依赖先注册并创建依赖的Bean
 *
 * 7)、没有依赖调用getSingleton获取对象(即再次从缓存中取 没有则调用createBean创建)
 *     @see org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#getSingleton(String, org.springframework.beans.factory.ObjectFactory)
 *     这里第一创建 调用createBean
 *     @see AbstractAutowireCapableBeanFactory#createBean(String, org.springframework.beans.factory.support.RootBeanDefinition, Object[])
 *     1.从当前Bean的定义中解析出其Class类型对象
 *     2.准备方法重写?
 *     3.调用resolveBeforeInstantiation
 *       @see AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation(String, org.springframework.beans.factory.support.RootBeanDefinition)
 *       <pre>
 *           bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 *           if (bean != null) {
 *               bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 *           }
 *       </pre>
 *       即应用InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation和postProcessAfterInstantiation方法
 *       @see AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInstantiation(Class, String)
 *       就这里来看 AnnotationAwareAspectJAutoProxyCreator 实现了InstantiationAwareBeanPostProcessor接口
 *       开始应用实例化前后的处理 即进入执行
 *       @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessBeforeInstantiation(Class, String)
 *
 *       获取自定义TargetSource @see {@link org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#getCustomTargetSource(Class, String)}
 *       这里没有
 *
 *    4.这里没有能为需要AOP的目标对对象做代理(没有自定义TargetSource)
 *
 * 8).调用doCreateBean创建对象
 *
 *    @see AbstractAutowireCapableBeanFactory#doCreateBean(String, org.springframework.beans.factory.support.RootBeanDefinition, Object[])
 *    @see AbstractAutowireCapableBeanFactory#createBeanInstance(String, org.springframework.beans.factory.support.RootBeanDefinition, Object[])
 *    @see AbstractAutowireCapableBeanFactory#instantiateBean(String, org.springframework.beans.factory.support.RootBeanDefinition)
 *    @see org.springframework.beans.factory.support.SimpleInstantiationStrategy#instantiate(org.springframework.beans.factory.support.RootBeanDefinition, String, org.springframework.beans.factory.BeanFactory)
 *
 *    doCreateBean 创建对象完成后 执行 applyMergedBeanDefinitionPostProcessors
 *    @see AbstractAutowireCapableBeanFactory#applyMergedBeanDefinitionPostProcessors(org.springframework.beans.factory.support.RootBeanDefinition, Class, String)
 *    但AnnotationAwareAspectJAutoProxyCreator不是 MergedBeanDefinitionPostProcessor 和这里无关所以执行对新创建的Bean的
 *    属性设置
 *    @see AbstractAutowireCapableBeanFactory#populateBean(String, org.springframework.beans.factory.support.RootBeanDefinition, org.springframework.beans.BeanWrapper)
 *    在属性填充前执行BeanPostProcessor的postProcessAfterInstantiation
 *    AnnotationAwareAspectJAutoProxyCreator 的postProcessAfterInstantiation 这里直接返回
 *
 *    属性设置初始化Bean
 *    @see AbstractAutowireCapableBeanFactory#initializeBean(String, Object, org.springframework.beans.factory.support.RootBeanDefinition)
 *
 *    初始化时会先后调用applyBeanPostProcessorsBeforeInitialization和applyBeanPostProcessorsAfterInitialization
 *    <pre>
 *        if (mbd == null || !mbd.isSynthetic()) {
 *            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
 *        }
 *
 *        try {
 *            invokeInitMethods(beanName, wrappedBean, mbd);
 *        }
 *        catch (Throwable ex) {
 *         ...
 *        }
 *        if (mbd == null || !mbd.isSynthetic()) {
 *            wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
 *        }
 *    </pre>
 *    AnnotationAwareAspectJAutoProxyCreator 的postProcessorsAfterInitialization进行的处理为
 *    @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#wrapIfNecessary(Object, String, Object)
 *    即在这里开始真正的创建代理
 *    1.获取全部的Advices 和 Advisors
 *      @see org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator#getAdvicesAndAdvisorsForBean(Class, String, org.springframework.aop.TargetSource)
 *      查找所有的Advisors即切点通知
 *      @see org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator#findEligibleAdvisors(Class, String)
 *      @see org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper#findAdvisorBeans()
 *      @see org.springframework.aop.aspectj.annotation.BeanFactoryAspectJAdvisorsBuilder#buildAspectJAdvisors()
 *
 *    2.调通creatPoxy创建代理
 *      @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#createProxy(Class, String, Object[], org.springframework.aop.TargetSource)
 *
 *      创建代理工厂ProxyFactory
 *      @see org.springframework.aop.framework.ProxyFactory
 *      构建advisors
 *
 *      @see {@link org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#buildAdvisors(String, Object[])}
 *      @see org.springframework.aop.framework.adapter.DefaultAdvisorAdapterRegistry#wrap(Object)
 *
 *      将advisors和需要被代理的目标类对象实例(targetSource-当前Bean即PlainMathematicalCalculationsService的实例])设置到代理工厂
 *
 *      自定义配置代理工厂 当前没有
 *
 *      从代理工厂返回代理对象
 *      @see org.springframework.aop.framework.ProxyFactory#getProxy(ClassLoader)
 *      @see org.springframework.aop.framework.DefaultAopProxyFactory#createAopProxy(org.springframework.aop.framework.AdvisedSupport)
 *      <pre>
 *          if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
 *              Class<?> targetClass = config.getTargetClass();
 *              if (targetClass == null) {
 *                  ....
 *              }
 *              if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
 *                  return new JdkDynamicAopProxy(config);
 *              }
 *              return new ObjenesisCglibAopProxy(config);
 *          }
 *          else {
 *              return new JdkDynamicAopProxy(config);
 *          }
 *      </pre>
 *      代理对象创建后从代理对象获取目标类的代理实例
 *      @see org.springframework.aop.framework.CglibAopProxy#getProxy(ClassLoader)
 *      @see org.springframework.aop.framework.ObjenesisCglibAopProxy#createProxyClassAndInstance(org.springframework.cglib.proxy.Enhancer, org.springframework.cglib.proxy.Callback[])
 *
 *     到这里考察的AnnotationAwareAspectJAutoProxyCreator创建代理对象已完成
 *
 * 综上Spring AOP代理对象的初始化创建过程为:
 *
 * 1.注解开启@EnableAspectJAutoProxy 容器初始化时向容器中导入AspectJAutoProxyRegistrar而AspectJAutoProxyRegistrar向容器中
 *   注册AnnotationAwareAspectJAutoProxyCreator
 *
 * 2.容器初始化刷新时先初始化全部注册的BeanPostProcessor 而AnnotationAwareAspectJAutoProxyCreator是
 *   其中一个BeanPostProcessor
 *
 * 3.容器最后执行单例Bean的初始化 对需要植入通知的Bean初始化后调用AnnotationAwareAspectJAutoProxyCreator的
 *   postProcessAfterInitialization植入通知创建代理对象返回给容器
 *
 * Spring 对代理对象的植入了通知的方法的调用过程
 *
 * 调用代理对象方法 进入
 * @see org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor#intercept(Object, java.lang.reflect.Method, Object[], org.springframework.cglib.proxy.MethodProxy)
 * 获取目标方法上全部的通知
 * @see org.springframework.aop.framework.DefaultAdvisorChainFactory#getInterceptorsAndDynamicInterceptionAdvice(org.springframework.aop.framework.Advised, java.lang.reflect.Method, Class)
 * 一般来说就是查找与目标方法匹配的通知 将通知对应的方法拦截器返回
 * @see org.springframework.aop.framework.adapter.DefaultAdvisorAdapterRegistry#getInterceptors(org.springframework.aop.Advisor)
 *
 * 这里共有5个方法拦截器
 * 0 = {ExposeInvocationInterceptor@2195}
 * 1 = {AspectJAfterThrowingAdvice@2326} "org.springframework.aop.aspectj.AspectJAfterThrowingAdvice: advice method [public void org.mac.explorations.framework.spring.aop.annodriven.LogAspects.logReturn(org.aspectj.lang.JoinPoint,java.lang.Exception)]; aspect name 'org.mac.explorations.framework.spring.aop.annodriven.LogAspects'"
 * 2 = {AfterReturningAdviceInterceptor@2442}
 * 3 = {AspectJAfterAdvice@2551} "org.springframework.aop.aspectj.AspectJAfterAdvice: advice method [public void org.mac.explorations.framework.spring.aop.annodriven.LogAspects.logEnd(org.aspectj.lang.JoinPoint)]; aspect name 'org.mac.explorations.framework.spring.aop.annodriven.LogAspects'"
 * 4 = {MethodBeforeAdviceInterceptor@2673}
 *
 * 调用目标方法
 * @see org.springframework.aop.framework.ReflectiveMethodInvocation#proceed()
 * <pre>
 *     public Object proceed() throws Throwable {
 *         // We start with an index of -1 and increment early.
 *         if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
 *             // 拦截器链上的拦截器调用完后 调用目标方法
 *             return invokeJoinpoint();
 *         }
 *
 *         Object interceptorOrInterceptionAdvice =
 *         this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
 *
 *         if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
 *            // Evaluate dynamic method matcher here: static part will already have
 *            // been evaluated and found to match.
 *            InterceptorAndDynamicMethodMatcher dm =
 *            (InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
 *            Class<?> targetClass = (this.targetClass != null ? this.targetClass : this.method.getDeclaringClass());
 *            if (dm.methodMatcher.matches(this.method, targetClass, this.arguments)) {
 *                return dm.interceptor.invoke(this);
 *            }
 *            else {
 *                // Dynamic matching failed.
 *                // Skip this interceptor and invoke the next in the chain.
 *                return proceed();
 *           }
 *         }
 *         else {
 *             // It's an interceptor, so we just invoke it: The pointcut will have
 *             // been evaluated statically before this object was constructed.
 *             return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
 *         }
 *     }
 * </pre>
 *
 * 拦截器链的调用
 *
 * 第一个拦截器 ExposeInvocationInterceptor  invoke
 * @see org.springframework.aop.interceptor.ExposeInvocationInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
 *
 *      proceed();
 *      第二个拦截器AspectJAfterThrowingAdvice invoke
 *
 *          proceed();
 *          第三个拦截器 AfterReturningAdviceInterceptor invoke
 *
 *              proceed();
 *              第四个拦截器 AspectJAfterAdvice  invoke
 *
 *                  proceed();
 *                  第五个拦截器 MethodBeforeAdviceInterceptor invoke
 *                  before(Method method, Object[] args, @Nullable Object target)
 *                  @see org.springframework.aop.aspectj.AbstractAspectJAdvice#invokeAdviceMethodWithGivenArgs(Object[])
 *                  执行Before通知(即 org.mac.explorations.framework.spring.aop.annodriven.LogAspects#logStart(org.aspectj.lang.JoinPoint))
 *
 *                          proceed();
 *                          此时 this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1
 *                          执行目标方法
 *                          org.springframework.aop.framework.CglibAopProxy.CglibMethodInvocation#invokeJoinpoint()
 *                          执行完成返回

 *                 第五个拦截器MethodBeforeAdviceInterceptor 执行完成返回
 *
 *            第四个拦截器 AspectJAfterAdvice  返回后向下继续 执行拦截器本身需要执行的方法 即After通知
 *            <pre>
 *                finally {
 *                    //invokeAdviceMethodWithGivenArgs(java.lang.Object[])
 *                    invokeAdviceMethod(getJoinPointMatch(), null, null);
 *                }
 *            </pre>
 *
 *        第三个拦截器 AfterReturningAdviceInterceptor  返回后向下继续
 *        <pre>
 *            Object retVal = mi.proceed();
 *            // 如果执行出现了异常 直接返回到第二个拦截器调用中 正常执行 AfterReturing 通知并返回返回值
 *            this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
 *            return retVal;
 *        </pre>
 *        @see org.springframework.aop.aspectj.AspectJAfterReturningAdvice#afterReturning(Object, java.lang.reflect.Method, Object[], Object)
 *        执行 AfterReturing 完成后 返回返回值
 *
 *    第二个拦截器AspectJAfterThrowingAdvice 返回后向下继续 捕获异常(如果有) 调用AfterThrowingAdvice通知
 *    @see org.springframework.aop.aspectj.AspectJAfterThrowingAdvice#invoke(org.aopalliance.intercept.MethodInvocation)
 *    <pre>
 *        try {
 *            return mi.proceed();
 *        }
 *        catch (Throwable ex) {
 *            if (shouldInvokeOnThrowing(ex)) {
 *                invokeAdviceMethod(getJoinPointMatch(), null, ex);
 *            }
 *            throw ex;
 *        }
 *    </pre>
 *
 * 第一个拦截器 ExposeInvocationInterceptor 返回
 * <pre>
 *     finally {
 *         invocation.set(oldInvocation);
 *     }
 * </pre>
 * AOP处理结束
 *
 * =====================================================================================================================
 *
 * @auther mac
 * @date 2020-01-13
 */
@Configuration
@EnableAspectJAutoProxy
@Import({PlainMathematicalCalculationsService.class,LogAspects.class})
public class AnnotationDrivenAOPExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AnnotationDrivenAOPExample.class);
        /**
         * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#getBean(Class, Object...)
         * 这个方法里会对获取到的对象(这里是CGLIB代理对象)向上转型为PlainMathematicalCalculationsService
         */
        PlainMathematicalCalculationsService mathematicalCalculationsService = context.getBean(PlainMathematicalCalculationsService.class);
        System.out.println(mathematicalCalculationsService);
        // 在doGetBean里向上转型为PlainMathematicalCalculationsService
        System.out.println(context.getBean("org.mac.explorations.framework.spring.aop.annodriven.PlainMathematicalCalculationsService").getClass());
        mathematicalCalculationsService.divide(100,5);
    }
}