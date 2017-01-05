package sec.project.controller;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.h2.tools.RunScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
//@RestController
public class SignupController {

    @PostConstruct
    public void init() throws Exception {
        // this data would typically be retrieved from a database
        // this.accountDetails = new TreeMap<>();
        // this.accountDetails.put("ted", "$2a$06$rtacOjuBuSlhnqMO2GKxW.Bs8J6KI0kYjw/gtF0bfErYgFyNTZRDm");
        
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
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            signupRepository.save(new Signup(name, address));
            System.out.println("INIT : " + id + "\t" + name + " " + address);
        } 
        
        resultSet.close();
        connection.close();
    }
    
    @Autowired
    private SignupRepository signupRepository;
    
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
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");

            Signup signup = new Signup(name, address);
            
            this.signupList.add(signup);
            //signupRepository.save(new Signup(name, address));
            //System.out.println("TÄSSÄ : " + id + "\t" + name + " " + address);
        } 
        
        resultSet.close();
        connection.close();
        
        model.addAttribute("signups", this.signupList);
        //model.addAttribute("signups", signupRepository.findAll());
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
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");

            Signup signup = new Signup(name, address);
            
            this.signupList.add(signup);
            //signupRepository.save(new Signup(name, address));
            System.out.println("TÄSSÄ : " + id + "\t" + name + " " + address);
        } 
        
        resultSet.close();
        connection.close();

        //model.addAttribute("signups", signupRepository.findAll());
        return this.signupList;
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        signupRepository.save(new Signup(name, address));
        return "done";
    }

}
