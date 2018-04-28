package com.yxdriver.app.model.bean;

/**
 * Created by mac on 2017/9/11.
 * 步骤
 */

public class StepBean {
    public static final int STEP_UNDO = -1;//未完成
    public static final int STEP_CURRENT = 0;//正在进行
    public static final int STEP_COMPLETED = 1;//已完成
    public static final int STEP_LAST_COMPLETED = 2;//终点完成
    public static final int STEP_LAST_UNCOMPLETED = 3;//终点未完成
    private String name;
    private int state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public StepBean() {
    }

    public StepBean(String name, int state) {
        this.name = name;
        this.state = state;
    }
}
