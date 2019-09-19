package swen90006.passbook;

public class WeakPassphraseException extends Exception
{
    public WeakPassphraseException (String passphrase)
    {
        super("Passphrase does not comply with the PassBook rules\n" +
	      "\t- must contains at least " +
	      PassBook.MINIMUM_PASSPHRASE_LENGTH + " characters\n" +
	      "\t- must contain at least one numeric character\n" +
	      "\t- must contain at least one lower case letter\n" +
	      "\t- must contain at least one upper case letter\n");
    }
}
