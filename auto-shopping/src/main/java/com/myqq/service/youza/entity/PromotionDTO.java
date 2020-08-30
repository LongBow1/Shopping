package com.myqq.service.youza.entity;

import java.util.List;

/**
 * 优惠相关
 */
public class PromotionDTO {

    /**
     * {
     *     "code": 0,
     *     "message": "成功",
     *     "status": 200,
     *     "data": [
     *         {
     *             "id": "8a37bdaf-e361-ba70-e425-8dcbb4133527",
     *             "activityName": null,
     *             "activityLabel": null,
     *             "startTime": 1598544000000,
     *             "endTime": 1599235200000,
     *             "allGoods": false,
     *             "goodsId": null,
     *             "reductionType": null,
     *             "activityType": null,
     *             "activityStatus": null,
     *             "payedOrder": null,
     *             "partakeMember": null,
     *             "singlePrice": null,
     *             "actualAmount": null,
     *             "content": "满49.80元,减2.9元",
     *             "detail": null
     *         }
     *     ]
     * }
     */
    private Integer code;
    private String message;
    private Integer status;
    private List<Activity> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Activity> getData() {
        return data;
    }

    public void setData(List<Activity> data) {
        this.data = data;
    }

    public static class Activity{
        private String id;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
