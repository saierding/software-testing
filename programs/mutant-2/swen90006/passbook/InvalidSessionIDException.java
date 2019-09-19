package swen90006.passbook;

public class InvalidSessionIDException extends Exception 
{
    public InvalidSessionIDException(Integer sessionID)
    {
        super("Invalid session ID: " + sessionID);
    }
}
