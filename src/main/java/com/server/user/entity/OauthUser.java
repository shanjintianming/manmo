package com.server.user.entity;

public class OauthUser {

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oauth_user.id
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oauth_user.name
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oauth_user.password
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    private String password;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oauth_user.id
     *
     * @return the value of oauth_user.id
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oauth_user.id
     *
     * @param id the value for oauth_user.id
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oauth_user.name
     *
     * @return the value of oauth_user.name
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oauth_user.name
     *
     * @param name the value for oauth_user.name
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oauth_user.password
     *
     * @return the value of oauth_user.password
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oauth_user.password
     *
     * @param password the value for oauth_user.password
     *
     * @mbg.generated Mon May 14 13:39:00 CST 2018
     */
    public void setPassword(String password) {
        this.password = password;
    }
}