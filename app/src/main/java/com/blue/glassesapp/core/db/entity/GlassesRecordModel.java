package com.blue.glassesapp.core.db.entity;

import com.blue.glassesapp.common.enums.InteractionDirection;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <pre>
 *     眼镜交互记录
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/7</p>
 *
 * @author zxh
 */
@Entity
public class GlassesRecordModel {
    @Id(autoincrement = true)
    private Long id;

    /**
     * 数据交互方向。
     * DataDirection
     *
     * @see InteractionDirection
     */
    private String direction;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 显示内容
     */
    private String content;
    /**
     * 交互内容图片
     */
    private String contentImg;

    /**
     * 响应或错误说明
     */
    private String message;
    /**
     * 时间戳
     */
    private long timestamp;
    @Generated(hash = 1863644087)
    public GlassesRecordModel(Long id, String direction, String businessType,
            String content, String contentImg, String message, long timestamp) {
        this.id = id;
        this.direction = direction;
        this.businessType = businessType;
        this.content = content;
        this.contentImg = contentImg;
        this.message = message;
        this.timestamp = timestamp;
    }
    @Generated(hash = 453176799)
    public GlassesRecordModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDirection() {
        return this.direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getBusinessType() {
        return this.businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContentImg() {
        return this.contentImg;
    }
    public void setContentImg(String contentImg) {
        this.contentImg = contentImg;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
