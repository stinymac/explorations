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

package org.mac.explorations.framework.springboot.autoconfigure.configuration;

import org.mac.explorations.framework.springboot.autoconfigure.service.SimpleEchoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 * @see org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration
 * @see org.springframework.boot.autoconfigure.mail.MailSenderJndiConfiguration
 * ......
 *
 * @auther mac
 * @date 2020-01-31 18:44
 */
@Configuration
public class ApplicationConfiguration {
    @Bean
    public SimpleEchoService simpleEchoService(){
        return new SimpleEchoService();
    }
}
