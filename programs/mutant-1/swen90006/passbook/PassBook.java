package swen90006.passbook;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.Arrays;

/**
 * PassBook is a (fictional) online password manager. A password
 * manager is a software application that generates, stores, and
 * retrieves login details for users. 
 * 
 * A user has an account with PassBook. This account is protected by a
 * master password, or passphrase. Each user can store login details
 * for multiple websites, identified by their URL. A user can add a
 * login details for a given website. The passphrase is used to
 * encrypt all passwords, but for this implementation, we have ignored
 * encryption.
 * 
 * The PassBook passphrase must conform to the following requirements:
 * Must be at least eight characters long and contain at
 * least one digit (range 0-9), one letter in the range 'a'-'z', and 
 * one letter in the range 'A'-'Z'.
 * 
 * Username and passwords for stored URLs have no password
 * requirements.
 *
 * When a user logs into PassBook, they are given a session ID. This
 * session ID is used to identify them for all transactions until they
 * log out.
 */
public class PassBook 
{
    /** The minimum length of a passphrase */
    public final static int MINIMUM_PASSPHRASE_LENGTH = 8;

    /** Valid URL protocols for the passbook */
    public final static String [] VALID_URL_PROTOCOLS = {"http", "https"};
    
    //The passphrases (master passwords) for all users
    private Map<String, String> passphrases;
 
    //The login details for all users and the URLs they have stored
    private Map<String, PasswordTable> details;

    //Mapping from session IDs to usernames
    private Map<Integer, String> userIDs;

    //Mapping from usernames to sessionIDs
    private Map<String, Integer> sessionIDs;

    /**
     * Constructs an empty passbook.
     */
    public PassBook()
    {
	passphrases = new HashMap<String, String>();
	details = new HashMap<String, PasswordTable>();
	userIDs = new HashMap<Integer, String>();
	sessionIDs = new HashMap<String, Integer>();
    }

    /**
     * Adds a new user to the passbook.
     *
     * @param passbookUsername   the username for the user to be added
     * @param passphrase         the passphrase (master password) for the user
     * @throws DuplicateUserException   if the username is already in the passbook
     * @throws WeakPassphraseException  if the password does not fit the passphrase
                                        rules (see class documentation)
     *
     * Assumption: passbookUsername and passphrase are non-null
     */
    public void addUser(String passbookUsername, String passphrase)
	throws DuplicateUserException, WeakPassphraseException
    {
	//Check if this user exists
	if (passphrases.containsKey(passbookUsername)) {
	    throw new DuplicateUserException(passbookUsername);
	}
	//check the passphrase strength
	else {
	    if (passphrase.length() < MINIMUM_PASSPHRASE_LENGTH) {
		throw new WeakPassphraseException(passphrase);
	    }
	    
	    boolean containsLowerCase = false;
	    boolean containsUpperCase = false;
	    boolean containsNumber = false;
	    for (int i = 0; i < passphrase.length(); i++) {

		if ('a' <= passphrase.charAt(i) && passphrase.charAt(i) <= 'z') {
		    containsLowerCase = true;
		}
		else if ('A' <= passphrase.charAt(i) && passphrase.charAt(i) <= 'Z') {
		    containsUpperCase = true;
		}
		else if ('0' <= passphrase.charAt(i) && passphrase.charAt(i) <= '9') {
		    containsNumber = true;
		}
	    }

	    if (!containsLowerCase || !containsUpperCase || !containsNumber) {
		throw new WeakPassphraseException(passphrase);
	    }
	}

	passphrases.put(passbookUsername, passphrase);
	PasswordTable pt = new PasswordTable();
	details.put(passbookUsername, pt);
    }

    /**
     * Checks if a user exists
     * @param passbookUsername  the passbookUsername
     * @return  true if and only if this user is in the passbook
     *
     * Assumption: passbookUsername is non-null
     */
    public boolean isUser(String passbookUsername)
    {
	return passphrases.containsKey(passbookUsername);
    }

    /**
     * Logs a user into the passbook.
     *
     * @param passbookUsername  the passbookUsername for the user
     * @param passphrase        the passphrase (master password) for the user
     * @returns                 a session ID greater than or equal to 0
     * @throws  NoSuchUserException if the user does not exist in the passbook
     * @throws  AlreadyLoggedInException if the user is already logged in
     * @throws  IncorrectPassphraseException if the passphrase is incorrect for this user
     *
     * Assumption: passbookUsername and passphrase are non-null
     */
    public int loginUser(String passbookUsername, String passphrase)
	throws NoSuchUserException, AlreadyLoggedInException, IncorrectPassphraseException
    {
	//check that the user exists
	if (!passphrases.containsKey(passbookUsername)) {
	    throw new NoSuchUserException(passbookUsername);
	}
	else if (sessionIDs.get(passbookUsername) != null) {
	    throw new AlreadyLoggedInException(passbookUsername);
	}
	else if (!passphrases.get(passbookUsername).equals(passphrase)) {
	    throw new IncorrectPassphraseException(passbookUsername, passphrase);
	}

	//generate a random session ID that is not already taken
	int sessionID = new Random().nextInt(Integer.MAX_VALUE);
	while (userIDs.containsKey(sessionID)) {
	    sessionID = new Random().nextInt(Integer.MAX_VALUE);
	}

	//add the session ID
	sessionIDs.put(passbookUsername, sessionID);
	userIDs.put(sessionID, passbookUsername);
	
	return sessionID;
    }

    /**
     * Logs out a user based on session ID. Has no affect if the session ID does not exist.
     *
     * @param sessionID  the session ID to be terminated
     *
     * Assumption: session ID is non-null
     */
    public void logoutUser(Integer sessionID)
    {
	sessionIDs.remove(userIDs.get(sessionID));
	userIDs.remove(sessionID);
    }

    /**
     * Updates the login details for a URL of a user. If the url
     * username or password are null, the login details for this URL
     * must be removed.
     *
     * @param sessionID    the session ID for the logged-in user
     * @param urlUsername  the username for the user to be added
     * @param url          the URL for the username/password pair
     * @param urlPassword  the password to be add
     * @throws InvalidSessionIDException  if the session ID does not exists
     * @throws MalformedURLException      if the protocol is not 'http' or 'https'
     *
     * Assumption: url is non-null and a valid URL object
     * Assumption: sessionID is non-null
     */
    public void updateDetails(Integer sessionID, URL url, String urlUsername, String urlPassword)
	throws InvalidSessionIDException, MalformedURLException
    {
 	//check that the session ID exists
	String passbookUsername = userIDs.get(sessionID);
	if (passbookUsername == null) {
	    throw new InvalidSessionIDException(sessionID);
	}
	else if (!Arrays.asList(VALID_URL_PROTOCOLS).contains(url.getProtocol())) {
	    throw new MalformedURLException(passbookUsername);
	}

	PasswordTable pt = details.get(passbookUsername);
	if (urlUsername == null || urlPassword == null) {
	    pt.remove(url);
	}
	else {
	    pt.put(url, new Pair<String, String> (urlUsername, urlPassword));
	    details.put(passbookUsername, pt);
	}
    }


    /**
     * Retrieves login details for a given URL and user.
     *
     * @param sessionID   the session ID
     * @param url         the URL for the password being retrieved.
     * @returns           the username and password for a given url for the corresponding user
     * @throws NoSuchUserException    if the username does not exist in the passbook
     * @throws MalformedURLException  if the syntax is invalid (see class documentation)
     * @throws NoSuchURLException     if user does not have login for this URL
     *
     * Assumption: url is non-null and a valid URL object
     * Assumption: sessionID is non-null
     */
    public Pair<String, String> retrieveDetails(Integer sessionID, URL url)
	throws InvalidSessionIDException, MalformedURLException, NoSuchURLException
    {
	//check that the session ID exists
	String passbookUsername = userIDs.get(sessionID);
	if (passbookUsername == null) {
	    throw new InvalidSessionIDException(sessionID);
	}
	else if (!Arrays.asList(VALID_URL_PROTOCOLS).contains(url.getProtocol())) {
	    throw new MalformedURLException(passbookUsername);
	}

	PasswordTable pt = details.get(passbookUsername);
	//if this returned nothing, the user has no details for any url
	if (pt == null) {
	    throw new NoSuchURLException(passbookUsername, url);
	}

	Pair<String, String> pair = pt.get(url);

	//if this returned nothing, the user does not have a login for this url
	if (pair == null) {
	    throw new NoSuchURLException(passbookUsername, url);
	}

	return pair;
    }

    //A simple label to improve code readability
    private class PasswordTable extends HashMap<URL, Pair<String, String>> {}
}
