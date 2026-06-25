package com.clubdeportivo.services.auth.utils;

import com.clubdeportivo.dtos.auth.LoginRequest;
import com.clubdeportivo.dtos.auth.LoginResponse;
import com.clubdeportivo.dtos.auth.RegisterRequest;
import com.clubdeportivo.services.auth.IAuthService;

public class AuthServiceImpl implements IAuthService {

    

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public LoginResponse register(RegisterRequest registerRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
    }

    @Override
    public void logout(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    @Override
    public boolean validateToken(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateToken'");
    }
    
}
