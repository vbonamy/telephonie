package fr.univrouen.telephonie.security;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import fr.univrouen.telephonie.domain.Authority;
import fr.univrouen.telephonie.domain.User;
import fr.univrouen.telephonie.repository.UserRepository;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@Transactional
public class UserDetailsService implements AuthenticationUserDetailsService<Authentication> {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Inject
    private UserRepository userRepository;

	@Override
	public UserDetails loadUserDetails(Authentication token)
			throws UsernameNotFoundException {
		
		String login = token.getPrincipal().toString();
		
		log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();
        
        User userFromDatabase = userRepository.findOne(lowercaseLogin);
        if (userFromDatabase == null) {
        	userFromDatabase = new User();
        	userFromDatabase.setLogin(lowercaseLogin);
        	userRepository.save(userFromDatabase);
        } 
        
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : userFromDatabase.getAuthorities()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            grantedAuthorities.add(grantedAuthority);
        }       
        
        return new org.springframework.security.core.userdetails.User(lowercaseLogin, lowercaseLogin, grantedAuthorities);
	}
}
