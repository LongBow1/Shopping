# Shopping
shopping on web test

huaweiyun:124.70.220.72
ubuntu:
jdk install: 
sudo apt install openjdk-8-jre-headless
sudo apt install openjdk-8-jdk-headless
tomcat8:
sudo apt-get install tomcat8 tomcat8-docs tomcat8-examples tomcat8-admin

cd  /var/lib/tomcat8/webapps/ROOT#
sudo vi /var/lib/tomcat8/conf/tomcat-users.xml

#启动
service tomcat8 start
#状态
service tomcat8 status
#停止
service tomcat8 stop

sudo vi /var/lib/tomcat8/conf/tomcat-users.xml

<role rolename="manager-gui"/>
<role rolename="admin-gui"/>
<user username="root" password="123456" roles="manager-gui,admin-gui"/>

sudo service tomcat8 restart
