package sec.project.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Signup extends AbstractPersistable<Long> {

    private String name;
    private String creditcard;
    private String site;
    private String siteurl;
    
    public Signup() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditcard() {
        return creditcard;
    }
    
    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
    
    public String getSiteurl() {
        return siteurl;
    }

    public void setSiteurl(String siteurl) {
        this.siteurl = siteurl;
    }

    public Signup(String name, String creditcard, String site, String siteurl) {
        this();
        this.name = name;
        this.creditcard = creditcard;
        this.site = site;
        this.siteurl = siteurl;
    }

}
