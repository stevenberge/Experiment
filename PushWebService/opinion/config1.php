<?php
	ob_start();
//session_start();
//error_reporting(0); //�ݴ����
$link_ID=mysql_connect("localhost","root","1234");//����Mysql������
mysql_select_db("opinion"); //ѡ�����ݿ�
mysql_query("set names 'utf8'");//ѡ������ʽ
date_default_timezone_set("Asia/Hong_Kong"); //���÷���ʱ���ʽ
?>