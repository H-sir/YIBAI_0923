package com.ybw.yibai.common.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean.ParamBean;
import com.ybw.yibai.common.bean.ProductScreeningParam.DataBean.ParamBean.SonBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ybw.yibai.base.YiBaiApplication.getProductScreeningParamList;

/**
 * 处理与用户"筛选"数据相关的工具类
 *
 * @author sjl
 */
public class FilterUtil {

    /**
     * 重置筛选参数
     *
     * @param paramList 筛选参数
     */
    public static void resetFilterParam(@NonNull List<ParamBean> paramList) {
        for (ParamBean paramBean : paramList) {
            // 注意这里重置的最小值必须是范围的最小值
            int min = paramBean.getMin();
            paramBean.setLeftValue(min);
            paramBean.setRightValue(min);
            List<ParamBean.SonBean> sonList = paramBean.getSon();
            if (null == sonList || sonList.size() == 0) {
                continue;
            }
            for (ParamBean.SonBean sonBean : sonList) {
                sonBean.setSelected(false);
            }
        }
    }

    /**
     * 获取用户上一次保存的筛选参数
     *
     * @param categoryCode 产品类别(产品筛选参数大类别名)默认获取植物
     */
    public static Map<String, String> getFilterParam(@NonNull String categoryCode) {
        List<ProductScreeningParam.DataBean> productFilterParamList = getProductScreeningParamList();
        if (null == productFilterParamList || productFilterParamList.size() == 0) {
            return null;
        }
        for (ProductScreeningParam.DataBean param : productFilterParamList) {
            String cateCode = param.getCate_code();
            if (!cateCode.equals(categoryCode)) {
                continue;
            }
            List<ParamBean> p = param.getParam();
            if (null == p) {
                continue;
            }
            return getFilterParam(p);
        }
        return null;
    }

    /**
     * 获取用户选中的筛选参数
     *
     * @param paramList 筛选参数
     * @return 用户选中的筛选参数
     */
    public static Map<String, String> getFilterParam(@NonNull List<ParamBean> paramList) {
        // 用户选中的产品筛选参数
        List<List<String>> mSelectedProductParamList = new ArrayList<>();

        /*
         * 用户选中的产品筛选参数
         * K:field
         * V:个属性ID拼接的String
         */
        Map<String, String> mSelectProductParamMap = new HashMap<>();

        for (ParamBean paramBean : paramList) {
            // 定义一个集合用于接收用户选中的某一个属性的筛选参数,例如{植物类别中的“观花”,“观叶”}
            List<String> stringList = new ArrayList<>();
            List<SonBean> sonList = paramBean.getSon();
            String field = paramBean.getField();
            float leftValue = paramBean.getLeftValue();
            float rightValue = paramBean.getRightValue();

            if (null != sonList && sonList.size() > 0) {
                for (SonBean sonBean : sonList) {
                    if (null == sonBean) {
                        continue;
                    }
                    // 判断用户是否选中了这个参数
                    if (!sonBean.isSelected()) {
                        continue;
                    }
                    String ids = sonBean.getId();
                    // 判断集合中是否包含"field(参数传参字段)"这个数据,如果没有就添加,如果有就不能添加,本且要保证
                    // field添加的顺序在ids前面,这样才能保证最后得到的数据格式为["pcId","7","8","9","10"]
                    if (!stringList.contains(field)) {
                        stringList.add(field);
                    }
                    stringList.add(ids);
                }
            }

            // 判断范围参数是否大于0
            if (leftValue > 0 || rightValue > 0) {
                mSelectProductParamMap.put(field, Math.round(leftValue) + "|" + Math.round(rightValue));
            }
            // 判断stringList是否为null,因为如果用户没有选中某一个属性的筛选参数,那么stringList集合的size == 0
            if (stringList.size() == 0) {
                continue;
            }
            mSelectedProductParamList.add(stringList);
        }
        // 最后获得这种格式的数据[["pcId","7","8","9","10"],["attrId","1","2","3","4"],["attrId","9"],["attrId","36","37","38","39","40","41"]]

        for (List<String> list : mSelectedProductParamList) {
            // list内容的格式为: ["pcId","7","8","9","10"]
            // 定义变量K用于接收field参数
            String k = null;
            // StringBuilder接收id
            StringBuilder stringBuilder = new StringBuilder();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String string = list.get(i);
                if (0 == i) {
                    // 有上面可知第一个参数为field
                    k = string;
                } else if (i == size - 1) {
                    stringBuilder.append(string);
                } else {
                    // 只要不是最后一个id参数那么就必须在后面添加英文","
                    stringBuilder.append(string);
                    stringBuilder.append(",");
                }
            }
            // 经过一次for循环获得例如: k = field / stringBuilder = 36,37,38,39,40,41 数据
            if (TextUtils.isEmpty(k)) {
               continue;
            }
            // 获取Value
            String value = mSelectProductParamMap.get(k);
            // 判断Value是否为空
            if (TextUtils.isEmpty(value)) {
                // 如果为空,就put
                mSelectProductParamMap.put(k, stringBuilder.toString());
            } else {
                // 如果为不为空,说明之前存在一个k与现在要put的key相同,那么就在是一个相同的key的v值中拼接当前的value本且以"|"分隔
                mSelectProductParamMap.put(k, value + "|" + stringBuilder.toString());
            }
        }
        // 最后获得这种格式的数据 {"attrId":"36,37,38,39,40,41|9|1,2,3,4","pcId":"7,8,9,10"}

        return mSelectProductParamMap;
    }
}