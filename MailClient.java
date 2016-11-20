/**
 * A class to model a simple email client. The client is run by a
 * particular user, and sends and retrieves mail via a particular server.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class MailClient
{
    // The server used for sending and receiving.
    private MailServer server;
    // The user running this client.
    private String user;
    //Modo de autorespuesta.
    private boolean answerMode; 
   //Asunto de la autorespuesta
    private String autoSubject;
   //Mensaje de la autorespuesta
    private String autoMessage;

    /**
     * Create a mail client run by user and attached to the given server.
     */
    public MailClient(MailServer server, String user)
    {
        this.server = server;
        this.user = user;
        answerMode = false;
        autoSubject = "";
        autoMessage = "";
    }

    /**
     * Return the next mail item (if any) for this user.
     */
    public MailItem getNextMailItem()
    {
        MailItem item = server.getNextMailItem(user);
        if (answerMode && item != null)
        { 
            sendMailItem(item.getFrom(), autoMessage, autoSubject);
        }
        return item;
    }

    /**
     * Print the next mail item (if any) for this user to the text 
     * terminal.
     */
    public void printNextMailItem()
    {
        MailItem item = server.getNextMailItem(user);
        if(item == null) {
            System.out.println("No new mail.");
        }
        else {
            item.print();
        }
    }

    /**
     * Send the given message to the given recipient via
     * the attached mail server.
     * @param to The intended recipient.
     * @param message The text of the message to be sent.
     */
    public void sendMailItem(String to, String subject, String message)
    {
        MailItem item = new MailItem(user, to, subject, message);
        server.post(item);
    }
    
    public void setAutomaticAnswer(boolean answer)
    {
        answerMode = answer;
    }
    
    public void setAutoContent (String subject, String message)
    {
        autoSubject = subject;
        autoMessage = message;
    }
    
    public void numberMail()
    {
        System.out.println("emails almacenados: " +server.howManyMailItems(user));
    }
}