
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**

 */
public class WorkerRunnable implements Runnable{
	
	
	
    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            DataInputStream dis = new DataInputStream(input);
            OutputStream output = clientSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(output);
            long time = System.currentTimeMillis();
                 
            int length = dis.readInt();
            byte[] bArray = new byte[length];
			dis.readFully(bArray, 0, length);
			//fromClientSocket.getInputStream().read(bArray);
			//ByteArrayInputStream bis = new ByteArrayInputStream(bArray);
			//fromClient = new ObjectInputStream(bis);
			
			//PaintObject po = (PaintObject) SerializationUtils.deserialize(bArray);
			
			
			//System.out.println("Received Object : "+ po.kuchBhi);
			
			dos.writeInt(length);
			dos.write(bArray);
            
            
          
           
            output.close();
            input.close();
            dis.close();
            dos.close();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}