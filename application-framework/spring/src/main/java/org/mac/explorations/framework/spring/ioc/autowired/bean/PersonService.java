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

package org.mac.explorations.framework.spring.ioc.autowired.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;

import javax.annotation.Resource;

/**
 * @auther mac
 * @date 2020-01-13
 */
@Service
public class PersonService implements ApplicationContextAware,BeanNameAware,EmbeddedValueResolverAware {

    //@Autowired
    //@Resource(name = "otherPersonRepository")
    @Resource
    //@Inject
    private PersonRepository personRepository;

    private Person person;

    @Autowired
    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "PersonService{" +
                "personRepository=" + personRepository +
                ", person=" + person +
                '}';
    }

    @Override
    public void setBeanName(String name) {
        System.err.println("Current Bean's name:"+name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.err.println("Current application context:"+ applicationContext);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        System.err.println("Value Resolver:"+resolver.resolveStringValue("os.name:${os.name},#{20*18}"));
    }
}
