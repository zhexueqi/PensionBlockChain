pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./PensionInsurance.sol";

//印章合约
contract PensionInsuranceTransfer is PensionInsurance {
    

    // 定义申请结构体
    struct Application {
        address applicant; // 申请人地址
        address currentBureauAddress;  //申请人当前社保局地址
        string fromCity;   // 原城市
        string toCity;     // 目标城市
        bool isStopped;    // 是否停缴  true === 停止缴费
        bool isApproved;   // 是否批准
    }
   
     // 定义账户结构体
    struct Account {
        uint personalFund;     // 个人账户基金
        uint overallFund;      // 统筹账户基金
        uint pensionInsurance; // 养老保险账户
        address currentBureauAddress;  //当前社保局地址
    }
    
    // 定义社保局结构体
    struct SocialSecurityBureau {
        string bureauCity;      // 社保局城市
        address bureauAddress;  // 社保局地址
        bool isAuthorized;      // 是否允许接收社保转移授权
        bool hasReceived;       // 是否已接收
    }
    
    // 定义申请人地址列表
    address[] public applicationAddress;
    
    // 定义社保局地址列表
    address[] public socialSecurityBureausAddressList;
    
    // 定义账户映射
    mapping(address => Account) public accounts;
    
    // 定义社保局映射
    mapping(address => SocialSecurityBureau) public socialSecurityBureaus;
    
    // 定义申请映射
    mapping(address => Application) public currentApplication;
    
    //获取员工个人账户余额
    function getAccount(address account) public view returns(uint256) { //需要修改内部可见
        return PensionInsurance.insuranceAccounts[account].personalBalance;
    }
    
    // 获取所有申请的申请人地址列表
    function getAllApplications() public view returns(address[]){
        return applicationAddress;
    }
    
    // 获取所有社保局地址列表
     function getAllSocialSecurityBureaus() public view returns(address[]){
        return socialSecurityBureausAddressList;
    }
    
    // 获取申请
    function getApplication(address _applicantAddress) public view returns(address, address, string, string, bool, bool){
        Application applicantion = currentApplication[_applicantAddress];
        return (applicantion.applicant, applicantion.currentBureauAddress, applicantion.fromCity, applicantion.toCity, applicantion.isStopped, applicantion.isApproved);
    }
    
    // 获取社保局
     function getCurrentBureau(address _bureauAddress) public view returns(string, address, bool, bool){
         SocialSecurityBureau currentBureau = socialSecurityBureaus[_bureauAddress];
        return (currentBureau.bureauCity, currentBureau.bureauAddress, currentBureau.isAuthorized, currentBureau.hasReceived);
    }
    
    
    
     /*********** 申请转移关系接口 **********/
    //  需要接收城市社保局被授权
    function applyForTransfer(string memory _fromCity, string memory _toCity, address applicant,address fromBureauAddress,  address toBureauAddress) public {
        accounts[applicant].personalFund = getAccount(applicant);
        // 申请人地址不能为空且必须有账户
        require(applicant != address(0) && accounts[applicant].personalFund > 0, "Invalid applicant");
        // // 目标城市社保局必须被授权
        SocialSecurityBureau storage targetBureau= socialSecurityBureaus[toBureauAddress];
        require(targetBureau.isAuthorized, "Target bureau not authorized");
        
        // 创建申请
        Application memory newApplication = Application(applicant, fromBureauAddress, _fromCity, _toCity, false, false);
        
        currentApplication[applicant] = newApplication;

        // 创建新的申请
        applicationAddress.push(newApplication.applicant);
    }
    
    
    
    /************************ fromCity 社保局查询调用申请************************/
    function authorizeTransfer(address _applicantAddress, address fromBureauAddress) public {
        
        // 判断社保局权限，只有当前社保局可以调用申请
        require(currentApplication[_applicantAddress].currentBureauAddress == fromBureauAddress,"only fromCity's SocialSecurityBureau can approved applicant!");
        
        
        // 转移账户
        Account storage currentAccount = accounts[_applicantAddress];
        
        SocialSecurityBureau storage currentBureau = socialSecurityBureaus[fromBureauAddress];
        
        // 申请人账户清零
        currentAccount.personalFund = 0;
        currentAccount.overallFund = 0;
        currentAccount.pensionInsurance = 0;
        currentAccount.currentBureauAddress = fromBureauAddress;
        // 当前社保局接收通过申请
        currentBureau.hasReceived = true;
        // 更新申请状态
        currentApplication[_applicantAddress].isStopped = true;
        // currentApplication[_applicantAddress].isApproved = true;
    }
    
    
        /*********** toCity 社保局接收账户转移接口 **********/
    function receiveTransfer(address _applicantAddress, address toBureauAddress) public {
        // toCity 社保局必须被授权
        require(socialSecurityBureaus[toBureauAddress].isAuthorized, "Unauthorized bureau");
        // toCity 社保局尚未接收账户
        require(!socialSecurityBureaus[toBureauAddress].hasReceived, "Account already received");
        
        // 申请必须已停缴
        require(currentApplication[_applicantAddress].isStopped, "Application not approved");
        
       
        // 获取账户
        Account storage currentAccount = accounts[_applicantAddress];
        SocialSecurityBureau storage currentBureau = socialSecurityBureaus[toBureauAddress];
        
        // 转移账户
        // currentBureau.isAuthorized = true;
        currentBureau.hasReceived = true;
        // 已经接受申请
        currentApplication[_applicantAddress].isApproved = true;

        
        // 转入城市个人账户设置
        currentAccount.personalFund = 0;
        currentAccount.overallFund = 0;
        currentAccount.pensionInsurance = 0;
        currentAccount.currentBureauAddress = toBureauAddress;

    }
    
    /*****************改变社保局接受状态*********************/ 
    function changeBureausAuthorized(address _bureauAddress) public returns(bool){
        if(socialSecurityBureaus[_bureauAddress].isAuthorized){
            socialSecurityBureaus[_bureauAddress].isAuthorized = false;
            return false;
        }else{
            socialSecurityBureaus[_bureauAddress].isAuthorized = true;
            return true;
        }
    }
    
    
    /*********** 新增社保局接口 **********/
    
    function addBureaus(string memory bureauCity, address bureauAddress) public {
        // 添加社保局需要管理权限
        require(PensionInsurance.admin == msg.sender,"only admin can add Bureaus!");
        // 社保局地址不能为空且不能已存在
        require(bureauAddress != address(0) && socialSecurityBureaus[bureauAddress].bureauAddress != bureauAddress,"1");
        
        SocialSecurityBureau memory newSocialSecurityBureau = SocialSecurityBureau(bureauCity,bureauAddress,true,false);
        
        socialSecurityBureaus[bureauAddress] = newSocialSecurityBureau;
        
        socialSecurityBureausAddressList.push(bureauAddress);
    }
    
    
    /*********** 查询社保局接口 **********/
    function searchBureaus(string _bureauCity) public view returns(string, address, bool, bool){    
  

        // 查询社保局需要管理权限
        require(PensionInsurance.admin == msg.sender,"only admin can search Bureaus!");
        SocialSecurityBureau memory currentBureaus;
        for (uint i = 0; i < socialSecurityBureausAddressList.length; i++) {
            if (keccak256(socialSecurityBureaus[socialSecurityBureausAddressList[i]].bureauCity) == keccak256(_bureauCity)) {
                currentBureaus = socialSecurityBureaus[socialSecurityBureausAddressList[i]];
                break;
            }
            
        }
       
        return (currentBureaus.bureauCity, currentBureaus.bureauAddress, currentBureaus.isAuthorized, currentBureaus.hasReceived);
    }

}


