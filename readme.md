# cybersecuritybase-project #

This project is part of Cyber Security Base course. Cyber Security Base with F-Secure is a course series by University of Helsinki in collaboration with F‑Secure Cyber Security Academy that focuses on building core knowledge and abilities related to the work of a cyber security professional. 

### Cyber Security Base - Course Project I ###

In the course project, task is to create a web application that has five flaws from the OWASP top ten list (https://www.owasp.org/index.php/Top_10_2013-Top_10). Starter code for the project is provided on Github at https://github.com/cybersecuritybase/cybersecuritybase-project.

## How to deploy ##

- Download Netbeans (or Eclipse)
- Download repository to your Netbeans projects (something like this /NetBeansProjects/mooc-2016-securing-software/cybersecuritybase-project)
- Open Netbeans
- Run Project
- Open browser and go to http://localhost:8080/form

## Vulnerabilities ##

Here is five flaws on the project. How to test vulnerabilities and fix those. (There could be more vulnerabilities than these, but don't mind those)

### A1-Injection ###

Test vulnerability

- Open http://localhost:8080/form
- Add these to your Form:

Name:
```
Jackie
```

Creditcard:
```
6011999910910162
```

Your homepage name:
```
EVIL
```

Your hamepage url:  
```sql
pagename + "'); DELETE FROM Signup; INSERT INTO Signup (name, creditcard, site, siteurl) VALUES ('Charlie', '377725598642897' , 'Evil PAGE', 'http://localhost:8080/password?password=evil
```

- Then push submit.
- List should be deleted and there should be only Charlie.

#### How to fix ####

SignupController.java:  
- Comment lines 123-124
- Uncomment lines 127-138



### A3-Cross-Site Scripting (XSS) ###
- Open http://localhost:8080/form
- Add these to your Form:

Name:  
```javascript
<script>alert("testing");</script>
```

Creditcard:
```
5557258692179716
```

Your homepage name:
```
OwnSite
```

Your hamepage url:
```
http://ownsite.com
```

- Then push submit.
- Push Back to signup form
- Alert text should appear

- Or you can just redirect just change Name on the form to this:
```javascript
<script>window.location.replace("https://soivi.net");</script>
```

#### How to fix ####

form.html
- Comment line 27
- Uncomment line 29

SecurityConfiguration.java
- Comment line 37



### A6-Sensitive Data Exposure ###

#### How to fix ####


### A2-Broken Authentication and Session Management ###

#### How to fix ####








### A8-Cross-Site Request Forgery (CSRF) ###

#### How to fix ####
