package panda.li.leavemanage.entity;

import java.io.Serializable;
import java.util.List;

public class UserWrapped implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6585084910502490100L;

    private String id;
    /**
     * 用户登陆号\工号\学号(具有实际意义的用户标识)
     */
    private String code;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户性别
     */
    private Integer userGender;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 用户所属基本部门
     */
    private String dept;
    /**
     * 用户所属基本部门id
     */
    private String deptId;
    /**
     * 用户所属班级名称
     */
    private String className;
    /**
     * 用户角色
     */
    private List<String> roleList;
    /**
     * 用户身份
     */
    private int userType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getUserGender() {
        return userGender;
    }

    public void setUserGender(Integer userGender) {
        this.userGender = userGender;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

}
