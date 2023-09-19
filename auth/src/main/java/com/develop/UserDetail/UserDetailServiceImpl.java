package com.develop.UserDetail;

import com.develop.Model.Roles;
import com.develop.Model.Users;
import com.develop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        return CustomUserDetail.builder()
                .id(user.get().getId())
                .username(user.get().getUsername())
                .password(user.get().getPassword())
                .authorities(mapRoles(user.get().getRoles()))
                .build();
    }

    private Collection<GrantedAuthority> mapRoles(List<Roles> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
