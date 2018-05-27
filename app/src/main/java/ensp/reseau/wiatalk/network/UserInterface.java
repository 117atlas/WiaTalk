package ensp.reseau.wiatalk.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ensp.reseau.wiatalk.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserInterface {

    @GET("users/register_mobile/{mobile}")
    Call<GetUserResponse> registerMobile(@Path("mobile") String mobile);

    @POST("users/register_user")
    Call<GetUserResponse> registerUser(@Body User user);

    @POST("users/initialize")
    Call<BaseResponse> initializeIB(@Body UserContacts userContacts);

    @GET("users/updates/{user_id}")
    Call<GetUserResponse> updates(@Path("user_id") String userId);

    class GetUserResponse extends BaseResponse{
        @SerializedName("user") @Expose protected User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
