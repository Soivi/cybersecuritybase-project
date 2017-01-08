package sec.project.controller;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.websocket.Session;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sec.project.config.CustomUserDetailsService;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @PostConstruct
    public void init() throws Exception {
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        try {
            RunScript.execute(connection, new FileReader("sql/database-schema.sql"));
            RunScript.execute(connection, new FileReader("sql/database-import.sql"));
        } catch (Throwable t) {
            System.err.println(t.getMessage());
        }

        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Signup");
        
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String creditcard = resultSet.getString("creditcard");
            String site = resultSet.getString("site");
            String siteurl = resultSet.getString("siteurl");
            System.out.println("INIT : \t" + name + " " + creditcard + " " + site + " " + siteurl);
        } 
        
        resultSet.close();
        connection.close();
    }
       
    private List<Signup> signupList;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) throws Exception {
        this.signupList = new ArrayList<>();
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Signup");
        
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String creditcard = resultSet.getString("creditcard");
            String site = resultSet.getString("site");
            String siteurl = resultSet.getString("siteurl");
            
            Signup signup = new Signup(name, creditcard, site, siteurl);
            
            this.signupList.add(signup);
        } 
        resultSet.close();
        connection.close();
        model.addAttribute("signups", this.signupList);
        return "form";
    }
   
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Signup> list() throws Exception {
        System.out.println("list alku");
        this.signupList = new ArrayList<>();
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Signup");
        
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String creditcard = resultSet.getString("creditcard");
            String site = resultSet.getString("site");
            String siteurl = resultSet.getString("siteurl");
            
            // A6-Sensitive Data Exposure
            // Comment this
            Signup signup = new Signup(name, creditcard, site, siteurl);
            // Uncomment this
            //Signup signup = new Signup(name, address);
            
            this.signupList.add(signup);
            System.out.println("TÄSSÄ : \t" + name);
        } 
        
        resultSet.close();
        connection.close();

        //model.addAttribute("signups", signupRepository.findAll());
        return this.signupList;
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String formname, @RequestParam String formcreditcard, @RequestParam String formsite, @RequestParam String formsiteurl)  throws Exception {
        // <script>alert("testing");</script>
        // <script>window.location.replace("https://soivi.net");</script>
        // debit + "'); DELETE FROM Signup; INSERT INTO Signup (name, address, creditcard) VALUES ('Charlie', 'Street' , '377725598642897
        // pagename + "'); DELETE FROM Signup; INSERT INTO Signup (name, creditcard, site, siteurl) VALUES ('Charlie', '377725598642897' , 'OwnSite', 'http://ownsite.com
        
        //signupRepository.save(new Signup(formname, formcreditcard, formsite, formsiteurl));
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        
        // A1-Injection
        // Comment this
        String sql = "INSERT INTO Signup (name, creditcard, site, siteurl) VALUES ('" + formname + "', '" + formcreditcard + "', '" + formsite + "', '" + formsiteurl + "');";
        connection.createStatement().execute(sql);
        // Uncomment this
        /*
        String sql = "INSERT INTO Signup (name, creditcard, site, siteurl) VALUES (?, ?, ?, ?);";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, formname);
            preparedStatement.setString(2, formcreditcard);
            preparedStatement.setString(3, formsite);
            preparedStatement.setString(4, formsiteurl);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException t){
            System.out.println("Insert error");
        }
        */
        
        System.out.println(sql);  
        return "done";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String loadAdmin(Model model) throws Exception {
        this.signupList = new ArrayList<>();
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Signup");
        
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String creditcard = resultSet.getString("creditcard");
            String site = resultSet.getString("site");
            String siteurl = resultSet.getString("siteurl");
            
            Signup signup = new Signup(name, creditcard, site, siteurl);
            
            this.signupList.add(signup);
        } 
        resultSet.close();
        connection.close();
        model.addAttribute("signups", this.signupList);
        return "admin";
    }
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    // A8-Cross-Site Request Forgery
    // Comment this
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    // Uncomment this
    //@RequestMapping(value = "/password", method = RequestMethod.POST)
    public String changePassword(Authentication authentication, String password) {
        customUserDetailsService.changePassword(authentication.getName(), password);
        return "redirect:/admin";
    }
}
