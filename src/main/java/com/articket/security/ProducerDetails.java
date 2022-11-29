package com.articket.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class ProducerDetails implements UserDetails {

    private String id;
    private String password;
    private String authority;
    private boolean enabled;
    private String name;

    //계정이 가지고 있는 권한 목록 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(authority));
        return null;
    }

    //계정의 비밀번호 리턴
    @Override
    public String getPassword() {
        return password;
    }

    //계정의 PK 리턴
    @Override
    public String getUsername() {
        return id;
    }

    //계정이 만료되지 않았는지 리턴 true: 만료X
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있는지 리턴 true: 잠기지않음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료되지 않았는지 리턴 true: 만료 안됨
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화인지 리턴 true:활성화
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
