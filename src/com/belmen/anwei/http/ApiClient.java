package com.belmen.anwei.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.belmen.anwei.util.URLCodec;

public class ApiClient {

	private static final int CONNECTION_TIMEOUT_MS = 30 * 1000;
	private static final int SOCKET_TIMEOUT_MS = 30 * 1000;
	
	private HttpClient mClient;
	private static ApiClient mInstance = new ApiClient();
	
	public static final int OK = 200;
	
	public static ApiClient getInstance() {
		return mInstance;
	}
	
	private ApiClient() {
		initHttpClient();
	}
	
	private void initHttpClient() {
	    try {
	    	// Create and initialize HTTP parameters
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setMaxTotalConnections(params, 10);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

			// Create and initialize scheme registry
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
	    	
	    	KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			schemeRegistry.register(new Scheme("https", sf, 443));
			
			// Create an HttpClient with the ThreadSafeClientConnManager.
			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
					params, schemeRegistry);
			mClient = new DefaultHttpClient(cm, params);
		} catch (Exception e) {
			mClient = new DefaultHttpClient();
		}
	}
	
	public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
	
	public Response execute(ApiRequest request) throws HttpException {
		HttpUriRequest uriRequest = createRequest(request);
		//SetupHTTPConnectionParams(uriRequest);
		HttpResponse response = null;
		Response res = null;
		try {
			response = mClient.execute(uriRequest);
			res = new Response(response);
			//String str = res.asString();
		} catch (ClientProtocolException e) {
			throw new HttpException(e.getMessage(), e);
		} catch (IOException e) {
			throw new HttpException(e.getMessage(), e);
		}
		if(response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			handleResponseStatusCode(statusCode, res);
		}
		return res;
	}
	
	private HttpUriRequest createRequest(ApiRequest rawRequest) throws HttpException {
		HttpUriRequest uriRequest = null;
		String url = rawRequest.getCompleteUrl();
		switch(rawRequest.getMethod()) {
		case GET:
			uriRequest = new HttpGet(url);
			break;
		case POST:
			HttpPost post = new HttpPost(url);
			HttpEntity entity = null;
			try {
				if(rawRequest.hasFile()) {
					entity = createMultipartEntity(rawRequest.getFileName(), rawRequest.getFile(),
							rawRequest.getPostParams());
				} else {
					entity = new UrlEncodedFormEntity(rawRequest.getPostParams(), HTTP.UTF_8);
				}
			} catch (IOException e) {
				throw new HttpException(e.getMessage(), e);
			}
			post.setEntity(entity);
			uriRequest = post;
			break;
		}
		setHeaders(uriRequest, rawRequest.getHeaders());
		return uriRequest;
	}
	
	private MultipartEntity createMultipartEntity(String filename, File file,
			List<NameValuePair> postParams) throws UnsupportedEncodingException {
		MultipartEntity entity = new MultipartEntity();
		for (NameValuePair param : postParams) {
			entity.addPart(param.getName(),
					new StringBody(param.getValue(), Charset.defaultCharset()));
		}
		entity.addPart(filename, new FileBody(file));
		return entity;
	}
	
	private void setHeaders(HttpUriRequest request, List<NameValuePair> headers) {
		if(headers.isEmpty()) return;
		for(NameValuePair header : headers) {
			request.addHeader(header.getName(), header.getValue());
		}
	}
	
	private void SetupHTTPConnectionParams(HttpUriRequest method) {
		HttpConnectionParams.setConnectionTimeout(method.getParams(),
				CONNECTION_TIMEOUT_MS);
		HttpConnectionParams
				.setSoTimeout(method.getParams(), SOCKET_TIMEOUT_MS);
		//mClient.setHttpRequestRetryHandler(requestRetryHandler);
		method.addHeader("Accept-Encoding", "gzip, deflate");
		method.addHeader("Accept-Charset", "UTF-8,*;q=0.5");
	}
	
	private void handleResponseStatusCode(int statusCode, Response res) throws HttpException {
		switch(statusCode) {
		case OK: break;
		default: throw new HttpException(res.asString(), statusCode);
		}
	}
}
