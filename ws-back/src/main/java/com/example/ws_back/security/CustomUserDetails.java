package com.example.ws_back.security;
import com.example.ws_back.usr.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserDto userDto;

    /** 권한 부여
     *
     * @return List<SimpleGrantedAuthority>
     */
    
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> isAdmin = new ArrayList<>();
        isAdmin.add("IsAdmin_" + userDto.getIsAdmin());

        return isAdmin.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userDto.getUserPw();
    }

    @Override
    public String getUsername() {
        return userDto.getUserId();
    }
    
    public String getRole() {
        return userDto.getIsAdmin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}