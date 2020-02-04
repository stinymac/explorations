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

package org.mac.explorations.framework.springboot.web.service;

import org.mac.explorations.framework.springboot.web.repository.entity.User;
import org.mac.explorations.framework.springboot.web.repository.jpa.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * @auther mac
 * @date 2020-02-02 19:30
 */
@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * find user
     *
     * @param username
     * @return
     */
     public User getUserByUsername(String username){

        User user =new User();
        user.setUsername(username);

        return userRepository.findOne(Example.of(user)).get();
     }
}
