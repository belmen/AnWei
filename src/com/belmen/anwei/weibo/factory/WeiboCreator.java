package com.belmen.anwei.weibo.factory;

import java.lang.reflect.Constructor;

import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.oauth.AppendMethod;
import com.belmen.anwei.oauth.HMACSha1SignatureService;
import com.belmen.anwei.oauth.OAuth10Signer;
import com.belmen.anwei.oauth.OAuth20Signer;
import com.belmen.anwei.oauth.OAuthSigner;
import com.belmen.anwei.oauth.PlainTextSignatureService;
import com.belmen.anwei.oauth.SignatureService;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboService;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public class WeiboCreator {

	public static Weibo create(String factoryClassName) {
		return create(getFactory(factoryClassName));
	}
	
	private static WeiboFactory getFactory(String factoryClassName) {
		try {
			String fullName = WeiboFactory.class.getPackage().getName() + "." + factoryClassName;
			return Class.forName(fullName).asSubclass(WeiboFactory.class).newInstance();
		} catch (Exception e) {
			throw new WeiboException("Error while loading weibo factory", e);
		}
	}
	
	public static Weibo create(WeiboFactory factory) {
		OAuthConfig config = factory.getOAuthConfig();
		WeiboService service = createWeiboService(config);
		WeiboExtractor extractor = createExtractor(factory);
		return createWeibo(factory, service, config, extractor);
	}
	
	/*
	public static Weibo create(Class<? extends WeiboFactory> factoryClass) {
		WeiboFactory factory = createFactory(factoryClass);
		
		OAuthConfig config = factory.getOAuthConfig();
		WeiboService service = createWeiboService(config);
		WeiboExtractor extractor = createExtractor(factory);
		return createWeibo(factory, service, config, extractor);
	}

	private static WeiboFactory createFactory(Class<? extends WeiboFactory> factoryClass) {
		WeiboFactory factory = null;
		try {
			factory = factoryClass.newInstance();
		} catch (Exception e) {
			throw new WeiboException("Error while loading weibo factory", e);
		}
		return factory;
	}
	*/
	private static WeiboService createWeiboService(OAuthConfig config) {
		return new WeiboService(createOAuthSigner(config));
	}
	
	private static OAuthSigner createOAuthSigner(OAuthConfig config) {
		OAuthSigner signer = null;
		AppendMethod method = config.getTokenAppendMethod();
		switch(config.getOAuthVersion()) {
		case OAuth10:
			SignatureService service = null;
			switch(config.getSignatureAlgorithm()) {
			case HmacSHA1: service = new HMACSha1SignatureService(); break;
			case PlainText: service = new PlainTextSignatureService(); break;
			}
			signer = new OAuth10Signer(config.getConsumerKey(),
					config.getConsumerSecret(), config.getCallbackUrl(), method, service,
					config.excludePostParamsWhenMultipart());
			break;
		case OAuth20:
			switch(method) {
			case Header:
				signer = new OAuth20Signer(); break;
			case Query:
				signer = new OAuth20Signer(config.getAccessTokenParamName()); break;
			}
			break;
		}
		return signer;
	}
	
	private static WeiboExtractor createExtractor(WeiboFactory factory) {
		Class<? extends WeiboExtractor> extractorClass =
				factory.getExtractorImplementation();
		String name = factory.getWeiboName();
		
		WeiboExtractor extractor = null;
		try {
			Class<?>[] consType = new Class[] {String.class};
			Constructor<?> constructor = extractorClass.getConstructor(consType);
			Object[] consParams = new Object[] {name};
			extractor = (WeiboExtractor) constructor.newInstance(consParams);
		} catch (Exception e) {
			throw new WeiboException("Error while loading weibo extractor", e);
		}
		return extractor;
	}
	
	private static Weibo createWeibo(WeiboFactory factory,
			WeiboService service, OAuthConfig config, WeiboExtractor extractor) {
		String name = factory.getWeiboName();
		String url = factory.getApiBaseUrl();
		String facName = factory.getClass()
				.asSubclass(WeiboFactory.class).getSimpleName();
		
		Class<? extends Weibo> weiboClass =
				factory.getWeiboImplementation();
		Weibo weibo = null;
		try {
			Class<?>[] consType = new Class[] {String.class, String.class, String.class,
					WeiboService.class, OAuthConfig.class, WeiboExtractor.class};
			Constructor<?> constructor = weiboClass.getConstructor(consType);
			Object[] consParams = new Object[] {name, url, facName, service, config, extractor};
			weibo = (Weibo) constructor.newInstance(consParams);
		} catch (Exception e) {
			throw new WeiboException("Error while loading weibo", e);
		}
		return weibo;
	}
}
