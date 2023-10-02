package com.develop.UserDetail;

import com.develop.Model.Users;
import com.develop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.get().getRoles().forEach(roles -> authorities.add(new SimpleGrantedAuthority(roles.getName())));
        return new User(user.get().getUsername(), user.get().getPassword(), authorities);
    }
}
