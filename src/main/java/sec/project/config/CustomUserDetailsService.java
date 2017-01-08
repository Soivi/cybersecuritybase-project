package sec.project.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private Map<String, String> accountDetails;
    @Autowired
    private PasswordEncoder passwordencoder;
    
    
    @PostConstruct
    public void init() {
        // this data would typically be retrieved from a database
        this.accountDetails = new TreeMap<>();

        // A2-Broken Authentication and Session Management
        // Comment this
        this.accountDetails.put("ted", "ted");
        // Uncomment this
        //this.accountDetails.put("ted", passwordencoder.encode("ted"));

    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!this.accountDetails.containsKey(username)) {
            throw new UsernameNotFoundException("No such user: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                username,
                this.accountDetails.get(username),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
    
    public void changePassword(String name, String password) {        
        // A2-Broken Authentication and Session Management
        // Comment this
        this.accountDetails.replace(name, password);
        // Uncomment this
        //this.accountDetails.replace(name, passwordencoder.encode(password));        
    }
    
}
