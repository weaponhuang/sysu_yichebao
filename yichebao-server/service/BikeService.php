<?php

include_once 'Service.php';

header("content-type:text/html; charset=utf-8");

class BikeService extends Service{
	/**
	 * 用于注册Service的Id
	 */
	public static $SERVICE_ID = 1;
	
	/**
	 * 用户注册命令的Id
	 */
	private static $ADDBIKE_ID = 0;
	private static $GETBIKESINFO_ID = 1;
	private static $GETLABELS_ID = 2;
	private static $GETBIKESBYUSERID = 3;
	private static $GETBIKESBYBIKENAME = 4;
	private static $GETBIKESBYLABEL = 5;
	private static $DELETEBIKE = 6;
	/**
	 * 构造函数，在这里注册相应命令
	 */
	public function BikeService() {
		$this->register(self::$ADDBIKE_ID, "addBikeInfo");
		$this->register(self::$GETBIKESINFO_ID, "getBikesInfo");
		$this->register(self::$GETLABELS_ID, "getLabelsByBikeId");
		$this->register(self::$GETBIKESBYUSERID, "getBikesByUserId");
		$this->register(self::$GETBIKESBYBIKENAME, "getBikesByBikeName");
		$this->register(self::$GETBIKESBYLABEL, "getBikesByLabel");
		$this->register(self::$DELETEBIKE, "deleteBike");
	}
	
	/**
	 * 发布单车信息
	 * @param  $msg
	 * @return $returnMsg 
	 * {"returnCode":0} ($msg中bikeName、content、userId缺少，或者抛出异常)
	 * {"returnCode":1} (正常添加单车)
	 */
	public function addBikeInfo($msg) {
		$returnMsg = array();
		try {
			if (isset($msg->{"bikeName"}) && isset($msg->{"content"}) && isset($msg->{"userId"}) && isset($msg->{"labelArr"}))  {
				$bikeId;
				$bikeName = $msg->{"bikeName"};
				$authorId = $msg->{"userId"};
				$labelArr = $msg->{"labelArr"};
				$content = $msg->{"content"};
				$currentTime = date("y-m-d h:i:s",time());
				
				$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
				mysql_select_db("app_yibaosysu", $con);
				mysql_query("set names 'utf8'");
				
				/*插入单车信息*/
				mysql_query("INSERT INTO bikeinfo (bikeName,authorId,content,time,authorName)
					VAlUES ('$bikeName','$authorId','$content','$currentTime','$authorId')", $con);
				
				/*查询刚插入单车信息的id*/
				$bikeId = mysql_insert_id();
				
				/*插入数据*/
				for ($i = 0; $i < count($labelArr); $i++) {
					$labelContent = $labelArr[$i];
					mysql_query("INSERT INTO label (bikeId, content)
						VAlUES ('$bikeId', '$labelContent')", $con);
				}
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
	 * 获取单车信息
	 * @param  $msg
	 * @return $returnMsg 
	 * {"returnCode":0} ($msg中startBikeId、size缺少，或者抛出异常)
	 * {"returnCode":1,"bikeList":[{"bikeId":"", "bikeName":"","authorId":"","content":"","time":"","authorId":""},......]}
	 */
	public function getBikesInfo($msg) {
		$returnMsg = array();
		try {
			if (isset($msg->{"startBikeId"}) && isset($msg->{"size"})) {
				$startBikeId = $msg->{"startBikeId"};
				$size = $msg->{"size"};
				
				if (-1 == $startBikeId)  {
					$returnMsg["returnCode"] = 1;
					$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
					mysql_select_db("app_yibaosysu", $con);
					mysql_query("set names 'utf8'");
						
					/*查询单车信息*/
					$sql = "SELECT * FROM bikeinfo ORDER BY time DESC, bikeId DESC LIMIT $size ";
					$result = mysql_query($sql);
					$tempList = array();
					while($row = mysql_fetch_array($result)) {
						$tempMsg = array();
						$tempMsg["bikeId"] = $row["bikeId"];
						$tempMsg["bikeName"] = $row["bikeName"];
						$tempMsg["authorId"] = $row["authorId"];
						$authorId = $row["authorId"];
						$tempMsg["content"] = $row["content"];
						$tempMsg["time"] = $row["time"];
							
						/*获取作者的名字*/
						$sql_2 = "SELECT * FROM userinfo WHERE userId = '$authorId'";
						$result_2 = mysql_query($sql_2);
						if ($row_2 = mysql_fetch_array($result_2)) {
							$tempMsg["authorName"] = $row_2["userName"];
						}
						array_push($tempList, $tempMsg);
							
					}
					$returnMsg["bikeList"] = $tempList;
					mysql_close($con);
					return $returnMsg;
				}
				else{
					$returnMsg["returnCode"] = 1;
					$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
					mysql_select_db("app_yibaosysu", $con);
					mysql_query("set names 'utf8'");
						
					/*查询单车信息*/
					/*获取startBikeId对应的时间戳*/
					$time;
					$sql = "SELECT * FROM bikeinfo WHERE bikeid = '$startBikeId'";
					$result = mysql_query($sql);
					while ($row = mysql_fetch_array($result))  {
						$time = $row["time"];
					}
				
					$sql = "SELECT * FROM bikeinfo WHERE bikeId < $startBikeId ORDER BY time DESC LIMIT $size";
					$result = mysql_query($sql);
					$tempList = array();
					while($row = mysql_fetch_array($result)) {
						$tempMsg = array();
						$tempMsg["bikeId"] = $row["bikeId"];
						$tempMsg["bikeName"] = $row["bikeName"];
						$tempMsg["authorId"] = $row["authorId"];
						$authorId = $row["authorId"];
						$tempMsg["content"] = $row["content"];
						$tempMsg["time"] = $row["time"];
							
						/*获取作者的名字*/
						$sql_2 = "SELECT * FROM userinfo WHERE userId = '$authorId'";
						$result_2 = mysql_query($sql_2);
						if ($row_2 = mysql_fetch_array($result_2)) {
							$tempMsg["authorName"] = $row_2["userName"];
						}
						array_push($tempList, $tempMsg);
							
					}
					$returnMsg["bikeList"] = $tempList;
					mysql_close($con);
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
	 * 用单车id获取标签
	 * @param  $msg
	 * @return $returnMsg 
	 * {"returnCode":0} ($msg中bikeId缺少，或者抛出异常)
	 * {"returnCode":1,"bikeList":[{"bikeId":"", "bikeName":"","authorId":"","content":"","time":"","authorId":""},......]}
	 */
	public function getLabelsByBikeId($msg) {
		$returnMsg = array();
		try{
			if (isset($msg->{"bikeId"}))  {
				$labelArr = array();
				$bikeId = $msg->{"bikeId"};
				
				$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
				mysql_select_db("app_yibaosysu", $con);
				mysql_query("set names 'utf8'");
					
				/*查询标签信息*/
				$sql = "SELECT * FROM label WHERE bikeId = '$bikeId'";
				$result = mysql_query($sql);
				while ($row = mysql_fetch_array($result)) {
					array_push($labelArr, $row["content"]);
				}
				
				mysql_close($con);
					
				$returnMsg["returnCode"] = 1;
				$returnMsg["labelArr"] =  $labelArr;
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
	 *根据用户id获取他的有关书籍
	 * @param  $msg
	 * @return $returnMsg 
	 * {"returnCode":0} ($msg中userId、startBikeId、size缺少，或者抛出异常)
	 * {"returnCode":1,"bikeList":[{"bikeId":"", "bikeName":"","authorId":"","content":"","time":"","authorId":""},......]}
	 */
	public function getBikesByUserId($msg) {
		$returnMsg = array();
		try {
			if (isset($msg->{"userId"}) && isset($msg->{"startBikeId"}) && isset($msg->{"size"})) {
				$authorId = $msg->{"userId"};
				$authorName;
				$startBikeId = $msg->{"startBikeId"};
				$size = $msg->{"size"};
	
				if (-1 == $startBikeId)  {
					$returnMsg["returnCode"] = 1;
					$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
					mysql_select_db("app_yibaosysu", $con);
					mysql_query("set names 'utf8'");
	
					/*获取用户名字*/
					$sql = "SELECT * FROM userinfo WHERE userId = '$authorId'";
					$result = mysql_query($sql);
					while ($row = mysql_fetch_array($result)) {
						$authorName = $row["userName"];
					}
							
					/*查询单车信息*/
					$sql = "SELECT * FROM bikeinfo WHERE authorId = '$authorId' ORDER BY time DESC, bikeId DESC LIMIT $size ";
					$result = mysql_query($sql);
					$tempList = array();
					while($row = mysql_fetch_array($result)) {
						$tempMsg = array();
						$tempMsg["bikeId"] = $row["bikeId"];
						$tempMsg["bikeName"] = $row["bikeName"];
						$tempMsg["authorId"] = $row["authorId"];
						$authorId = $row["authorId"];
						$tempMsg["content"] = $row["content"];
						$tempMsg["time"] = $row["time"];
						$tempMsg["authorName"] = $authorName;

						array_push($tempList, $tempMsg);
							
					}
					$returnMsg["bikeList"] = $tempList;
					mysql_close($con);
					return $returnMsg;
				}
				else{
					$returnMsg["returnCode"] = 1;
					$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
					mysql_select_db("app_yibaosysu", $con);
					mysql_query("set names 'utf8'");
					
					/*获取用户名字*/
					$sql = "SELECT * FROM userinfo WHERE userId = '$authorId'";
					$result = mysql_query($sql);
					while ($row = mysql_fetch_array($result)) {
						$authorName = $row["userName"];
					}
					
					
					/*获取startBikeId对应的时间戳*/
					$time;
					$sql = "SELECT * FROM bikeinfo WHERE bikeid = '$startBikeId'";
					$result = mysql_query($sql);
					while ($row = mysql_fetch_array($result))  {
						$time = $row["time"];
					}
					
					/*查询单车信息*/
					$sql = "SELECT * FROM bikeinfo WHERE authorId = '$authorId' AND bikeId < $startBikeId ORDER BY time DESC LIMIT $size";
					$result = mysql_query($sql);
					$tempList = array();
					while($row = mysql_fetch_array($result)) {
						$tempMsg = array();
						$tempMsg["bikeId"] = $row["bikeId"];
						$tempMsg["bikeName"] = $row["bikeName"];
						$tempMsg["authorId"] = $row["authorId"];
						$authorId = $row["authorId"];
						$tempMsg["content"] = $row["content"];
						$tempMsg["time"] = $row["time"];
						$tempMsg["authorName"] = $authorName;

 						array_push($tempList, $tempMsg);
							
					}
					$returnMsg["bikeList"] = $tempList;
					mysql_close($con);
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
	 * 根据单车名搜索单车
	 * @param $msg
	 * @return returnMsg
	 * {"returnCode":0} ($msg中bikeName、startBikeId、size缺少，或者抛出异常)
	 * {"returnCode":1,"bikeList":[{"bikeId":bikeId,"bikeName":bikeName,"authorId":authorId,"content":content,"time":time,"authorName":authorName},.....]} (成功获取，返回bikeList)
	 */
	public function getBikesByBikeName($msg){
		$returnMsg = array();
		try {
			if (isset($msg->{"bikeName"}) && isset($msg->{"startBikeId"}) && isset($msg->{"size"})) {
				$bikeName = $msg->{"bikeName"};
				$authorName;
				$startBikeId = $msg->{"startBikeId"};
				$size = $msg->{"size"};
		
				if (-1 == $startBikeId)  {
					$returnMsg["returnCode"] = 1;
					$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
					mysql_select_db("app_yibaosysu", $con);
					mysql_query("set names 'utf8'");
		
					/*查询单车信息*/
					$sql = "SELECT * FROM bikeinfo WHERE bikeName LIKE '%$bikeName%' ORDER BY time DESC, bikeId DESC LIMIT $size ";
					$result = mysql_query($sql);
					$tempList = array();
					while($row = mysql_fetch_array($result)) {
						$tempMsg = array();
						$tempMsg["bikeId"] = $row["bikeId"];
						$tempMsg["bikeName"] = $row["bikeName"];
						$tempMsg["authorId"] = $row["authorId"];
						$authorId = $row["authorId"];
						$tempMsg["content"] = $row["content"];
						$tempMsg["time"] = $row["time"];
							
						/*获取作者的名字*/
						$sql_2 = "SELECT * FROM userinfo WHERE userId = '$authorId'";
						$result_2 = mysql_query($sql_2);
						if ($row_2 = mysql_fetch_array($result_2)) {
							$tempMsg["authorName"] = $row_2["userName"];
						}
						array_push($tempList, $tempMsg);
							
					}
					$returnMsg["bikeList"] = $tempList;
					mysql_close($con);
					return $returnMsg;
				}
				else{
					$returnMsg["returnCode"] = 1;
					$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
					mysql_select_db("app_yibaosysu", $con);
					mysql_query("set names 'utf8'");
						
					
					/*获取startBikeId对应的时间戳*/
					$time;
					$sql = "SELECT * FROM bikeinfo WHERE bikeid = '$startBikeId'";
					$result = mysql_query($sql);
					while ($row = mysql_fetch_array($result))  {
						$time = $row["time"];
					}
					
					/*查询单车信息*/
					$sql = "SELECT * FROM bikeinfo WHERE bikeName LIKE '%$bikeName%' AND bikeId < $startBikeId ORDER BY time DESC LIMIT $size";
					$result = mysql_query($sql);
					$tempList = array();
					while($row = mysql_fetch_array($result)) {
						$tempMsg = array();
						$tempMsg["bikeId"] = $row["bikeId"];
						$tempMsg["bikeName"] = $row["bikeName"];
						$tempMsg["authorId"] = $row["authorId"];
						$authorId = $row["authorId"];
						$tempMsg["content"] = $row["content"];
						$tempMsg["time"] = $row["time"];
							
						/*获取作者的名字*/
						$sql_2 = "SELECT * FROM userinfo WHERE userId = '$authorId'";
						$result_2 = mysql_query($sql_2);
						if ($row_2 = mysql_fetch_array($result_2)) {
							$tempMsg["authorName"] = $row_2["userName"];
						}
						array_push($tempList, $tempMsg);
							
					}
					$returnMsg["bikeList"] = $tempList;
					mysql_close($con);
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
	 * 根据标签搜索单车
	 * @param $msg
	 * @return $returnMsg 
	 * {"returnCode":0} ($msg中label、startBikeId、size缺少，或者抛出异常)
	 * {"returnCode":1,"bikeList":[{"bikeId":"", "bikeName":"","authorId":"","content":"","time":"","authorId":""},......]}
	 */
	public function getBikesByLabel($msg){
		$returnMsg = array();
		try {
			if (isset($msg->{"label"}) && isset($msg->{"startBikeId"}) && isset($msg->{"size"})) {
				$label = $msg->{"label"};
				$startBikeId = $msg->{"startBikeId"};
				$size = $msg->{"size"};
		
				if (-1 == $startBikeId)  {
					$returnMsg["returnCode"] = 1;
					$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
					mysql_select_db("app_yibaosysu", $con);
					mysql_query("set names 'utf8'");
		
					/*根据label获取单车信息*/
					$sql = "SELECT DISTINCT bikeId FROM label WHERE content LIKE '%$label%' ORDER BY bikeId DESC LIMIT $size";
					$result = mysql_query($sql);
					$tempList = array();
					while($row = mysql_fetch_array($result)) {
						$bikeId = $row["bikeId"];
						
						/*根据单车id获取单车信息*/
						$sql_2 = "SELECT * FROM bikeinfo WHERE bikeId = '$bikeId'";
						$result_2 = mysql_query($sql_2);
						while ($row_2 = mysql_fetch_array($result_2)) {
							$tempMsg = array();
							$tempMsg["bikeId"] = $row_2["bikeId"];
							$tempMsg["bikeName"] = $row_2["bikeName"];
							$tempMsg["authorId"] = $row_2["authorId"];
							$authorId = $row_2["authorId"];
							$tempMsg["content"] = $row_2["content"];
							$tempMsg["time"] = $row_2["time"];
							
							/*获取作者的名字*/
							$sql_3 = "SELECT * FROM userinfo WHERE userId = '$authorId'";
							$result_3 = mysql_query($sql_3);
							if ($row_3 = mysql_fetch_array($result_3)) {
									$tempMsg["authorName"] = $row_3["userName"];
							}
							array_push($tempList, $tempMsg);
						}
					}
					$returnMsg["bikeList"] = $tempList;
					mysql_close($con);
					return $returnMsg;
				}
				else{
					$returnMsg["returnCode"] = 1;
					$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
					mysql_select_db("app_yibaosysu", $con);
					mysql_query("set names 'utf8'");
		
					
					/*根据label获取单车信息*/
					$sql = "SELECT * FROM label WHERE CONTAINS(content,'$label') AND bikeId < '$startBikeId' ORDER BY bikeId DESC LIMIT '$size'";
					$result = mysql_query($sql);
					$tempList = array();
					while($row = mysql_fetch_array($result)) {
						$bikeId = $row["bikeId"];
						
						/*根据单车id获取单车信息*/
						$sql_2 = "SELECT * FROM bikeinfo WHERE bikeId = '$bikeId";
						$result_2 = mysql_query($sql_2);
						while ($row_2 = mysql_fetch_array($result_2)) {
							$tempMsg = array();
							$tempMsg["bikeId"] = $row_2["bikeId"];
							$tempMsg["bikeName"] = $row_2["bikeName"];
							$tempMsg["authorId"] = $row_2["authorId"];
							$authorId = $row_2["authorId"];
							$tempMsg["content"] = $row_2["content"];
							$tempMsg["time"] = $row_2["time"];
							
							/*获取作者的名字*/
							$sql_3 = "SELECT * FROM userinfo WHERE userId = '$authorId'";
							$result_3 = mysql_query($sql_3);
							if ($row_3 = mysql_fetch_array($result_3)) {
									$tempMsg["authorName"] = $row_3["userName"];
							}
							array_push($tempList, $tempMsg);
						}
					}
					$returnMsg["bikeList"] = $tempList;
					mysql_close($con);
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
	 * 删除单车
	 * @param $msg
	 * @return $returnMsg
	 * {"returnCode":1} (删除成功)
	 * {"returnCode":0} ($msg中bikeId缺少，或者抛出异常)
	 */
	public function deleteBike($msg){
		$returnMsg = array();
		try {
			if (isset($msg->{"bikeId"}))  {
				$bikeId = $msg->{"bikeId"};
		
				$con = mysql_connect(DatabaseConstant::$MYSQL_HOST, DatabaseConstant::$MYSQL_USERNAME, DatabaseConstant::$MYSQL_PASSWORD);
				mysql_select_db("app_yibaosysu", $con);
				mysql_query("set names 'utf8'");
				
				
				/*删除该单车的有关评论*/
				mysql_query("DELETE FROM comment WHERE bikeId = '$bikeId'");
				/*删除该单车的标签*/
				mysql_query("DELETE FROM label WHERE bikeId = '$bikeId'");
				/*删除单车信息*/
				mysql_query("DELETE FROM bikeinfo WHERE bikeId = '$bikeId'");
				
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
}
?>