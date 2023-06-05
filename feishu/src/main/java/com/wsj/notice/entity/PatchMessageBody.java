package com.wsj.notice.entity;

import java.util.List;

public class PatchMessageBody {
    /**
     * open_ids : ["ou_18eac85d35a26f989317ad4f02e8bbbb","ou_461cf042d9eedaa60d445f26dc747d5e"]
     * union_ids : ["on_cad4860e7af114fb4ff6c5d496d1dd76","on_gdcq860e7af114fb4ff6c5d496dabcet"]
     * user_ids : ["7cdcc7c2","ca51d83b"]
     * msg_type : text
     * department_ids : ["3dceba33a33226","d502aaa9514059"]
     * content : {"text":"test content"}
     */
    private List<String> open_ids;
    private List<String> union_ids;
    private List<String> user_ids;
    private String msg_type;
    private List<String> department_ids;
    private ContentEntity content;

    public void setOpen_ids(List<String> open_ids) {
        this.open_ids = open_ids;
    }

    public void setUnion_ids(List<String> union_ids) {
        this.union_ids = union_ids;
    }

    public void setUser_ids(List<String> user_ids) {
        this.user_ids = user_ids;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public void setDepartment_ids(List<String> department_ids) {
        this.department_ids = department_ids;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    public List<String> getOpen_ids() {
        return open_ids;
    }

    public List<String> getUnion_ids() {
        return union_ids;
    }

    public List<String> getUser_ids() {
        return user_ids;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public List<String> getDepartment_ids() {
        return department_ids;
    }

    public ContentEntity getContent() {
        return content;
    }

    public static class ContentEntity {
        /**
         * text : test content
         */
        private String text;

        public ContentEntity setText(String text) {
            this.text = text;
            return this;
        }

        public String getText() {
            return text;
        }
    }
}
