package swen90006.passbook;

import java.util.List;

import javax.print.attribute.standard.PDLOverrideSupported;

import java.util.ArrayList;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.*;


import jdk.nashorn.internal.ir.CatchNode;
import sun.misc.Unsafe;
import sun.security.jgss.HttpCaller;

import static org.junit.Assert.*;

public class PartitioningTests
{
    protected PassBook pb;
    public String passbookUsername;
    public String passphrase; 
    public Integer sessionID;
    public URL url;
    public String urlUsername;
    public String urlPassword;
    //Any method annotated with "@Before" will be executed before each test,
    //allowing the tester to set up some shared resources.
    @Before public void setUp()
    {
	   pb = new PassBook();
    }

    //Any method annotated with "@After" will be executed after each test,
    //allowing the tester to release any shared resources used in the setup.
    @After public void tearDown()
    {
    }

    //Any method annotation with "@Test" is executed as a test.
    //@Test public void aTest()
    //{
	//the assertEquals method used to check whether two values are
	//equal, using the equals method
	//final int expected = 2;
	//final int actual = 1 + 1;
	//assertEquals(expected, actual);
    //}

    //@Test public void anotherTest()
	//throws DuplicateUserException, WeakPassphraseException
    //{
	//pb.addUser("passbookUsername", "properPassphrase1");

	//the assertTrue method is used to check whether something holds.
	//assertTrue(pb.isUser("passbookUsername"));
	//assertFalse(pb.isUser("nonUser"));
    //}
  
    //To test an exception, specify the expected exception after the @Test
    //@Test(expected = java.io.IOException.class) 
    //public void anExceptionTest()
	//throws Throwable
    //{
	//throw new java.io.IOException();
    //}

    //This test should fail.
    //To provide additional feedback when a test fails, an error message
    //can be included
    //@Test public void aFailedTest()
    //{
	//include a message for better feedback
	//final int expected = 2;
	//final int actual = 1 + 2;
	//assertEquals("Some failure message", expected, actual);
    //}
    
    /*EC1 for method addUser*/
    @Test(expected = DuplicateUserException.class)
    public void DuplicateUser()
            throws Throwable
    {

        pb.addUser("dse1","Victor23");
        pb.addUser("dse1","Victor23");
    }

    /*EC2 for method addUser*/
    @Test(expected = WeakPassphraseException.class)
    public void lowerminlength()
        throws Throwable
    {

        pb.addUser("dse","Victor2");
        assertTrue(pb.isUser("dse"));
    }

    /*EC3 for method addUser*/
    @Test(expected = WeakPassphraseException.class)
    public void NoLowercase()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        pb.addUser("dse", "VICTOR22");

    }

    /*EC4 for method addUser*/
    @Test(expected = WeakPassphraseException.class)
    public void NoUppercase()
        throws Throwable
    {
         pb.addUser("dse1","Victor23");
         pb.addUser("dse", "victor22");

    }

    /*EC5 for method addUser*/
    @Test(expected = WeakPassphraseException.class)
    public void NoNumber()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        pb.addUser("dse", "VICTOR");

    }

    /*EC6 for method addUser*/
    @Test public void AllSuccess()
    throws Throwable
    {
        pb.addUser("dse1","Victor23");
        pb.addUser("dse","Victor22");

    }

    
    /*EC1 for method loginUser*/
    @Test (expected = NoSuchUserException.class)
    public void usernamenotExists()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        pb.loginUser("dse1","Victor23");
        pb.loginUser("dse","Victor22");

    }

    /*EC2 for method loginUser*/
    @Test public void sessionIdempty()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        pb.loginUser("dse1","Victor23");
    }

    /*EC3 for method loginUser*/
    @Test (expected = IncorrectPassphraseException.class)
    public void invalidPassphrase()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        pb.addUser("dse","Victor22");
        pb.loginUser("dse1","Victor24");
        pb.loginUser("dse","Victor22");

    }

    /*EC4 for method loginUser*/
    @Test public void validPassphrase()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        pb.addUser("dse","Victor22");
        pb.loginUser("dse1","Victor23");
        pb.loginUser("dse","Victor22");

    }

    @Test public void successLogin()
    throws DuplicateUserException, WeakPassphraseException,NoSuchUserException,IncorrectPassphraseException,AlreadyLoggedInException
    {
        pb.addUser("passbookUsername", "properPassphrase1");
        int i = pb.loginUser("passbookUsername", "properPassphrase1");
    }

    
    /*EC1 for updateDetails*/
    @Test(expected = MalformedURLException.class)
    public void Urlform()
     throws Throwable
    {
        pb.addUser("dse1","Victor23");
        int sessionID = pb.loginUser("dse1","Victor23");
        URL url =new URL ("hppp://123.com");
        pb.updateDetails(sessionID, url,"vic","viclove");
    }
    

    /*EC2 for updateDetails*/
    @Test (expected = NoSuchURLException.class)
    public void httpsnoUsername()
        throws Throwable
    {
        pb.addUser("dse1","Victor23");
        int sessionID = pb.loginUser("dse1","Victor23");
        URL url = new URL("http://123.com");
        pb.updateDetails(sessionID,url,"vic","viclove");
        pb.updateDetails(sessionID,url,null,"viclove");
        pb.retrieveDetails(sessionID,url);
    }

    /*EC3 for updateDetails*/
    @Test (expected = NoSuchURLException.class)
    public void httpsnoUsepassword()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        int sessionID = pb.loginUser("dse1","Victor23");
        URL url = new URL("https://123.com");
        pb.updateDetails(sessionID,url,"vic","viclove");
        pb.updateDetails(sessionID,url,"vic",null);
        pb.retrieveDetails(sessionID,url);
    }
    
    /*EC4 for updateDetails*/
    @Test public void httpAllpass()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        int sessionID = pb.loginUser("dse1","Victor23");
        URL url = new URL("http://123.com");
        pb.updateDetails(sessionID,url,"vic","viclove");
    }
    
    /*EC5 for updateDetails*/
    @Test (expected = InvalidSessionIDException.class)
    public  void sessionIDnotmatch()
            throws Throwable
    {
        pb.addUser("dse1","Victor23");
        int sessionID = pb.loginUser("dse1","Victor23");
        URL url = new URL("http://123.com");
        pb.updateDetails(sessionID+1,url,"vic","viclove");
    }
    
    @Test(expected = InvalidSessionIDException.class)
    public void ExceptionTest4EC5()
    throws Throwable
    {
    passbookUsername = "dse1";
    passphrase = "Victor23";
    pb.addUser(passbookUsername, passphrase);
    sessionID = pb.loginUser(passbookUsername, passphrase);
    url = new URL("http://123.com/");
    urlUsername = "vic";
    urlPassword = "viclove";
    pb.updateDetails(sessionID, url, urlUsername, urlPassword);
    sessionID = 3;
    assertTrue(pb.retrieveDetails(sessionID, url) != null);
    }
    
    @Test(expected = MalformedURLException.class)
    public void ExceptionTest4EC1()
    throws Throwable
    {
    passbookUsername = "dse1";
    passphrase = "Victor23";
    pb.addUser(passbookUsername, passphrase);
    sessionID = pb.loginUser(passbookUsername, passphrase);
    url = new URL("http://123.com/");
    urlUsername = "vic";
    urlPassword = "viclove";
    pb.updateDetails(sessionID, url, urlUsername, urlPassword);
    url = new URL("fppp://123.com/");
    assertTrue(pb.retrieveDetails(sessionID, url) != null);
    }
    
    @Test(expected = NoSuchURLException.class)
    public void ExceptionTest4EC4()
    throws Throwable
    {
    passbookUsername = "dse1";
    passphrase = "Victor23";
    pb.addUser(passbookUsername, passphrase);
    sessionID = pb.loginUser(passbookUsername, passphrase);
    url = new URL("http://123.com/");
    assertTrue(pb.retrieveDetails(sessionID, url) != null);
    }
    
    @Test(expected = NoSuchURLException.class)
    public void ExceptionTest4EC2()
    throws Throwable
    {
    passbookUsername = "dse1";
    passphrase = "Victor23";
    pb.addUser(passbookUsername, passphrase);
    sessionID = pb.loginUser(passbookUsername, passphrase);
    url = new URL("http://123.com/");
    urlUsername = "vic";
    urlPassword = "viclove";
    pb.updateDetails(sessionID, url, urlUsername, urlPassword);
    url = new URL("http://1234.com");
    assertTrue(pb.retrieveDetails(sessionID, url) != null);
    }    
    @Test public void Test4EC3()
    throws NoSuchUserException, AlreadyLoggedInException, IncorrectPassphraseException, MalformedURLException, InvalidSessionIDException, NoSuchURLException, DuplicateUserException, WeakPassphraseException
    {
    passbookUsername = "dse1";
    passphrase = "Victor23";
    pb.addUser(passbookUsername, passphrase);
    sessionID = pb.loginUser(passbookUsername, passphrase);
    url = new URL("http://123.com/");
    urlUsername = "vic";
    urlPassword = "viclove";
    pb.updateDetails(sessionID, url, urlUsername, urlPassword);
    assertTrue(pb.retrieveDetails(sessionID, url) != null);
    }  

}

    
         

