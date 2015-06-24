<?php
/**
 * 各类Service的基类
 * @author XH's
 *
 */
class Service {
	/**
	 * 已注册的命令集
	 */
	private $commandMap;
	
	/**
	 * 构造函数
	 */
	public function Service() {
	
	}
	
	/**
	 * 注册函数，将命令注册进相应的cid
	 * @param $cid 命令id
	 * @param $function 具体函数名
	 */
	public function register($cid, $function) {
		$this->commandMap[$cid] = $function;
	}
	
	/**
	 * 分发函数，根据cid调用相应的处理函数，若不存在，则抛出异常
	 * @param $msg 封装成json的请求
	 * @throws Exception
	 */
	public function handle($msg) {
		if (isset($msg->{"cid"})) {
			$cid = $msg->{"cid"};
			if (isset($this->commandMap[$cid])) {
				$function = $this->commandMap[$cid];
				return $this->$function($msg);
			}
			else {
				throw new Exception("The service has no command :" . $cid);
			}
		}
		else {
			throw new Exception("Key Error!The Message has no key : cid!");
		}
	}
}
?>