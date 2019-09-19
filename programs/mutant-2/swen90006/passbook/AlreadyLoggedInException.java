package swen90006.passbook;

public class AlreadyLoggedInException extends Exception 
{
    public AlreadyLoggedInException(String username)
    {
        super("Username already logged in: " + username);
    }
}
