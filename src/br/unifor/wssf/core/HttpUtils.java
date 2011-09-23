package br.unifor.wssf.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class HttpUtils {

    public synchronized static URL getURL(byte[] request) throws MalformedURLException{
		
    	String data = new String(request);
		int pos = data.indexOf(" ");
		String URLStr = data.substring(pos+1, data.indexOf(" ", pos+1));
		URL url = new URL(URLStr);
        
        return url;
    	
	}
    
    public synchronized static byte[] replaceURL(URL url,URL newURL, byte[] request){
    	String reqStr= new String(request);
    	reqStr = reqStr.replaceAll(Pattern.quote(url.toString()),newURL.toString());
    	reqStr = reqStr.replaceAll(Pattern.quote(url.getHost()),newURL.getHost());
    	return reqStr.getBytes();
    }
    
    public synchronized static int getResponseCode(byte[] response) {    	
    	//TODO: Solu��o provis�ria. Posteriormente substituir por HTTPClient (Apache)
    	int responseCode = -1;
    	String responseCodeStr = "";
    	String data;
		try {
			data = readLine(new ByteArrayInputStream(response));
	    	System.out.println(data);
	    	int pos = -1;
			if (data != null)
			{
				//header.append(data + "\r\n");
				pos = data.indexOf(" ");
				if ((data.toLowerCase().startsWith("http")) && 
					(pos >= 0) && (data.indexOf(" ", pos+1) >= 0))
				{
					responseCodeStr = data.substring(pos+1, data.indexOf(" ", pos+1));
					responseCode = Integer.parseInt(responseCodeStr);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return responseCode;
    }

    private synchronized static String readLine (InputStream in) throws IOException
	{
		// reads a line of text from an InputStream
		StringBuffer data = new StringBuffer("");
		int c;

			// if we have nothing to read, just return null
			in.mark(1);
			if (in.read() == -1)
				return null;
			else
				in.reset();
			
			while ((c = in.read()) >= 0)
			{
				// check for an end-of-line character
				if ((c == 0) || (c == 10) || (c == 13))
					break;
				else
					data.append((char)c);
			}
		
			// deal with the case where the end-of-line terminator is \r\n
			if (c == 13)
			{
				in.mark(1);
				if (in.read() != 10)
					in.reset();
			}
		
		// and return what we have
		return data.toString();
	}
    
    public static synchronized String getHTTPRequestFieldValue(byte[] request, String field){
    	
    	InputStream in = new ByteArrayInputStream(request);
    	String data = "";
    	int pos = -1;
    	
    	// get the rest of the header info
		try {
			while ((data = readLine(in)) != null) {
				// the header ends at the first blank line
				if (data.length() == 0)
					break;

				// check for the Host header
				pos = data.toLowerCase().indexOf(field.toLowerCase());
				if (pos >= 0) {
					return data.substring(pos + (field.length()+1)).trim();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
    }
}
