package cn.obcc.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class HttpUtils {
	public static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	private static OkHttpClient okHttpClient;

	public HttpUtils() {
	}

	public static String post(String url, Map<String, String> headersParams, Map<String, String> bodyParams)
			throws IOException {
		Request.Builder builder = (new Request.Builder()).url(url);
		builder.headers(setHeaders(headersParams));
		builder.post(setRequestBody(bodyParams));
		Request request = builder.build();
		Call call = okHttpClient.newCall(request);
		return call.execute().body().string();
	}

	public static String get(String url, Map<String, String> headersParams) throws IOException {
		return get(url, (Map) null, (Map) null);
	}

	public static String get(String url, Map<String, String> params, Map<String, String> headersParams)
			throws IOException {
		Request request = (new Request.Builder()).headers(setHeaders(headersParams)).url(url + setGetParams(params))
				.get().build();
		Call call = okHttpClient.newCall(request);
		return call.execute().body().string();
	}

	public static String postJson(String url, String json, Map<String, String> headersParams) throws IOException {
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JSON, json);
		OkHttpClient okHttpClient = new OkHttpClient();
		Request.Builder builder = (new Request.Builder()).url(url);
		builder.headers(setHeaders(headersParams));
		builder.post(body);
		Request request = builder.build();
		Call call = okHttpClient.newCall(request);
		return call.execute().body().string();
	}

	public static String post(String url, Map<String, String> bodyParams) throws IOException {
		return post(url, (Map) null, bodyParams);
	}

	public static <T> T post(String url, Map<String, String> bodyParams, Class<T> clz) throws IOException {
		String str = post(url, bodyParams);
		return JSON.parseObject(str, clz);
	}

	public static <T> T get(String url, Map<String, String> params, Class<T> cls) throws IOException {
		String str = get(url, params, (Map) null);
		return JSON.parseObject(str, cls);
	}

	public static <T> T get(String url, Class<T> cls) throws IOException {
		String str = get(url, (Map) null);
		return JSON.parseObject(str, cls);
	}

	public static String postFile(String url, Map<String, String> headersParams, Map<String, Object> bodyParams)
			throws IOException {

		Request.Builder builder = new Request.Builder().url(url);

		builder.headers(setHeaders(headersParams));

		builder.post(setMultipartRequestBody(bodyParams));

		Request request = builder.build();
		Call call = okHttpClient.newCall(request);

		return call.execute().body().string();

	}

	private static void setCommonHeader(Map<String, String> headersParams, String name, String value) {
		if (!headersParams.containsKey(name)) {
			headersParams.put(name, value);
		}

	}

	private static Headers setHeaders(Map<String, String> headersParams) {
		Headers headers = null;
		okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();
		if (headersParams == null) {
			headersParams = new HashMap();
		}

		if (headersParams != null) {
			Iterator<String> iterator = ((Map) headersParams).keySet().iterator();
			String key = "";

			while (iterator.hasNext()) {
				key = ((String) iterator.next()).toString();
				headersbuilder.add(key, (String) ((Map) headersParams).get(key));
			}
		}

		headers = headersbuilder.build();
		return headers;
	}

	private static RequestBody setRequestBody(Map<String, String> BodyParams) {
		RequestBody body = null;
		okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
		if (BodyParams != null) {
			Iterator<String> iterator = BodyParams.keySet().iterator();
			String key = "";

			while (iterator.hasNext()) {
				key = ((String) iterator.next()).toString();
				formEncodingBuilder.add(key, (String) BodyParams.get(key));
			}
		}

		body = formEncodingBuilder.build();
		return body;
	}

	private static String setGetParams(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		if (params != null && params.size() > 0) {
			Set<Map.Entry<String, String>> entrySet = params.entrySet();
			sb.append("?");

			for (Iterator var3 = entrySet.iterator(); var3.hasNext(); sb.append("&")) {
				Map.Entry<String, String> entry = (Map.Entry) var3.next();
				sb.append((String) entry.getKey());
				sb.append("=");

				try {
					sb.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
				} catch (UnsupportedEncodingException var6) {
					var6.printStackTrace();
				}
			}

			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	private static RequestBody setMultipartRequestBody(Map<String, Object> params) {

		MultipartBody.Builder builder = new MultipartBody.Builder();
		builder.setType(MultipartBody.FORM);

		if (params != null) {
			Iterator<String> iterator = params.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next();
				Object o = params.get(key);

				if (o == null) {
					continue;
				}

				if (o instanceof File) {
					File file = (File) o;
					builder.addFormDataPart(key, file.getName(),
							RequestBody.create(MediaType.parse("multipart/form-data"), file));
				} else {
					builder.addFormDataPart(key, o.toString());
				}
			}
		}

		return builder.build();
	}

	static {
//        String httpRetrySwitch = SpringInit.getConfig("com.sparkchain.http.retry.switch");
//        System.out.println("##################okhttp init###################" + httpRetrySwitch);

		boolean retry = false;
//        if (null != httpRetrySwitch) {
//            retry = Boolean.parseBoolean(httpRetrySwitch);
//        }
//        okHttpClient = (new OkHttpClient())
//                .newBuilder()
//                .connectTimeout(25L, TimeUnit.SECONDS)
//                .readTimeout(240L, TimeUnit.SECONDS)
//                .writeTimeout(240L, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(retry)
//                .build();

		okHttpClient = (new OkHttpClient()).newBuilder().connectTimeout(10L, TimeUnit.SECONDS)
				.readTimeout(15L, TimeUnit.SECONDS).writeTimeout(15L, TimeUnit.SECONDS).retryOnConnectionFailure(retry)
				.build();
		okHttpClient.dispatcher().setMaxRequests(100);
		okHttpClient.dispatcher().setMaxRequestsPerHost(60);
	}
}
