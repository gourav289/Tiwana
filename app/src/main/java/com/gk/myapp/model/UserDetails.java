package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 19-02-2017.
 */
public class UserDetails {

    @SerializedName("return")
    private String success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private UserData userData;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public class UserData {
        @SerializedName("user_id")
        private String userId;
        @SerializedName("role")
        private String role;
        @SerializedName("admin_id")
        private String adminId;
        @SerializedName("parent_id")
        private String parentId;
        @SerializedName("name")
        private String name;
        @SerializedName("emp_code")
        private String empCode;
        @SerializedName("state_code")
        private String areaCode;
        @SerializedName("username")
        private String userName;
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;
        @SerializedName("visible_password")
        private String visiblePassword;
        @SerializedName("image")
        private String image;
        @SerializedName("age")
        private String age;
        @SerializedName("mobile_number")
        private String mobile;
        @SerializedName("employee_area")
        private String empArea;
        @SerializedName("state_name")
        private String state;
        @SerializedName("city")
        private String city;
        @SerializedName("address")
        private String address;
        @SerializedName("device_token")
        private String deviceToken;
        private String location;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getAdminId() {
            return adminId;
        }

        public void setAdminId(String adminId) {
            this.adminId = adminId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmpCode() {
            return empCode;
        }

        public void setEmpCode(String empCode) {
            this.empCode = empCode;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getVisiblePassword() {
            return visiblePassword;
        }

        public void setVisiblePassword(String visiblePassword) {
            this.visiblePassword = visiblePassword;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmpArea() {
            return empArea;
        }

        public void setEmpArea(String empArea) {
            this.empArea = empArea;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }
    }

}


//api call

//ApiInterface apiService =
//        ApiClient.getClient().create(ApiInterface.class);
//
//Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
//call.enqueue(new Callback<MoviesResponse>() {
//@Override
//public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
//        List<Movie> movies = response.body().getResults();
//        Log.d(TAG, "Number of movies received: " + movies.size());
//        }
//
//@Override
//public void onFailure(Call<MoviesResponse>call, Throwable t) {
//        // Log error here since request failed
//        Log.e(TAG, t.toString());
//        }
//        });