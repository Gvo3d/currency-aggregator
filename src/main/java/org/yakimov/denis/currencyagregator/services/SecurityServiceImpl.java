//package org.yakimov.denis.currencyagregator.services;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.yakimov.denis.currencyagregator.models.User;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class SecurityServiceImpl implements SecurityService, UserDetailsService {
//
//    private static final Logger LOGGER = Logger.getLogger(SecurityServiceImpl.class);
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public boolean cryptUserPass(User user) {
//        if (null!=user && null!=user.getPassword()){
//            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
//            user.setPassword(encodedPassword);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean isPasswordMatchEncrypted(String raw, String encoded) {
//        return bCryptPasswordEncoder.matches(raw, encoded);
//    }
//
//    @Override
//    public Long getAuthenticatedUserId(String login){
//        User credentials = userService.getUserByLogin(login);
//        return credentials.getId();
//    }
//
//    @Override
//    public Boolean autologin(String username, String password) {
//        UserDetails userDetails = loadUserByUsername(username);
//        String encodedPassword = bCryptPasswordEncoder.encode(password);
//        LOGGER.info("login="+username);
//        LOGGER.info("password="+password+" encoded="+encodedPassword);
//        Collection<? extends GrantedAuthority> authorities;
//        if(userDetails != null && null!=userDetails.getAuthorities()){
//            authorities =userDetails.getAuthorities();
//        }  else authorities = null;
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
//                (userDetails, password, authorities);
//        authenticationManager.authenticate(authenticationToken);
//        if (authenticationToken.isAuthenticated()){
//            LOGGER.info("auth+");
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            return Boolean.TRUE;
//        } else {
//            LOGGER.info("auth-");
//            return Boolean.FALSE;
//        }
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public UserDetails loadUserByUsername(String s) {
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        User credentials = userService.getUserByLogin(s);
//
//
//        if (null==credentials || null==credentials.getGroup().toString()) {
//            return null;
//        }
//        grantedAuthorities.add(new SimpleGrantedAuthority(credentials.getGroup().toString()));
//        return new org.springframework.security.core.userdetails.User(credentials.getLogin(), credentials.getPassword(), grantedAuthorities);
//    }
//}
