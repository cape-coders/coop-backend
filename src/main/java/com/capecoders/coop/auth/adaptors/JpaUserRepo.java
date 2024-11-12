package com.capecoders.coop.auth.adaptors;

import com.capecoders.coop.auth.core.CoopUser;
import com.capecoders.coop.auth.core.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class JpaUserRepo implements UserRepo, UserDetailsService {
    private final JpaUserRepoInterface jpaUserRepoInterface;

    public JpaUserRepo(JpaUserRepoInterface jpaUserRepoInterface) {
        this.jpaUserRepoInterface = jpaUserRepoInterface;
    }

    @Override
    public List<CoopUser> getAllUsers() {
        return jpaUserRepoInterface.findAll();
    }

    @Override
    public CoopUser saveUser(CoopUser coopUser) {
        return jpaUserRepoInterface.save(coopUser);
    }

    @Override
    public CoopUser getUserByEmail(String email) {
        return jpaUserRepoInterface.getUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CoopUser userByEmail = jpaUserRepoInterface.getUserByEmail(username);
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public String getPassword() {
                return userByEmail.getPassword();
            }

            @Override
            public String getUsername() {
                return userByEmail.getEmail();
            }
        };
    }
}
