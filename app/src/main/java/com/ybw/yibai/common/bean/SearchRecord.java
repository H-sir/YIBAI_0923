package com.ybw.yibai.common.bean;

import android.support.annotation.NonNull;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 搜索记录
 *
 * @author sjl
 */
@Table(name = "SearchRecord")
public class SearchRecord implements Comparable<SearchRecord> {

    /**
     * 时间
     */
    @Column(name = "time")
    private long time;

    /**
     * 搜索内容
     */
    @Column(name = "content", isId = true, autoGen = false)
    private String content;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(@NonNull SearchRecord searchRecord) {
        if (this.time < searchRecord.time) {
            return 1;
        } else {
            return -1;
        }
    }
}
