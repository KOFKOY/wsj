package com.wsj.server.entity;

public class RebotEvent {
    /**
     * schema : 2.0
     * header : {"event_id":"e6e003c95188b654f99d694c0ab5ce90","event_type":"application.bot.menu_v6","tenant_key":"12f877ed3fcf175d","create_time":"1689056699000","app_id":"cli_a114bdbbf0fd100c","token":"7Mt8ZY94fGjBhTRm9xjdveNQbWk12GoF"}
     * event : {"event_key":"{\"test\":\"wsj\"}","operator":{"operator_id":{"open_id":"ou_6df1ecb8dd56e8dee488c02cb45b57fd","user_id":"58f56c23","union_id":"on_4a3a8a60db76941f58d91651693b6f87"}},"timestamp":1689056699}
     */
    private String schema;
    private HeaderEntity header;
    private EventEntity event;

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setHeader(HeaderEntity header) {
        this.header = header;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public String getSchema() {
        return schema;
    }

    public HeaderEntity getHeader() {
        return header;
    }

    public EventEntity getEvent() {
        return event;
    }

    public class HeaderEntity {
        /**
         * event_id : e6e003c95188b654f99d694c0ab5ce90
         * event_type : application.bot.menu_v6
         * tenant_key : 12f877ed3fcf175d
         * create_time : 1689056699000
         * app_id : cli_a114bdbbf0fd100c
         * token : 7Mt8ZY94fGjBhTRm9xjdveNQbWk12GoF
         */
        private String event_id;
        private String event_type;
        private String tenant_key;
        private String create_time;
        private String app_id;
        private String token;

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public void setEvent_type(String event_type) {
            this.event_type = event_type;
        }

        public void setTenant_key(String tenant_key) {
            this.tenant_key = tenant_key;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getEvent_id() {
            return event_id;
        }

        public String getEvent_type() {
            return event_type;
        }

        public String getTenant_key() {
            return tenant_key;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getApp_id() {
            return app_id;
        }

        public String getToken() {
            return token;
        }
    }

    public class EventEntity {
        /**
         * event_key : {"test":"wsj"}
         * operator : {"operator_id":{"open_id":"ou_6df1ecb8dd56e8dee488c02cb45b57fd","user_id":"58f56c23","union_id":"on_4a3a8a60db76941f58d91651693b6f87"}}
         * timestamp : 1689056699
         */
        private String event_key;
        private OperatorEntity operator;
        private int timestamp;

        public void setEvent_key(String event_key) {
            this.event_key = event_key;
        }

        public void setOperator(OperatorEntity operator) {
            this.operator = operator;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getEvent_key() {
            return event_key;
        }

        public OperatorEntity getOperator() {
            return operator;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public class OperatorEntity {
            /**
             * operator_id : {"open_id":"ou_6df1ecb8dd56e8dee488c02cb45b57fd","user_id":"58f56c23","union_id":"on_4a3a8a60db76941f58d91651693b6f87"}
             */
            private Operator_idEntity operator_id;

            public void setOperator_id(Operator_idEntity operator_id) {
                this.operator_id = operator_id;
            }

            public Operator_idEntity getOperator_id() {
                return operator_id;
            }

            public class Operator_idEntity {
                /**
                 * open_id : ou_6df1ecb8dd56e8dee488c02cb45b57fd
                 * user_id : 58f56c23
                 * union_id : on_4a3a8a60db76941f58d91651693b6f87
                 */
                private String open_id;
                private String user_id;
                private String union_id;

                public void setOpen_id(String open_id) {
                    this.open_id = open_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public void setUnion_id(String union_id) {
                    this.union_id = union_id;
                }

                public String getOpen_id() {
                    return open_id;
                }

                public String getUser_id() {
                    return user_id;
                }

                public String getUnion_id() {
                    return union_id;
                }
            }
        }
    }
}
