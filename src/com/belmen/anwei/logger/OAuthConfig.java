package com.belmen.anwei.logger;

import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.oauth.AppendMethod;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.oauth.SignatureType;

public interface OAuthConfig {

	/**
	 * 返回OAuth的版本
	 * @return
	 */
	public OAuthVersion getOAuthVersion();
	
	/**
	 * 返回Consumer Key
	 * @return
	 */
	public String getConsumerKey();
	
	/**
	 * 返回Consumer Secret
	 * @return
	 */
	public String getConsumerSecret();
	
	/**
	 * 返回Callback Url
	 * @return
	 */
	public String getCallbackUrl();
	
	/**
	 * 返回用户授权的地址，不需要带参数
	 * @return
	 */
	public String getAuthorizationUrl();
	
	/**
	 * 返回获取Access Token的方法
	 * @return
	 */
	public HttpMethod getAccessTokenMethod();
	
	/**
	 * 返回请求Access Token的地址，不需要带参数
	 * @return
	 */
	public String getAccessTokenUrl();
	
	/**
	 * 返回TokenExtractor，可根据版本选用OAuth10TokenExtractor或OAuth20TokenExtractor
	 * </br>
	 * 也可以自己实现接口
	 * @return
	 */
	public TokenExtractor getTokenExtractor();
	
	/**
	 * 返回使用Token调用API时出示Token的方法，大部分微博支持在Header中传递Token，
	 * </br>
	 * 部分微博需要在请求参数中传递Token（如腾讯微博）
	 * @return
	 */
	public AppendMethod getTokenAppendMethod(); 
	
	/**
	 * （OAuth 1.0专用）
	 * </br>
	 * 返回请求Request Token的方法
	 * @return
	 */
	public HttpMethod getRequestTokenMethod();
	
	/**
	 * （OAuth 1.0专用）
	 * </br>
	 * 返回请求Request Token的地址，不需要带参数
	 * @return
	 */
	public String getRequestTokenUrl();
	
	/**
	 * 有些微博要求在获取Request Token时加入Callback Url（如腾讯微博），此时需要将此值设为true
	 * </br>
	 * 默认情况下Callback Url都是在Authorize Url中加入的
	 * @return 默认为false
	 */
	//public boolean isCallbackUrlInGettingRequestToken();
	
	/**
	 * （OAuth1.0专用）
	 * </br>
	 * 返回签名的算法，目前多数微博只支持HMAC-SHA1算法
	 * @return
	 */
	public SignatureType getSignatureAlgorithm();
	
	/**
	 * （OAuth1.0专用）
	 * </br>
	 * 使用Multipart格式发送时，签名时是否排除POST参数
	 * @return
	 */
	public boolean excludePostParamsWhenMultipart();
	
	/**
	 * （OAuth1.0专用）
	 * </br>
	 * 返回是否使用XAuth，即通过输入用户名密码授权，否则需要在网页端授权
	 * @return
	 */
	public boolean isXAuth();
	
	/**
	 * （OAuth2.0专用）
	 * </br>
	 * 返回是否通过输入用户名和密码进行授权，否则需要在网页端授权
	 * @return
	 */
	public boolean isGrantTypePassword();
	
	/**
	 * （OAuth2.0专用）
	 * </br>
	 * 返回用户授权地址的display参数，若不使用网页授权可忽略此方法
	 * @return
	 */
	public String getDisplay();	
	
	/**
	 * （OAuth2.0专用）
	 * </br>
	 * 返回在Query参数中传递Access Token访问API时参数的名称
	 * @return
	 */
	public String getAccessTokenParamName();
	
}
