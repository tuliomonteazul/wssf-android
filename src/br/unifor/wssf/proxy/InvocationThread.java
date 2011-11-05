package br.unifor.wssf.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.List;

import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;

public class InvocationThread extends WSSFInvocationThread {

	public InvocationThread(byte[] request) throws MalformedURLException {
		super(request);
	}

	// the socket to the remote server
	Socket server = null;

	// the socketTimeout is used to time out the connection to
	// the remote server after a certain period of inactivity;
	// the value is in milliseconds -- use zero if you don't want
	// a timeout
	public static final int DEFAULT_TIMEOUT = 20 * 1000;
	private int socketTimeout = DEFAULT_TIMEOUT;
	private int debugLevel = 0;
	private PrintStream debugOut = System.out;
	BufferedInputStream serverIn;

	public void setTimeout(int timeout) {
		// assume that the user will pass the timeout value
		// in seconds (because that's just more intuitive)
		socketTimeout = timeout * 1000;
	}

	public void setDebug(int level, PrintStream out) {
		debugLevel = level;
		debugOut = out;
	}

	public void run() {
		List<WSSFInvocationListener> list = getInvocationListenerList();
		
		try {
			
			server = new Socket(getHostName(), getHostPort());
			setConnectionTime(System.currentTimeMillis());
			//TODO: CHECAR A NECESSIDADE DE VERIFICAR SE O SERVER EST� NULL 
			if (server != null) {

				if(!isRunning()){
					closeConnection();
					return;
				}

				for (WSSFInvocationListener listener : list) {
					listener.serverConnectionOpened(this);
				}					

				server.setSoTimeout(socketTimeout);
				serverIn = new BufferedInputStream(server.getInputStream());
				
				
				BufferedOutputStream serverOut = new BufferedOutputStream(
						server.getOutputStream());

				// send the request out
				serverOut.write(getRequest(), 0, Array.getLength(getRequest()));
				serverOut.flush();
				
				// and get the response; if we're not at a debug level that
				// requires us to return the data in the response, just stream
				// it back to the client to save ourselves from having to
				// create and destroy an unnecessary byte array. Also, we
				// should set the waitForDisconnect parameter to 'true',
				// because some servers (like Google) don't always set the
				// Content-Length header field, so we have to listen until
				// they decide to disconnect (or the connection times out).
				byte[] response = null;
				if (debugLevel > 1) {
					response = getHTTPData(serverIn, true);
					// responseLength = Array.getLength(response);
				} else {
					ByteArrayOutputStream bs = new ByteArrayOutputStream();
					streamHTTPData(serverIn, bs, true);
					response = bs.toByteArray();
				}
				
				if(!isRunning()){
					return;
				}
				//System.out.println(new String(response));
				for (WSSFInvocationListener listener : list)
					listener.serverResponseReceived(this, response);

				serverIn.close();
				serverOut.close();
			}
		} catch (Exception e) {
			if(isRunning()){
				for (WSSFInvocationListener listener : list)
					listener.serverExceptionOccurred(this, e);
				if (debugLevel > 0 )
					debugOut.println("Error getting HTTP data: " + e);				
			}
		}
	}

	private byte[] getHTTPData(InputStream in, boolean waitForDisconnect) {
		// get the HTTP data from an InputStream, and return it as
		// a byte array
		// the waitForDisconnect parameter tells us what to do in case
		// the HTTP header doesn't specify the Content-Length of the
		// transmission
		StringBuffer foo = new StringBuffer("");
		return getHTTPData(in, foo, waitForDisconnect);
	}

	private byte[] getHTTPData(InputStream in, StringBuffer host,
			boolean waitForDisconnect) {
		// get the HTTP data from an InputStream, and return it as
		// a byte array, and also return the Host entry in the header,
		// if it's specified -- note that we have to use a StringBuffer
		// for the 'host' variable, because a String won't return any
		// information when it's used as a parameter like that
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		streamHTTPData(in, bs, host, waitForDisconnect);
		return bs.toByteArray();
	}

	private int streamHTTPData(InputStream in, OutputStream out,
			boolean waitForDisconnect) {
		StringBuffer foo = new StringBuffer("");
		return streamHTTPData(in, out, foo, waitForDisconnect);
	}

	private int streamHTTPData(InputStream in, OutputStream out,
			StringBuffer host, boolean waitForDisconnect) {
		// get the HTTP data from an InputStream, and send it to
		// the designated OutputStream
		StringBuffer header = new StringBuffer("");
		String data = "";
		int responseCode = 200;
		int contentLength = 0;
		int pos = -1;
		int byteCount = 0;
		List<WSSFInvocationListener> list = getInvocationListenerList();
		
		try {
			
			// get the first line of the header, so we know the response code
			data = readLine(in);
			if (data != null) {
				header.append(data + "\r\n");
				pos = data.indexOf(" ");
				if ((data.toLowerCase().startsWith("http")) && (pos >= 0)
						&& (data.indexOf(" ", pos + 1) >= 0)) {
					String rcString = data.substring(pos + 1, data.indexOf(" ",
							pos + 1));
					try {
						responseCode = Integer.parseInt(rcString);
					} catch (Exception e) {
						if (debugLevel > 0)
							debugOut.println("Error parsing response code "
									+ rcString);
					}
				}
			}

			// get the rest of the header info
			while ((data = readLine(in)) != null) {
				// the header ends at the first blank line
				if (data.length() == 0)
					break;
				header.append(data + "\r\n");

				// check for the Host header
				pos = data.toLowerCase().indexOf("host:");
				if (pos >= 0) {
					host.setLength(0);
					host.append(data.substring(pos + 5).trim());
				}

				// check for the Content-Length header
				pos = data.toLowerCase().indexOf("content-length:");
				if (pos >= 0) {
					contentLength = Integer.parseInt(data.substring(pos + 15)
							.trim());
					setContentLength(contentLength);
				}
			}

			// add a blank line to terminate the header info
			header.append("\r\n");

			// convert the header to a byte array, and write it to our stream
			out.write(header.toString().getBytes(), 0, header.length());

			// if the header indicated that this was not a 200 response,
			// just return what we've got if there is no Content-Length,
			// because we may not be getting anything else
			if ((responseCode != 200) && (contentLength == 0)) {
				// out.flush();
				return header.length();
			}

			// get the body, if any; we try to use the Content-Length header to
			// determine how much data we're supposed to be getting, because
			// sometimes the client/server won't disconnect after sending us
			// information...
			if (contentLength > 0)
				waitForDisconnect = false;

			if ((contentLength > 0) || (waitForDisconnect)) {
				try {
					byte[] buf = new byte[4096];
					int bytesIn = 0;
					
					while (((byteCount < contentLength) || (waitForDisconnect))
							&& ((bytesIn = in.read(buf)) >= 0)) {
						out.write(buf, 0, bytesIn);
						byteCount += bytesIn;

						if(!isRunning()){
							return byteCount;
						}
						addBytesReceived(bytesIn);
						for (WSSFInvocationListener listener : list)
							listener.serverDataReceived(this, bytesIn);

					}
				} catch (Exception e) {
					String errMsg = "Error getting HTTP body: " + e;
					if (isRunning() && debugLevel > 0){
						for (WSSFInvocationListener listener : list)
							listener.serverExceptionOccurred(this, e);
						debugOut.println(errMsg);
					}
					// bs.write(errMsg.getBytes(), 0, errMsg.length());
				}
			}
		} catch (Exception e) {
			if (isRunning() && debugLevel > 0){
				for (WSSFInvocationListener listener : list)
					listener.serverExceptionOccurred(this, e);
				debugOut.println("Error getting HTTP data: " + e);
			}	
		}

		// flush the OutputStream and return
		// try { out.flush(); } catch (Exception e) {}
		return (header.length() + byteCount);
	}

	private String readLine(InputStream in) {
		// reads a line of text from an InputStream
		StringBuffer data = new StringBuffer("");
		int c;

		try {
			// if we have nothing to read, just return null
			in.mark(1);
			if (in.read() == -1)
				return null;
			else
				in.reset();

			while ((c = in.read()) >= 0) {
				// check for an end-of-line character
				if ((c == 0) || (c == 10) || (c == 13))
					break;
				else
					data.append((char) c);
			}

			// deal with the case where the end-of-line terminator is \r\n
			if (c == 13) {
				in.mark(1);
				if (in.read() != 10)
					in.reset();
			}
		} catch (Exception e) {
			if (isRunning() && debugLevel > 0){
				debugOut.println("Error getting header: " + e);
			}	
		}

		// and return what we have
		return data.toString();
	}

	@Override
	public void closeConnection() throws IOException {
		if (server != null && !server.isClosed()){
//			serverIn.close();
			try {
				server.shutdownInput();
			} catch (Exception e) {
				e.printStackTrace();
			}
			server.close();
		}	
	}
	
	public void stopInvocation() throws IOException {
		super.stopInvocation();
		for (WSSFInvocationListener listener : getInvocationListenerList()) {
			listener.serverConnectionClosed(this);
		}
	};

}