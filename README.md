# PensionBlockChain
这是一个基于FISCO-BCOS联盟链去实现的养老保险养老金自动化管理，发放的平台

# 搭建一个四节点的联盟链
https://fisco-bcos-documentation.readthedocs.io/zh-cn/latest/docs/installation.html

# 搭建WeBASE-Front节点前置平台
https://webasedoc.readthedocs.io/zh-cn/latest/docs/WeBASE-Install/developer.html

# 上传合约
将contract文件夹中的合约文件sol，上传到WeBASE-Front平台
# 编译部署合约
编译部署PensionInsuranceTransfer合约
# 导出Java项目
只勾选PensionInsuranceTransfer合约，用户选择为部署的用户，IP改为虚拟机的IP。channelPort为20200。
# 打开项目
复制导出的项目的resource目录下的abi和conf到我们项目的resource目录下即可。
# 修改配置文件
修稿application.properties中的PensionInsuranceTransferAddress，改为我们部署后的合约地址。
修改owner为我们部署的用户 
修改WeBASE-Front URL为 http://{虚拟机IP}:5002/WeBASE-Front/trans/handle
修改Mysql用户名和密码

# 导入SQL
导入sql文件夹下的sql脚本创建数据库和表。

启动即可
