package com.ybw.yibai.common.widget.selectsspecview;

/**
 * 选择规格视图侦听器
 *
 * @author sjl
 */
public interface OnSpecSelectedListener {

    /**
     * 在一个新的规格被选中的时候回调
     *
     * @param title              规格名称
     * @param titlePosition      规格名称在列表中的位置
     * @param smallTitle         具体规格值
     * @param smallTitlePosition 具体规格值在列表中的位置
     */
    void onSpecSelected(String title, int titlePosition, String smallTitle, int smallTitlePosition);
}
