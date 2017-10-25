package com.pigbear.hi_andgmusic.event;

/**
 * Created by 杨阳洋 on 2017/10/24.
 * 构造消息
 */

public class CollectionUpdateEvent {

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    private boolean update;

    public CollectionUpdateEvent(boolean update) {
        this.update = update;
    }


}
