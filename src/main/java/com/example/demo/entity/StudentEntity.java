package com.example.demo.entity;

import java.sql.Date;

public class StudentEntity {
        private String stuNumber;
        private String name;
        private String password;
        private String gender;
        private String phone;
        private String userAvatar;
        private String email;

        public String getStuNumber() {
            return stuNumber;
        }

        public void setStuNumber(String stuNumber) {
            this.stuNumber = stuNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StudentEntity that = (StudentEntity) o;
            if (stuNumber != null ? !stuNumber.equals(that.stuNumber) : that.stuNumber != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (password != null ? !password.equals(that.password) : that.password != null) return false;
            if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
            if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
            if (userAvatar != null ? !userAvatar.equals(that.userAvatar) : that.userAvatar != null) return false;
            if (email != null ? !email.equals(that.email) : that.email != null) return false;

            return true;
        }
        
}
