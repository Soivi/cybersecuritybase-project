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
            String address = resultSet.getString("address");
            String creditcard = resultSet.getString("creditcard");
            signupRepository.save(new Signup(name, address, creditcard));
            System.out.println("INIT : \t" + name + " " + address + " " + creditcard);
        } 
        
        resultSet.close();
        connection.close();
    }
    
    @Autowired
    private SignupRepository signupRepository;
    
    private List<Signup> signupList;

    //@RequestMapping("*")
    //public String defaultMapping() {
    //    return "redirect:/form";
    //}

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) throws Exception {
        this.signupList = new ArrayList<>();
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Signup");
        
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            String creditcard = resultSet.getString("creditcard");
            
            Signup signup = new Signup(name, address, creditcard);
            
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
            String address = resultSet.getString("address");
            String creditcard = resultSet.getString("creditcard");

            // A6-Sensitive Data Exposure
            // Comment this
            Signup signup = new Signup(name, address, creditcard);
            // Uncomment this
            //Signup signup = new Signup(name, address);
            
            this.signupList.add(signup);
            System.out.println("TÄSSÄ : \t" + name + " " + address);
        } 
        
        resultSet.close();
        connection.close();

        //model.addAttribute("signups", signupRepository.findAll());
        return this.signupList;
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String formname, @RequestParam String formaddress, @RequestParam String formcreditcard)  throws Exception {
        // <script>alert("testing");</script>
        // <script>window.location.replace("https://soivi.net");</script>
        // debit + "'); DELETE FROM Signup; INSERT INTO Signup (name, address, creditcard) VALUES ('Charlie', 'Street' , '377725598642897
        signupRepository.save(new Signup(formname, formaddress, formcreditcard));
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        
        // A1-Injection
        // Comment this
        String sql = "INSERT INTO Signup (name, address, creditcard) VALUES ('" + formname + "', '" + formaddress + "', '" + formcreditcard + "');";
        connection.createStatement().execute(sql);
        // Uncomment this
        /*
        String sql = "INSERT INTO Signup (name, address, creditcard) VALUES (?, ?, ?);";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, formname);
            preparedStatement.setString(2, formaddress);
            preparedStatement.setString(3, formcreditcard);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException t){
            System.out.println("Insert error");
        }
        */
        
        System.out.println(sql);       
        return "done";
    }
    
    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String loadDone(Model model) {
        return "done";
    }
}
