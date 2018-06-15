package ensp.reseau.wiatalk.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ensp.reseau.wiatalk.model.Group;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GroupInterface {

    @POST("groups/create/{members}")
    Call<BaseResponse> createGroup(@Body Group group, @Path("members") String members);

    @POST("groups/update/{updater}")
    Call<BaseResponse> updateGroup(@Body Group group, @Path("updater") String updater);

    @POST("groups/addmembers/{group_id}")
    Call<GetGroupResponse> addMembersInGroup(@Body AddMembersBody members, @Path("group_id") String groupId);

    @GET("groups/removemember/{group_id}/{member}/{remover}")
    Call<GetGroupResponse> removeMemberFromGroup(@Path("group_id") String groupId, @Path("member") String member, @Path("remover") String remover);

    @GET("groups/addadmin/{group_id}/{admin}")
    Call<GetGroupResponse> addAdminForGroup(@Path("group_id") String groupId, @Path("admin") String admin);

    @GET("groups/removeadmin/{group_id}/{admin}")
    Call<GetGroupResponse> removeAdminForGroup(@Path("group_id") String groupId, @Path("admin") String admin);

    class AddMembersBody{
        @SerializedName("members") @Expose private List<String> members;

        public List<String> getMembers() {
            return members;
        }

        public void setMembers(List<String> members) {
            this.members = members;
        }
    }

    class GetGroupResponse extends BaseResponse{
        @SerializedName("group") @Expose private Group group;

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }
    }
}
