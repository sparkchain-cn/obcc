package com.sparkchain.aries.chain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Member;
import com.ecwid.consul.v1.agent.model.Service;
import com.ecwid.consul.v1.health.model.Check;

import io.swagger.annotations.ApiOperation;

@RestController
public class ApiController {
	private static Logger log = LoggerFactory.getLogger(ApiController.class);
	@Autowired
	private ConsulClient consulClient;
	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.cloud.consul.host:127.0.0.1}")
	String host;
	@Value("${spring.cloud.consul.port:8500}")
	int port;

//	/**
//	 * 获取所有服务
//	 */
//	@ApiOperation(value = "剔除所有无效的服务实例", notes = "剔除所有无效的服务实例")
//	@RequestMapping(value = "/allservicer", method = RequestMethod.GET)
//	public void getAllServicer() {
//		log.info("***********************consul上无效服务清理开始*******************************************");
//		// 获取所有的members的信息
//		List<Member> members = consulClient.getAgentMembers().getValue();
//		for (int i = 0; i < members.size(); i++) {
//			// 获取每个member的IP地址
//			String address = members.get(i).getAddress();
//			log.info("member的IP地址为:{}", address);
//			// 根据role变量获取每个member的角色 role：consul---代表服务端 role：node---代表客户端
//			String role = members.get(i).getTags().get("role");
//			log.info("{}机器的role为：{}=====注释*role为consul代表服务端   role为node代表客户端", address, role);
//			// 判断是否为client
//			if (role.equals("node")) {
//				// 将IP地址传给ConsulClient的构造方法，获取对象
//				ConsulClient clearClient = new ConsulClient(address);
//				// 根据clearClient，获取当前IP下所有的服务 使用迭代方式 获取map对象的值
//				Iterator<Map.Entry<String, Service>> it = clearClient.getAgentServices().getValue().entrySet()
//						.iterator();
//				while (it.hasNext()) {
//					// 迭代数据
//					Map.Entry<String, Service> serviceMap = it.next();
//					// 获得Service对象
//					Service service = serviceMap.getValue();
//					// 获取服务名称
//					String serviceName = service.getService();
//					// 获取服务ID
//					String serviceId = service.getId();
//					log.info("在{}客户端上的服务名称 :{}**服务ID:{}", address, serviceName, serviceId);
//					// 根据服务名称获取服务的健康检查信息
//					Response<List<Check>> checkList = consulClient.getHealthChecksForService(serviceName, null);
//					List<Check> checks = checkList.getValue();
//					// 获取健康状态值 PASSING：正常 WARNING CRITICAL UNKNOWN：不正常
//					Check.CheckStatus checkStatus = checks.get(0).getStatus();
//					log.info("在{}客户端上的服务 :{}的健康状态值：{}", address, serviceName, checkStatus);
//					if (checkStatus != Check.CheckStatus.PASSING) {
//						log.info("在{}客户端上的服务 :{}为无效服务，准备清理...................", address, serviceName);
//						clearClient.agentServiceDeregister(serviceId);
//					}
//				}
//			}
//		}
//	}

	@RequestMapping("/consul/del")
	@ApiOperation(value = "Unregister Service Instance")
	public boolean unregisterInvalidServices() {

		log.info("***********************consul上无效服务清理开始*******************************************");

		// 获取所有的members的信息
	
		List<Member> members = consulClient.getAgentMembers().getValue();

		for (int i = 0; i < members.size(); i++) {

			// 获取每个member的IP地址
			// String address = members.get(i).getAddress();
			String address = host;
			log.info("member的IP地址为:{}", address);

			// 创造对象
			ConsulClient clearClient = new ConsulClient(address);

			// 根据clearClient，获取当前IP下所有的服务 使用迭代方式 获取map对象的值
			Iterator<Map.Entry<String, Service>> it = clearClient.getAgentServices().getValue().entrySet().iterator();

			while (it.hasNext()) {

				Map.Entry<String, Service> serviceMap = it.next();
				// 获得Service对象
				Service service = serviceMap.getValue();
				// 获取服务名称
				String serviceName = service.getService();
				// 获取服务ID
				String serviceId = service.getId();
				log.info("在{}客户端上的服务名称 :{}**服务ID:{}", address, serviceName, serviceId);

				// 根据服务名称获取服务的健康检查信息
				Response<List<Check>> checkList = consulClient.getHealthChecksForService(serviceName, null);

				List<Check> checks = checkList.getValue();

				// 获取健康状态值 PASSING：正常 WARNING CRITICAL UNKNOWN：不正常
				for (Check check : checks) {
					Check.CheckStatus checkStatus = check.getStatus();
					log.info("在{}客户端上的服务 :{},{} 的健康状态值：{}", address, serviceName, serviceId, checkStatus);
					if (check.getServiceId().equals(serviceId) && checkStatus != Check.CheckStatus.PASSING) {
						log.info("在{}客户端上的服务 :{},{}为无效服务，准备清理...................", address, serviceName, serviceId,
								serviceId);

						// 调用不了，PUT请求被封装成GET请求了
						// clearClient.agentServiceDeregister(serviceId);
						boolean delete = deleteInvalidService(address, serviceId, restTemplate);
						if (!delete) {
							log.error("unregister service id: " + serviceId + " failed.");

						} else {
							log.info("unregister service id: " + serviceId + " success.");
						}
					}
				}

			}
		}
		log.info("***********************consul上无效服务清理结束*******************************************");
		return true;
	}

	//curl -v -d "serviceId=spc-jingtum-lib"  -X PUT http://127.0.0.1:8500/v1/agent/service/deregister/consul
	private boolean deleteInvalidService(String ip, String serviceId, RestTemplate restTemplate) {
		String url = "http://" + ip + ":" + port + "/v1/agent/service/deregister/{serviceId}";

		Map<String, String> params = new HashMap<>();
		params.put("serviceId", serviceId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity httpEntity = new HttpEntity(null, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class,
				params);
		int statusCode = responseEntity.getStatusCodeValue();
		if (200 == statusCode) {
			return true;

		} else {

			return false;
		}
	}

}
