package indi.sunyc.footprints.domain.user.entity;

/**
 * Created by ChamIt-001 on 2017/10/10.
 */
public class User {

    public static enum SEX {
        UNDEFINED(0),MALE(1),FEMALE(2);
        private Integer value;
        SEX(Integer value){this.value = value;}
        public Integer getValue(){return this.value;}
    }

    private String nickName;
    private String profilePhoto;
    private Integer sex;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
