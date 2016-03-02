package com.example.sony.jizha.model;

import com.example.sony.jizha.Widget.BaseModel;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：用户详细信息实体
 * 创建人：sony
 * 创建时间：2015/12/31 16:29
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class Profile{

    //会员id
    private Long memberId;
    //姓名
    private String name;
    //邮箱/账号
    private String email;
    //省份
    private String province;
    //城市
    private String city;
    //公司
    private String company;
    //年龄
    private int age;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
