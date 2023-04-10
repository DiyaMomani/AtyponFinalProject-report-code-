package com.example.worker.Authentication;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final Adding adding=new Adding();
    private final Checking checking=new Checking();
    public void add_User(UserForm form){adding.addUser(form);}
    public void add_Admin(UserForm form){adding.addAdmin(form);}
    public boolean trusted_User(String username , String token){return checking.trustedUser(username,token);}
    public boolean trusted_Admin(String username , String token){return checking.trustedAdmin(username,token);}
}
