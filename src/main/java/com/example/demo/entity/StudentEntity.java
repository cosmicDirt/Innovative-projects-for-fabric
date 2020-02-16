package com.example.demo.entity;

import java.sql.Date;

public class StudentEntity {
        private int userId;
        private String userName;
        private String userPassword;
        private String userSex;
        private String userPhone;
        private Date userAddtime;
        private String userAvatar;
        private Integer credits;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public Date getUserAddtime() {
            return userAddtime;
        }

        public void setUserAddtime(Date userAddtime) {
            this.userAddtime = userAddtime;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public Integer getCredits() {
            return credits;
        }

        public void setCredits(Integer credits) {
            this.credits = credits;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StudentEntity that = (StudentEntity) o;

            if (userId != that.userId) return false;
            if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
            if (userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null) return false;
            if (userSex != null ? !userSex.equals(that.userSex) : that.userSex != null) return false;
            if (userPhone != null ? !userPhone.equals(that.userPhone) : that.userPhone != null) return false;
            if (userAddtime != null ? !userAddtime.equals(that.userAddtime) : that.userAddtime != null) return false;
            if (userAvatar != null ? !userAvatar.equals(that.userAvatar) : that.userAvatar != null) return false;
            if (credits != null ? !credits.equals(that.credits) : that.credits != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = userId;
            result = 31 * result + (userName != null ? userName.hashCode() : 0);
            result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
            result = 31 * result + (userSex != null ? userSex.hashCode() : 0);
            result = 31 * result + (userPhone != null ? userPhone.hashCode() : 0);
            result = 31 * result + (userAddtime != null ? userAddtime.hashCode() : 0);
            result = 31 * result + (userAvatar != null ? userAvatar.hashCode() : 0);
            result = 31 * result + (credits != null ? credits.hashCode() : 0);
            return result;
        }
}
