<?php

include_once 'Service.php';

header("content-type:text/html; charset=utf-8");

class UserService extends Service{
	/**
	 * 用于注册Service的Id
	 */
	public static $SERVICE_ID = 0;
	
	/**
	 * 用户注册命令的Id
	 */
	private static $REGISTER_ID = 0;
	private static $LOGIN_ID = 1;
	private static $GETUSERINFO = 2;
	private static $CHANGEDESCRIPTION = 3;
	private static $CHANGEPASSWORD = 4;
	
	/**
	 * 构造函数，在这里注册相应命令
	 */
	public function UserService() {
		$this->register(self::$REGISTER_ID, "handle_register");
		$this->register(self::$LOGIN_ID, "handle_login");
		$this->register(self::$GETUSERINFO, "getUserInfo");
		$this->register(self::$CHANGEDESCRIPTION, "changeDescription");
		$this->register(self::$CHANGEPASSWORD , "changePassword");
	}
	
	/**
	 * 用户注册
	 * @param $msg
	 * @return $returnMsg
	 * {"returnCode":0} ($msg中userName，password缺少，或者抛出异常)
	 * {"returnCode":1,"userId":userId}  (注册成功,返回注册用户的id)
	 * {"returnCode":-1} (用户已存在)
	 */
	public function handle_register($msg) {
		$returnMsg = array();
		try {	
			if (isset($msg->{"userName"}) && isset($msg->{"password"})) {
				$userName = $msg->{"userName"};
				$password = $msg->{"password"};
				$currentTime = date("y-m-d h:i:s",time());
				
				$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
				mysql_select_db("app_yibaosysu", $con);
				mysql_query("set names 'utf8'");
				
				/*查询用户名是否存在*/
				$sql = "SELECT * FROM userinfo WHERE userName = '$userName'";
				$result = mysql_query($sql);
				
				if (mysql_fetch_array($result)) {//如果用户名存在
					$returnMsg["returnCode"] = -1;
					return $returnMsg;
				}
				
				/*插入数据*/
				mysql_query("INSERT INTO userinfo (userName, password, time)
					VAlUES ('$userName', '$password', '$currentTime')", $con);
				
				/*获取用户id*/
				$userId = mysql_insert_id();
				
				mysql_close($con);
				$returnMsg["returnCode"] = 1;
				$returnMsg["userId"] = $userId;			
				return $returnMsg;
			}
			else {
				$returnMsg["returnCode"] = 0;
				return $returnMsg;
			}
		}
		catch (Exception $e) {
			$returnMsg["returnCode"] = 0;
			return $returnMsg;
		}
	}
	
	/**
	 * 处理登录
	 * @param  $msg
	 * @return $returnMsg
	 * {"returnCode":0} ($msg中userName、password缺少，或者抛出异常)
	 * {"returnCode":1,"userId":userId} (登录成功，返回登录用户id)
	 * {"returnCode":-1} (用户名不存在或着密码不正确)
	 */
	public function handle_login($msg){
		$returnMsg = array();
		try {
			if (isset($msg->{"userName"}) && isset($msg->{"password"})) {
				$userName = $msg->{"userName"};
				$password = $msg->{"password"};
				$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
				mysql_select_db("app_yibaosysu", $con);
				mysql_query("set names 'utf8'");
				
				/*是否存在*/
				$sql ="SELECT * FROM userinfo WHERE userName = '$userName'";
				$result= mysql_query($sql);
				mysql_close($con);
				
				/*用户名存在*/
				while ($row = mysql_fetch_array($result)) {	
					if ($password == $row["password"]) {       //密码正确
						$returnMsg["returnCode"] = 1;
						$returnMsg["userId"] = $row["userId"];
						return $returnMsg;
					}
					else {									   //密码不正确
						$returnMsg["returnCode"] = -1;
						return $returnMsg;
					}
				}
				/*用户名不存在*/
				$returnMsg["returnCode"] = -1;
				return $returnMsg;
			}
			else {
				$returnMsg["returnCode"] = 0;
				return $returnMsg;
			}
		}
		catch (Exception $e) {
				$returnMsg["returnCode"] = 0;
				return $returnMsg;
		}
	}
	
	/**
	 * 获取用户信息
	 * @param $msg
	 * @return $returnMsg
	 * {"returnCode":0} ($msg中userId缺少，或者抛出异常)
	 * {"returnCode":1,"userId":userId,"userName":userName,"time":time,"description":description} (成功，返回个人信息)
	 */
	public function getUserInfo($msg) {
		$returnMsg = array();
		try {
			if (isset($msg->{"userId"})) {
				$userId = $msg->{"userId"};
				
				$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
				mysql_select_db("app_yibaosysu", $con);
				mysql_query("set names 'utf8'");
		
				/*查询*/
				$sql ="SELECT * FROM userinfo WHERE userId = '$userId'";
				$result= mysql_query($sql);
				mysql_close($con);
		
				while ($row = mysql_fetch_array($result)) {
					$returnMsg["returnCode"] = 1;
					$returnMsg["userId"] = $row["userId"];
					$returnMsg["userName"] = $row["userName"];
					$returnMsg["time"] = $row["time"];
					$returnMsg["description"] = $row["description"];
					return $returnMsg;
				}
			}
			else {
				$returnMsg["returnCode"] = 0;
				return $returnMsg;
			}
		}
		catch (Exception $e) {
			$returnMsg["returnCode"] = 0;
			return $returnMsg;
		}
	}
	
	/**
	 * 更改个人描述
	 * @param $msg
	 * @return $returnMsg
	 * 
	 */
	public function changeDescription($msg) {
		$returnMsg = array();
		try {
			if (isset($msg->{"userId"}) && isset($msg->{"description"})) {
				$userId = $msg->{"userId"};
				$description = $msg->{"description"};
		
				$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
				mysql_select_db("app_yibaosysu", $con);
				mysql_query("set names 'utf8'");
		
				/*查询*/
				$sql ="UPDATE userinfo SET description = '$description' WHERE userId = '$userId' ";
				mysql_query($sql);
				mysql_close($con);
		
				$returnMsg["returnCode"] = 1;
				return $returnMsg;
			}
			else {
				$returnMsg["returnCode"] = 0;
				return $returnMsg;
			}
		}
		catch (Exception $e) {
			$returnMsg["returnCode"] = 0;
			return $returnMsg;
		}
	}
	
	/**
	 * 更改密码
	 * @param $msg
	 * {"returnCode":0} ($msg中userId、oldPassword、newPassword缺少，或者抛出异常)
	 * {"returnCode":1} (密码更改成功)
	 * {"returnCode":-1} (旧密码不正确)
	 */
	public function changePassword($msg) {
		$returnMsg = array();
		try {
			if (isset($msg->{"userId"}) && isset($msg->{"oldPassword"}) && isset($msg->{"newPassword"})) {
				$userId = $msg->{"userId"};
				$oldPassword = $msg->{"oldPassword"};
				$newPassword = $msg->{"newPassword"};
				
				$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
				mysql_select_db("app_yibaosysu", $con);
				mysql_query("set names 'utf8'");
		
				/*查询*/
				$sql ="SELECT * FROM userinfo WHERE userId = '$userId' ";
				$result = mysql_query($sql);
				/*旧密码正确*/
				while ($row = mysql_fetch_array($result)) {
					if ($oldPassword == $row["password"]) {
						$sql ="UPDATE userinfo SET password = '$newPassword' WHERE userId = '$userId' ";
						mysql_query($sql);
						mysql_close($con);
						
						$returnMsg["returnCode"] = 1;
						return $returnMsg;
					}
				}
				/*旧密码不正确*/
				mysql_close($con);
				$returnMsg["returnCode"] = -1;
				return $returnMsg;
			}
		}
		catch (Exception $e) {
			$returnMsg["returnCode"] = 0;
			return $returnMsg;
		}
	}
}
?>