package com.ybw.yibai.common.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * 城市区域列表
 *
 * @author sjl
 */
public class CityAreaList {

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements IPickerViewData {

        /**
         *
         */
        private int id;

        /**
         *
         */
        private int pid;

        /**
         *
         */
        private int depth;

        /**
         * 省名称
         */
        private String name;

        /**
         * 区域编码
         */
        private String code;

        /**
         *
         */
        private List<ChildrenBeanX> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<ChildrenBeanX> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBeanX> children) {
            this.children = children;
        }

        /**
         * 实现 IPickerViewData 接口
         * 这个用来显示在PickerView上面的字符串
         * PickerView会通过IPickerViewData获取getPickerViewText方法显示出来
         */
        @Override
        public String getPickerViewText() {
            return this.name;
        }

        public static class ChildrenBeanX implements IPickerViewData {

            /**
             *
             */
            private int id;

            /**
             *
             */
            private int pid;

            /**
             *
             */
            private int depth;

            /**
             * 市名称
             */
            private String name;

            /**
             * 区域编码
             */
            private String code;

            /**
             *
             */
            private List<ChildrenBean> children;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getDepth() {
                return depth;
            }

            public void setDepth(int depth) {
                this.depth = depth;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            /**
             * 实现 IPickerViewData 接口
             * 这个用来显示在PickerView上面的字符串
             * PickerView会通过IPickerViewData获取getPickerViewText方法显示出来
             */
            @Override
            public String getPickerViewText() {
                return this.name;
            }

            public static class ChildrenBean implements IPickerViewData {

                /**
                 *
                 */
                private int id;

                /**
                 *
                 */
                private int pid;

                /**
                 *
                 */
                private int depth;

                /**
                 * 区名称
                 */
                private String name;

                /**
                 * 区域编码
                 */
                private String code;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }

                public int getDepth() {
                    return depth;
                }

                public void setDepth(int depth) {
                    this.depth = depth;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                /**
                 * 实现 IPickerViewData 接口
                 * 这个用来显示在PickerView上面的字符串
                 * PickerView会通过IPickerViewData获取getPickerViewText方法显示出来
                 */
                @Override
                public String getPickerViewText() {
                    return this.name;
                }
            }
        }
    }
}
