package com.doc.mobileappws;

import com.doc.mobileappws.entity.AuthorityEntity;
import com.doc.mobileappws.entity.RoleEntity;
import com.doc.mobileappws.entity.UserEntity;
import com.doc.mobileappws.repository.AuthorityRepository;
import com.doc.mobileappws.repository.RoleRepository;
import com.doc.mobileappws.repository.UserRepository;
import com.doc.mobileappws.utils.Roles;
import com.doc.mobileappws.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialUsersSetup {
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        System.out.println("From application ready event...");

        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
        RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        if(roleAdmin == null) return;

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("Mike");
        adminUser.setLastName("Ivanov");
        adminUser.setEmail("4amazomws@gmail.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(30));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123456"));
        adminUser.setRoles(Arrays.asList(roleAdmin));

        userRepository.save(adminUser);

    }

    @Transactional
    protected AuthorityEntity createAuthority(String name){
        AuthorityEntity authority = authorityRepository.findByName(name);
        if(authority == null){
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    protected RoleEntity createRole(String name, Collection<AuthorityEntity> authorities){
        RoleEntity role = roleRepository.findByName(name);
        if(role == null){
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
