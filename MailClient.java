/**
 * A class to model a simple email client. The client is run by a
 * particular user, and sends and retrieves mail via a particular server.
 * 
 * @author David J. Barnes and Michael KÃ¶lling
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
    
    private MailItem ultimoMensaje;

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
        ultimoMensaje = null;
    }

    /**
     * Return the next mail item (if any) for this user.
     */
    public MailItem getNextMailItem()
    {
        //Recibimos  algo  del servidor
        MailItem item = server.getNextMailItem(user);
        
        //Si lo que recibimos es un mensaje de correo...
        if (item != null)
        {
            //Compruebo si el mensaje es spam
            if (item.getMessage().contains("regalo") ||
                item.getMessage().contains("promocion"))
            {
                item = null;
            }
            
            //En caso de no ser spam...
            else
            {
                ultimoMensaje = item;
            }
        }
        
        //Si lo que recibimos es un email y la  respuesta automática está activa...
        if (answerMode && item != null)
        { 
            //Enviamos un correo de respuesta  automáticamente
            //creamos el email
            MailItem email =  new MailItem (user, item.getFrom(),
                                            autoSubject, autoMessage);
            //Enviamos el email
            server.post(email);
            
            /**otra forma de enviar el mensaje:
             *  sendMailItem(item.getFrom(), autoMessage, autoSubject);
             */
        }
        //Devolvemos lo recibido por el servidor
        return item;
    }

    /**
     * Print the next mail item (if any) for this user to the text 
     * terminal.
     */
    public void printNextMailItem()
    {
        MailItem item = server.getNextMailItem(user);
        
        if (item != null)
        {
            ultimoMensaje = item;
        }
        
        if(item == null) 
        {
            System.out.println("No new mail.");
        }
        else 
        {
            if (item.getMessage().contains ("regalo") ||
                item.getMessage().contains ("promocion"))
                {
                    //El email es spam
                    System.out.println("Has recibido un mensaje con spam");
                }
            else 
            {
                //El mensaje no es spam
                item.print();
            }
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
    
    //Habilita o deshabilita la respuesta automática
    public void setAutomaticAnswer()
    {
        if (answerMode)
        {
            answerMode = false;
        }
        else
        {
            answerMode = true;           
        }
    }
    
    /**
     * Permite configurar el texto del asunto y del mensaje 
     * de la respuesta automatica
     */
    public void configurarRespuestaAutomatica (String subjectAuto, String messageAuto)
    {
        this.autoSubject = subjectAuto;
        this.autoMessage = messageAuto;
    }
    
    public void numberMail()
    {
        System.out.println("emails almacenados: " +server.howManyMailItems(user));
    }
    
    public void printUltimoMensaje()
    {
        if (ultimoMensaje == null)
        {
            System.out.println ("No ha recibido mensajes");
        }
        else
        {
            ultimoMensaje.print();
        }
    }
}