<?php

include_once 'service/UserService.php';
include_once 'service/BikeService.php';
include_once 'service/CommentService.php';
include_once 'database/DatabaseConstant.php';

header("content-type:text/html; charset=utf-8");

/**
 * 分发器，根据sid分发给不同的service进行相关操作
 * @author XH's
 *
 */
class Dispatcher {
	/**
	 * 已注册的service集合
	 */
	private $serviceMap = array();
	
	/**
	 * 构造函数，在这里注册相关service
	 */
	public function Dispatcher() {
		$this->register(UserService::$SERVICE_ID, new UserService());
		$this->register(BikeService::$SERVICE_ID, new BikeService());
		$this->register(CommentService::$SERVICE_ID, new CommentService());
	}
	
	/**
	 * 注册函数，将service注册进相应的sid
	 * @param $sid 对应service的id
	 * @param $svc service实例
	 */
	public function register($sid, $svc) {
		$this->serviceMap[$sid] = $svc;
	}
	
	/**
	 * 分发请求操作，根据请求的sid去让相应service执行相关操作
	 * @param $msg 封装成json的请求
	 * @throws Exception
	 */
	public function dispatch($msg) {
		if (isset($msg->{"sid"})) {
			$sid = $msg->{"sid"};
			if (isset($this->serviceMap[$sid])) {
				$svc = $this->serviceMap[$sid];
				return $svc->handle($msg);
			}
			else {
				throw new Exception("The server has no service :" . $sid);
			}
		}
		else {
			throw new Exception("Key Error!The Message has no key : sid!");
		}
	}
}


?>