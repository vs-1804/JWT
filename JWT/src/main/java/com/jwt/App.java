package com.jwt;

public class App 
{
    public static void main( String[] args )
    {
         UserDetails userdetail = new UserDetails("vikas", "123", Role.USER);
      
         String token= JWTutil.generateToken(userdetail);
         System.out.println(token);
         System.out.println(JWTutil.validateToken(token, userdetail));
    }
}
