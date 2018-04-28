package com.yxclient.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 2017/10/20.
 * ////////////////////////////////////////////////////////////////////
 * //                            _ooOoo_                             //
 * //                           o8888888o                            //
 * //                           88" . "88                            //
 * //                           (| ^_^ |)                            //
 * //                           O\  =  /O                            //
 * //                        ____/`---'\____                         //
 * //                      .'  \\|     |//  `.                       //
 * //                     /  \\|||  :  |||//  \                      //
 * //                    /  _||||| -:- |||||-  \                     //
 * //                    |   | \\\  -  /// |   |                     //
 * //                    | \_|  ''\---/''  |   |                     //
 * //                    \  .-\__  `-`  ___/-. /                     //
 * //                  ___`. .'  /--.--\  `. . ___                   //
 * //                ."" '<  `.___\_<|>_/___.'  >'"".                //
 * //              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
 * //              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
 * //        ========`-.____`-.___\_____/___.-`____.-'========       //
 * //                             `=---='                            //
 * //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
 * //         佛祖保佑       永无BUG        永不修改                    //
 * ////////////////////////////////////////////////////////////////////
 */
// 同城信息
public class CityWideModel implements Serializable {
    private static final long serialVersionUID = 8880943322827638763L;
    private String uuid;// uuid
    private String title;// 标题
    private String content;// 信息内容
    private String type;// 信息类型
    private String[] images;//  信息图片集合
    private String createTime;// 创建日期
    private UserModel createUser;// 创建者
    private List<InfoEvaluationsModel> evaluations;// 评论列表

    public CityWideModel() {
    }

    public CityWideModel(String title, String content, String type, String[] images
            , String createTime, UserModel createUser) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.images = images;
        this.createTime = createTime;
        this.createUser = createUser;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserModel getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserModel createUser) {
        this.createUser = createUser;
    }


    public List<InfoEvaluationsModel> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<InfoEvaluationsModel> evaluations) {
        this.evaluations = evaluations;
    }
}
