/***
 * Exception class for exceptions caused
 * by map values which are not appropriate
 * in the context given.
 */
public class MaPsciiContextException extends Exception
{
    /***
     * Constructor.
     */
    public MaPsciiContextException(){
        super();
    }

    /***
     * Constructor.
     * @param message Error message to
     *                report for the exception.
     */
    public MaPsciiContextException(String message){
        super(message);
    }
}
