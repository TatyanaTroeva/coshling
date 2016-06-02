/**
 * 
 * @author BRSLABS
 *
 */
package com.coshling.connection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpManager
{
	private static DefaultHttpClient sClient ;
	static
	{
		// Set basic data
		HttpParams params = new BasicHttpParams() ;
		HttpProtocolParams.setVersion( params, HttpVersion.HTTP_1_1 ) ;
		HttpProtocolParams.setContentCharset( params, "UTF-8" ) ;
		HttpProtocolParams.setUseExpectContinue( params, true ) ;

		// Make pool
		ConnPerRoute connPerRoute = new ConnPerRouteBean( 12 ) ;
		ConnManagerParams.setMaxConnectionsPerRoute( params, connPerRoute ) ;
		ConnManagerParams.setMaxTotalConnections( params, 200 ) ;

		// Set timeout
		HttpConnectionParams.setStaleCheckingEnabled( params, false ) ;
		HttpConnectionParams.setConnectionTimeout( params, 5000 ) ; // 5 seconds
		HttpConnectionParams.setSoTimeout( params, 10 * 60 * 1000 ) ; // 10 minutes
		HttpConnectionParams.setSocketBufferSize( params, 4096 ) ;

		// Some client params
		HttpClientParams.setRedirecting( params, false ) ;

		// Register http/s shemas!
		SchemeRegistry schReg = new SchemeRegistry() ;
		schReg.register( new Scheme( "http", PlainSocketFactory.getSocketFactory(), 80 ) ) ;
		schReg.register( new Scheme( "https", SSLSocketFactory.getSocketFactory(), 443 ) ) ;
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager( params, schReg ) ;
		sClient = new DefaultHttpClient( conMgr, params ) ;
	}

	public static HttpResponse execute( HttpHead head ) throws IOException
	{
		return sClient.execute( head ) ;
	}

	public static HttpResponse execute( HttpHost host, HttpGet get ) throws IOException
	{
		return sClient.execute( host, get ) ;
	}

	public static HttpResponse execute( HttpGet get ) throws IOException
	{
		return sClient.execute( get ) ;
	}

	public static HttpResponse execute( HttpPost post ) throws IOException
	{
		return sClient.execute( post ) ;
	}

	public static synchronized CookieStore getCookieStore()
	{
		return sClient.getCookieStore() ;
	}
	
	public static synchronized void close()
	{
		if ( sClient != null )
		{
			sClient.clearRequestInterceptors() ;
			sClient.clearResponseInterceptors() ;
			sClient.getConnectionManager().closeExpiredConnections() ;
			sClient.getConnectionManager().closeIdleConnections( 100, TimeUnit.MILLISECONDS ) ;
			sClient.getConnectionManager().shutdown() ;
			sClient = null ;
		}
	}
}