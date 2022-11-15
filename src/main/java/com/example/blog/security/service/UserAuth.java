package com.example.blog.security.service;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class UserAuth implements UserDetailsService {
    Long id;
    String username;
    String email;
    String first_name;
    String last_name;
    String phone;
    List<String> roles;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*RestTemplate restTemplate = new RestTemplate();
        System.out.println("Load User By Username");
        ResponseEntity<UserAuth> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/auth/verify-token/" + token, UserAuth.class);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED){
            UserAuth datas = responseEntity.getBody();
            System.out.println(responseEntity.getStatusCode());
            return UserDetailsImplement.build(datas.getId(), datas.getUsername(), datas.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        }*/
        return null;
    }
}
