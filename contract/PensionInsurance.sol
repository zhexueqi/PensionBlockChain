pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;


contract PensionInsurance {
    
    // 定义用于存储员工身份信息的结构体
    struct Person {
        string name; // 姓名
        uint age; // 年龄
        string idNumber; // 身份证号码
        string employer; // 雇主
        uint startDate; // 缴费开始日期
        uint salary; // 工资        return people[employeeAddress];
        uint paymentBase; // 缴费基数
        address employeeAddress; //员工地址
    }
    
    
    // 养老保险账户信息
    struct InsuranceAccount {
        uint256 personalBalance; // 个人账户余额
        uint256 overallBalance; // 总账户余额
        bool isSponsor; // 雇主是否为职工的管理员
        uint paymentTimestamp;//缴费时间
    }
    
    // 公司信息
    struct Sponsor{
        string sponsorName;
        address sponsorAddress;
        
    }
    
    // 定义合约的变量
    uint256 constant MAX_INSURANCE_BASE = 24000; // 最大保险基数
    uint256 constant MIN_INSURANCE_BASE = 3200; // 最小保险基数
    uint256 constant PERSONAL_RATE = 8; // 个人缴费率
    uint256 constant OVERALL_RATE = 19; // 总缴费率
    uint256 constant SPONSOR_RATE = OVERALL_RATE - PERSONAL_RATE; // 雇主缴费率    
    uint256 constant PAYMENT_DEADLINE = 30 days; // 缴费截止日期
    address public admin = msg.sender; // 管理员的地址
    // 以工资金额为：25000 元为例
    // uint256 constant PERSONAL_AMOUNT = 2000; // 每月个人缴费金额  value =  salary*8/100
    // uint256 constant OVERALL_AMOUNT = 4750; // 每月总缴费金额     value =  salary*19/100
    // uint256 constant SPONSOR_AMOUNT = OVERALL_AMOUNT - PERSONAL_AMOUNT; // 每月雇主缴费金额
    
    
    
    // 定义员工信息映射
    mapping(address => Person) people;
    
    // 定义养老保险账户的映射
    mapping(address => InsuranceAccount) public insuranceAccounts;
    
    // 公司信息映射
    mapping(address => Sponsor) public sponsor;
    
    // 员工地址数组
    address[] public persons;
    
    // 雇主地址数组
    address[] public sponsors;
    
    
    /*********** 添加员工身份信息接口开发 **********/
    
    function addPerson(string name, uint age, string memory idNumber, string employer, uint startDate, uint salary, address employeeAddress ) public {
        // 身份证号码不能为空且账户不能已存在
        require(bytes(idNumber).length > 0 && bytes(people[employeeAddress].idNumber).length == 0);
        
        uint paymentBase = salary > MAX_INSURANCE_BASE ? MAX_INSURANCE_BASE : (salary < MIN_INSURANCE_BASE ? MIN_INSURANCE_BASE : salary);
        
        // 创建新账户并存储信息
        people[employeeAddress] = Person(name, age, idNumber, employer, startDate, salary, paymentBase, employeeAddress);
        
        persons.push(employeeAddress);
        
       
      
    }
    
    // 获取雇员信息
    function getPerson(address employeeAddress)public view returns(string,uint,string,string,uint,uint,uint,address){
        Person person = people[employeeAddress];
        return (person.name, person.age, person.idNumber, person.employer, person.startDate, person.salary, person.paymentBase, person.employeeAddress);
    }
    
    // 获取雇员地址列表
    function getPersonList()public view returns(address[]){
        return persons;
    }
    
    // 获取公司地址列表
    function getSponsorsList()public view returns(address[]){
        return sponsors;
    }
    
    // 获取雇主信息
    function getSponsor(address sponsorAddress)public view returns(string,address){
        Sponsor currentSponsor = sponsor[sponsorAddress];
        return (currentSponsor.sponsorName, currentSponsor.sponsorAddress);
    }
    
    // 获取用户保险账户信息
    function getInsuranceAccount(address employeeAddress)public view returns(uint256,uint256,bool,uint){
        InsuranceAccount insuranceAccount = insuranceAccounts[employeeAddress];
        return (insuranceAccount.personalBalance, insuranceAccount.overallBalance, insuranceAccount.isSponsor, insuranceAccount.paymentTimestamp);
    }
    
    
    
    /*********** 添加管理员账户总账户存入余额接口 **********/
    
    function depositSposor(address _sponsor, uint256 amount ) public {
        // 只有管理员可以存入总账户余额
        require(msg.sender == admin, "Only admin can deposit overallBalance");
        // 从管理员账户转出总缴费金额
        insuranceAccounts[_sponsor].overallBalance += amount;
    }
    
    
    /*********** 添加新职工账户接口 **********/
    
    function addEmployee(address employee, uint256 salary, address _sponsor) public {
        // 只有管理员可以添加新职工账户
        require(msg.sender == admin, "Only admin can add employees");        
        // require(msg.sender == _sponsor, "Only sponsor can add employees");

        // 职工地址不能为空且职工账户不能已存在
        require(employee != address(0) && insuranceAccounts[employee].personalBalance == 0, "Invalid employee address");
        
        // 创建新职工账户
        insuranceAccounts[employee] = InsuranceAccount({
            personalBalance: 0,
            overallBalance: 0,
            isSponsor: false,
            paymentTimestamp: block.timestamp
        });
        // 计算保险基数和缴费金额
        uint256 insuranceBase = salary > MAX_INSURANCE_BASE ? MAX_INSURANCE_BASE : (salary < MIN_INSURANCE_BASE ? MIN_INSURANCE_BASE : salary);
        uint256 personalAmount = insuranceBase * PERSONAL_RATE / 100;
        uint256 overallAmount = insuranceBase * OVERALL_RATE / 100;
        // 从管理员账户转出总缴费金额
        insuranceAccounts[_sponsor].overallBalance -= overallAmount;
        // 职工个人账户增加个人缴费金额
        insuranceAccounts[employee].personalBalance += personalAmount;
        // 管理员个人账户增加个人缴费金额
        insuranceAccounts[_sponsor].personalBalance = overallAmount - personalAmount;
        
        
    }
    
    
    
    /*********** 添加新雇主账户接口 **********/
    function addSponsorAccount(address _sponsor, string sponsorName) public {
        // 只有管理员可以添加新雇主账户 
        require(admin == msg.sender, "Only admin can add new sponsors");
        // 雇主地址不能为空且雇主账户不能已存在
        require(_sponsor != address(0) && insuranceAccounts[_sponsor].paymentTimestamp == 0, "Invalid sponsor address"); 
        
        // 创建新雇主账户
        insuranceAccounts[_sponsor] = InsuranceAccount({
            personalBalance: 0,
            overallBalance: 0,
            isSponsor: true,
            paymentTimestamp: block.timestamp
        });
        sponsor[_sponsor] = Sponsor(sponsorName,_sponsor);
        
        
        sponsors.push(_sponsor);
        
    }

    
    // 定义设置管理员的函数
    function setSponsor(address newSponsor) public {
        // 只有当前管理员可以设置新的管理员
        require(msg.sender == admin, "只有当前管理员可以设置新的管理员");
        // 新的管理员不能与当前管理员相同
        require(newSponsor != admin, "新的管理员不能与当前管理员相同");
        // 新的管理员必须有雇主账户
        require(insuranceAccounts[newSponsor].overallBalance > 0, "新的管理员必须有雇主账户");
        // 设置新的管理员并更新账户的管理员状态
        address oldSponsor = admin;
        admin = newSponsor;
        insuranceAccounts[oldSponsor].isSponsor = false;
        insuranceAccounts[newSponsor].isSponsor = true;
        
    }
    
    // 定义进行缴费的函数
    function makePayment(address employee, uint256 salary, address _sponsor) public {
        // 职工账户必须存在
        require(insuranceAccounts[employee].personalBalance > 0 || insuranceAccounts[employee].overallBalance > 0, "职工账户必须存在");
        // 雇主账户必须存在且已被赞助
        require(insuranceAccounts[_sponsor].overallBalance > 0 && insuranceAccounts[_sponsor].isSponsor, "雇主账户必须存在且已被赞助");
        // 雇主账户必须有足够的余额进行总缴费
        require(insuranceAccounts[_sponsor].overallBalance >= salary*19/100, "雇主账户必须有足够的余额进行总缴费");
        // 缴费必须在截止日期之前进行
        // require(block.timestamp <= insuranceAccounts[_sponsor].paymentTimestamp + PAYMENT_DEADLINE, "缴费必须在截止日期之前进行");
        
        // 根据职工的工资计算个人和总缴费金额
        // 职工的月薪   salary
        uint256 insuranceBase = salary > MAX_INSURANCE_BASE ? MAX_INSURANCE_BASE : (salary < MIN_INSURANCE_BASE ? MIN_INSURANCE_BASE : salary);
        uint256 personalAmount = insuranceBase * PERSONAL_RATE / 100;
        uint256 overallAmount = insuranceBase * OVERALL_RATE / 100;
        // 更新职工和雇主账户的余额
        insuranceAccounts[employee].personalBalance += personalAmount;
        insuranceAccounts[_sponsor].personalBalance += insuranceBase * (OVERALL_RATE-PERSONAL_RATE) / 100;
        insuranceAccounts[_sponsor].overallBalance -= overallAmount;
        insuranceAccounts[_sponsor].paymentTimestamp = block.timestamp;
    }
    
    // 定义删除职工账户的函数
    function removeEmployee(address employee) public {
        // 只有管理员可以删除职工账户
        require(msg.sender == admin, "1");
        // 职工账户必须存在且未被赞助
        require(insuranceAccounts[employee].personalBalance > 0 && !insuranceAccounts[employee].isSponsor, "2");
        // 职工账户必须余额为零
        require(insuranceAccounts[employee].personalBalance == 0 && insuranceAccounts[employee].overallBalance == 0, "3");
        // 删除职工账户
        delete insuranceAccounts[employee];
    }

} 



