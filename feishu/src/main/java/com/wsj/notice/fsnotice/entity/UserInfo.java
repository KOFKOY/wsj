package com.wsj.notice.fsnotice.entity;

public class UserInfo {

    /**
     * open_id : ou_2c8e9e6582f07338f2d7843793361415
     * user_id : 9fc41e12
     * mobile : +8617073721806
     * name : 许七安
     * union_id : on_c901628a93f2b9973ce3efd0dc6300ed
     * description :
     * en_name :
     * avatar : {"avatar_640":"https://s1-imfile.feishucdn.com/static-resource/v1/v2_44bbb547-b064-4a3f-999a-e5bb94e832cg~?image_size=640x640&cut_type=&quality=&format=png&sticker_format=.webp","avatar_origin":"https://s1-imfile.feishucdn.com/static-resource/v1/v2_44bbb547-b064-4a3f-999a-e5bb94e832cg~?image_size=noop&cut_type=&quality=&format=png&sticker_format=.webp","avatar_72":"https://s1-imfile.feishucdn.com/static-resource/v1/v2_44bbb547-b064-4a3f-999a-e5bb94e832cg~?image_size=72x72&cut_type=&quality=&format=png&sticker_format=.webp","avatar_240":"https://s1-imfile.feishucdn.com/static-resource/v1/v2_44bbb547-b064-4a3f-999a-e5bb94e832cg~?image_size=240x240&cut_type=&quality=&format=png&sticker_format=.webp"}
     * email :
     * mobile_visible : true
     */
    private String open_id;
    private String user_id;
    private String mobile;
    private String name;
    private String union_id;
    private String description;
    private String en_name;
    private AvatarEntity avatar;
    private String email;
    private boolean mobile_visible;

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnion_id(String union_id) {
        this.union_id = union_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public void setAvatar(AvatarEntity avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile_visible(boolean mobile_visible) {
        this.mobile_visible = mobile_visible;
    }

    public String getOpen_id() {
        return open_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getUnion_id() {
        return union_id;
    }

    public String getDescription() {
        return description;
    }

    public String getEn_name() {
        return en_name;
    }

    public AvatarEntity getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public boolean isMobile_visible() {
        return mobile_visible;
    }

    public class AvatarEntity {
        /**
         * avatar_640 : https://s1-imfile.feishucdn.com/static-resource/v1/v2_44bbb547-b064-4a3f-999a-e5bb94e832cg~?image_size=640x640&cut_type=&quality=&format=png&sticker_format=.webp
         * avatar_origin : https://s1-imfile.feishucdn.com/static-resource/v1/v2_44bbb547-b064-4a3f-999a-e5bb94e832cg~?image_size=noop&cut_type=&quality=&format=png&sticker_format=.webp
         * avatar_72 : https://s1-imfile.feishucdn.com/static-resource/v1/v2_44bbb547-b064-4a3f-999a-e5bb94e832cg~?image_size=72x72&cut_type=&quality=&format=png&sticker_format=.webp
         * avatar_240 : https://s1-imfile.feishucdn.com/static-resource/v1/v2_44bbb547-b064-4a3f-999a-e5bb94e832cg~?image_size=240x240&cut_type=&quality=&format=png&sticker_format=.webp
         */
        private String avatar_640;
        private String avatar_origin;
        private String avatar_72;
        private String avatar_240;

        public void setAvatar_640(String avatar_640) {
            this.avatar_640 = avatar_640;
        }

        public void setAvatar_origin(String avatar_origin) {
            this.avatar_origin = avatar_origin;
        }

        public void setAvatar_72(String avatar_72) {
            this.avatar_72 = avatar_72;
        }

        public void setAvatar_240(String avatar_240) {
            this.avatar_240 = avatar_240;
        }

        public String getAvatar_640() {
            return avatar_640;
        }

        public String getAvatar_origin() {
            return avatar_origin;
        }

        public String getAvatar_72() {
            return avatar_72;
        }

        public String getAvatar_240() {
            return avatar_240;
        }
    }
}
