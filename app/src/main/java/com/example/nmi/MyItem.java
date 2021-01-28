package com.example.nmi;

import com.example.nmi.model.LikeModel;

public class MyItem {
        private String petname;
        private String imageurl;
        private String description;
        private String phone;
        private String price;
        private String adress;
        //
//        private String likeCount;
//        private boolean is_Liked;

        //
        private String user_id;
        private String postId;



        public MyItem() {


        }

        public MyItem(String petname, String imageurl, String description, String phone, String price, String adress) {
            this.petname = petname;
            this.imageurl = imageurl;
            this.description = description;
            this.phone = phone;
            this.price = price;
            this.adress = adress;
        }

    public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }




        public String getPetname() {
            return petname;
        }

        public void setPetname(String petname) {
            this.petname = petname;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }
    }
