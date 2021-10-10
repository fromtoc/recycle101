package com.xin.portal.system.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 基于HttpClient实现的Http请求工具
 * 
 * @author 大漠知秋
 * @description 支持POST和GET请求,支持SSL
 * @description HttpClient 4.5.2
 * @description fastjson 1.2.31
 * 
 *              <dependency> <groupId>org.apache.httpcomponents</groupId>
 *              <artifactId>httpclient</artifactId> <version>4.5.2</version>
 *              </dependency>
 * 
 *              <dependency> <groupId>com.alibaba</groupId>
 *              <artifactId>fastjson</artifactId> <version>1.2.31</version>
 *              </dependency>
 */
public class HttpRequestUtils {

	/** 编码 */
	private static final String ENCODING = "UTF-8";

	/** 出错返回结果 */
	private static final String RESULT = "-1";

	/**
	 * 获取客户端连接对象
	 * 
	 * @param timeOut
	 *            超时时间
	 * @return
	 */
	private static CloseableHttpClient getHttpClient(Integer timeOut) {

		// 配置请求参数
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
				.setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
		// 配置超时回调机制
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 3) {// 如果已经重试了3次，就放弃
					return false;
				}
				if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
					return false;
				}
				if (exception instanceof InterruptedIOException) {// 超时
					return true;
				}
				if (exception instanceof UnknownHostException) {// 目标服务器不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException) {// ssl握手异常
					return false;
				}
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};

		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
				.setRetryHandler(retryHandler).build();

		return httpClient;

	}

	/**
	 * post请求,支持SSL
	 * 
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求头信息
	 * @param params
	 *            请求参数
	 * @param timeOut
	 *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
	 * @param isStream
	 *            是否以流的方式获取响应信息
	 * @param filePaht
	 * 			  文件下载路径
	 * @param clientContext
	 *            Http请求客户端上下文对象，包含Cookie
	 * @return 响应信息
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPost(String url, Map<String, Object> headers, Map<String, Object> params, Integer timeOut,
			boolean isStream, String filePaht, HttpClientContext clientContext) throws UnsupportedEncodingException {

		// 创建post请求
		HttpPost httpPost = new HttpPost(url);

		// 添加请求头信息
		if (null != headers) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}

		// 添加请求参数信息
		if (null != params) {
			httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(params), ENCODING));
		}

		return getResult(httpPost, timeOut, isStream, filePaht, clientContext);

	}

	/**
	 * post请求,支持SSL
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param timeOut
	 *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
	 * @param clientContext
	 *            Http请求客户端上下文对象，包含Cookie
	 * @return 响应信息
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPost(String url, Map<String, Object> params, Integer timeOut,
			HttpClientContext clientContext) throws UnsupportedEncodingException {

		// 创建post请求
		HttpPost httpPost = new HttpPost(url);

		// 添加请求参数信息
		if (null != params) {
			httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(params), ENCODING));
		}

		return getResult(httpPost, timeOut, true, null, clientContext);

	}

	/**
	 * post请求,支持SSL
	 * 
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求头信息
	 * @param params
	 *            请求参数
	 * @param timeOut
	 *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
	 * @param isStream
	 *            是否以流的方式获取响应信息
	 * @param clientContext
	 *            Http请求客户端上下文对象，包含Cookie
	 * @return 响应信息
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPost(String url, JSONObject headers, JSONObject params, Integer timeOut, boolean isStream,
			String filePaht, HttpClientContext clientContext) throws UnsupportedEncodingException {

		// 创建post请求
		HttpPost httpPost = new HttpPost(url);

		// 添加请求头信息
		if (null != headers) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}

		// 添加请求参数信息
		if (null != params) {
			httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(params), ENCODING));
		}

		return getResult(httpPost, timeOut, isStream, filePaht, clientContext);

	}

	/**
	 * post请求,支持SSL
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param timeOut
	 *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
	 * @param clientContext
	 *            Http请求客户端上下文对象，包含Cookie
	 * @return 响应信息
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPost(String url, JSONObject params, Integer timeOut, HttpClientContext clientContext)
			throws UnsupportedEncodingException {

		// 创建post请求
		HttpPost httpPost = new HttpPost(url);

		// 添加请求参数信息
		if (null != params) {
			httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(params), ENCODING));
		}

		return getResult(httpPost, timeOut, true, null, clientContext);

	}

	/**
	 * get请求,支持SSL
	 * 
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求头信息
	 * @param params
	 *            请求参数
	 * @param timeOut
	 *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
	 * @param isStream
	 *            是否以流的方式获取响应信息
	 * @param clientContext
	 *            Http请求客户端上下文对象，包含Cookie
	 * @return 响应信息
	 * @throws URISyntaxException
	 */
	public static String httpGet(String url, Map<String, Object> headers, Map<String, Object> params, Integer timeOut,
			boolean isStream, String filePaht, HttpClientContext clientContext) throws URISyntaxException {

		// 构建url
		URIBuilder uriBuilder = new URIBuilder(url);
		// 添加请求参数信息
		if (null != params) {
			uriBuilder.setParameters(covertParams2NVPS(params));
		}

		// 创建post请求
		HttpGet httpGet = new HttpGet(url);

		// 添加请求头信息
		if (null != headers) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}

		return getResult(httpGet, timeOut, isStream, filePaht, clientContext);

	}

	/**
	 * get请求,支持SSL
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param timeOut
	 *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
	 * @param clientContext
	 *            Http请求客户端上下文对象，包含Cookie
	 * @return 响应信息
	 * @throws URISyntaxException
	 */
	public static String httpGet(String url, Map<String, Object> params, Integer timeOut,
			HttpClientContext clientContext) throws URISyntaxException {

		// 构建url
		URIBuilder uriBuilder = new URIBuilder(url);
		// 添加请求参数信息
		if (null != params) {
			uriBuilder.setParameters(covertParams2NVPS(params));
		}

		// 创建post请求
		HttpGet httpGet = new HttpGet(url);

		return getResult(httpGet, timeOut, true, null, clientContext);

	}

	/**
	 * get请求,支持SSL
	 * 
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求头信息
	 * @param params
	 *            请求参数
	 * @param timeOut
	 *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
	 * @param isStream
	 *            是否以流的方式获取响应信息
	 * @param clientContext
	 *            Http请求客户端上下文对象，包含Cookie
	 * @return 响应信息
	 * @throws URISyntaxException
	 */
	public static String httpGet(String url, JSONObject headers, JSONObject params, Integer timeOut, boolean isStream,
			String filePaht, HttpClientContext clientContext) throws URISyntaxException {

		// 构建url
		URIBuilder uriBuilder = new URIBuilder(url);
		// 添加请求参数信息
		if (null != params) {
			uriBuilder.setParameters(covertParams2NVPS(params));
		}

		// 创建post请求
		HttpGet httpGet = new HttpGet(url);

		// 添加请求头信息
		if (null != headers) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}

		return getResult(httpGet, timeOut, isStream, filePaht, clientContext);

	}

	/**
	 * get请求,支持SSL
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param timeOut
	 *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
	 * @param clientContext
	 *            Http请求客户端上下文对象，包含Cookie
	 * @return 响应信息
	 * @throws URISyntaxException
	 */
	public static String httpGet(String url, JSONObject params, Integer timeOut, HttpClientContext clientContext)
			throws URISyntaxException {

		// 构建url
		URIBuilder uriBuilder = new URIBuilder(url);
		// 添加请求参数信息
		if (null != params) {
			uriBuilder.setParameters(covertParams2NVPS(params));
		}

		// 创建post请求
		HttpGet httpGet = new HttpGet(url);

		return getResult(httpGet, timeOut, true, null, clientContext);

	}

	/**
	 * 获取响应结果
	 * @param httpRequest
	 * @param timeOut
	 * @param isStream
	 * @param filePaht
	 * @param clientContext
	 * @return 文件名称
	 */
	private static String getResult(HttpRequestBase httpRequest, Integer timeOut, boolean isStream, String filePaht,
			HttpClientContext clientContext) {

		// 响应结果
		StringBuilder sb = null;

		CloseableHttpResponse response = null;

		try {
			// 获取连接客户端
			CloseableHttpClient httpClient = getHttpClient(timeOut);
			// 发起请求
			if (null != clientContext) {
				response = httpClient.execute(httpRequest, clientContext);
			} else {
				response = httpClient.execute(httpRequest);
			}

			int respCode = response.getStatusLine().getStatusCode();
			// 如果是重定向
			if (302 == respCode) {
				String locationUrl = response.getLastHeader("Location").getValue();
				return getResult(new HttpPost(locationUrl), timeOut, isStream, filePaht, clientContext);
			}
			// 正确响应
			if (200 == respCode) {
				// 获得响应实体
				HttpEntity entity = response.getEntity();
				sb = new StringBuilder();

				// 如果是以流的形式获取
				if (isStream) {
					String fileName = null;
					Header contentHeader = response.getFirstHeader("Content-Disposition");
					if (contentHeader != null) {
						HeaderElement[] values = contentHeader.getElements();
						if (values.length == 1) {
							String param = values[0].getValue();
							if (param != null) {
								fileName = URLDecoder.decode(param, "UTF-8");
							} else {
								NameValuePair param1 = values[0].getParameterByName("filename");
								fileName = URLDecoder.decode(param1.getValue(),"UTF-8");
							}
						}
					}
					InputStream in = entity.getContent();
					isChartPathExist(filePaht);
					File file = new File(filePaht + File.separator + fileName);
					FileOutputStream fout = new FileOutputStream(file);
					int a = -1;
					byte[] tmp = new byte[1024];
					while ((a = in.read(tmp)) != -1) {
						fout.write(tmp, 0, a);
					}
					fout.flush();
					fout.close();
					in.close();
					return fileName;
				} else {
					sb.append(EntityUtils.toString(entity, ENCODING));
					if (sb.length() < 1) {
						sb.append("-1");
					}
				}

			}
		} catch (ConnectionPoolTimeoutException e) {
			System.err.println("从连接池获取连接超时!!!");
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			System.err.println("响应超时");
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			System.err.println("请求超时");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.err.println("http协议错误");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.err.println("不支持的字符编码");
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			System.err.println("不支持的请求操作");
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println("解析错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO错误");
			e.printStackTrace();
		} finally {
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					System.err.println("关闭响应连接出错");
					e.printStackTrace();
				}
			}

		}

		return sb == null ? RESULT : ("".equals(sb.toString().trim()) ? "-1" : sb.toString());

	}

	/**
	 * Map转换成NameValuePair List集合
	 * 
	 * @param params
	 *            map
	 * @return NameValuePair List集合
	 */
	public static List<NameValuePair> covertParams2NVPS(Map<String, Object> params) {

		List<NameValuePair> paramList = new LinkedList<>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
		}

		return paramList;

	}

	private static void isChartPathExist(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}