<?php

include_once 'Dispatcher.php';

header("content-type:text/html; charset=utf-8");
try {
	date_default_timezone_set("PRC");
	$dispatcher = new Dispatcher();
	
 	$msg = json_decode($_POST["msg"]);
 	$returnMsg = $dispatcher->dispatch($msg);
 	echo json_encode($returnMsg);
	
}
catch (Exception $e) {
	$returnMsg = array();
	$returnMsg["returnCode"] = 0;
	echo $returnMsg;
}

?>