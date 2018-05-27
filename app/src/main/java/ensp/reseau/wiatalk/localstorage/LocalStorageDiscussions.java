package ensp.reseau.wiatalk.localstorage;

import android.content.Context;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.AdminsGroups;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.model.UsersGroups;

public class LocalStorageDiscussions {

    public static Group getGroupById(String groupId, Context context){
        GroupDAO groupDAO = new GroupDAO(context);
        Group group = null;
        try{
            groupDAO.open();
            group = groupDAO.getGroupById(groupId);
        } catch (Exception e) {e.printStackTrace();}
        finally {
            groupDAO.close();
        }
        return group;
    }

    public static ArrayList<Group> getAllDiscussions(Context context){
        GroupDAO groupDAO = new GroupDAO(context);
        ArrayList<Group> groups = new ArrayList<>();
        try{
            groups = groupDAO.getAllGroups();
            for (Group group: groups){
                group.setLastMessage(getLastMessage(group.get_id(), context));
                ArrayList<Message> newMessages = getNewMessages(group.get_id(), context);
                group.setNewMessages(newMessages==null?0:newMessages.size());
            }
            //Eliminate groups with zero messages
            int i = 0;
            while  (i<groups.size()){
                Group group = groups.get(i);
                if (group.getLastMessage()==null || group.getNewMessages()==0) groups.remove(i);
                else i++;
            }
        } finally {
            groupDAO.close();
        }
        return groups;
    }

    public static Message getLastMessage(String groupId, Context context) {
        MessageDAO messageDAO = new MessageDAO(context);
        Message lastMessage = null;
        try{
            messageDAO.open();
            lastMessage = messageDAO.getLastGroupMessage(groupId);
        } catch (Exception e){ e.printStackTrace();}
        finally {
            messageDAO.close();
        }
        return lastMessage;
    }

    public static ArrayList<Message> getNewMessages(String groupId, Context context) {
        MessageDAO messageDAO = new MessageDAO(context);
        ArrayList<Message> newMessages = null;
        try{
            messageDAO.open();
            newMessages = messageDAO.getGroupNewMessages(groupId);
        } catch (Exception e){e.printStackTrace();}
        finally {
            messageDAO.close();
        }
        return newMessages;
    }

    private static ArrayList<AdminsGroups> getGroupAdminsIds(String groupId, Context context){
        AdminsGroupsDAO adminsGroupsDAO = new AdminsGroupsDAO(context);
        ArrayList<AdminsGroups> adminsGroups = new ArrayList<>();
        try{
            adminsGroupsDAO.open();
            adminsGroups = adminsGroupsDAO.getGroupAdmins(groupId);
        } catch (Exception e){e.printStackTrace();}
        finally {
            adminsGroupsDAO.close();
        }
        return adminsGroups;
    }

    private static ArrayList<UsersGroups> getGroupMembersIds(String groupId, Context context){
        UsersGroupsDAO usersGroupsDAO = new UsersGroupsDAO(context);
        ArrayList<UsersGroups> members = new ArrayList<>();
        try{
            usersGroupsDAO.open();
            members = usersGroupsDAO.getGroupMembersIds(groupId);
        } catch (Exception e){e.printStackTrace();}
        finally {
            usersGroupsDAO.close();
        }
        return members;
    }

    public static void populateGroup(Group group, Context context){
        ArrayList<AdminsGroups> admins = getGroupAdminsIds(group.get_id(), context);
        for (AdminsGroups admin: admins){
            admin.setAdmin(LocalStorageUser.getUserById(admin.getMemberId(), context));
        }

        ArrayList<UsersGroups> memebers = getGroupMembersIds(group.get_id(), context);
        for (UsersGroups member: memebers){
            member.setMember(LocalStorageUser.getUserById(member.getMemberId(), context));
        }

        group.setAdmins(admins);
        group.setMembers(memebers);
        group.setMessages(LocalStorageMessages.getGroupsMessages(group, context));
        group.setCreator(LocalStorageUser.getUserById(group.getCreatorId(), context));
    }

    public static void storeUsersGroups(UsersGroups usersGroups, Context context){
        UsersGroupsDAO usersGroupsDAO = new UsersGroupsDAO(context);
        try{
            usersGroupsDAO.open();
            UsersGroups existingusersGroups = usersGroupsDAO.getUsersGroupsByIds(usersGroups.getMemberId(), usersGroups.getGroupId());
            if (existingusersGroups==null) usersGroupsDAO.storeUserGroup(usersGroups);
            else usersGroupsDAO.updateUserGroup(usersGroups);
        } finally {
            usersGroupsDAO.close();
        }
    }

    public static void storeAdminsGroups(AdminsGroups adminsGroups, Context context){
        AdminsGroupsDAO adminsGroupsDAO = new AdminsGroupsDAO(context);
        try{
            adminsGroupsDAO.open();
            AdminsGroups existingAdminsGroups = adminsGroupsDAO.getAdminsGroupsByIds(adminsGroups.getMemberId(), adminsGroups.getGroupId());
            if (existingAdminsGroups==null) adminsGroupsDAO.storeAdminGroup(adminsGroups);
            //else adminsGroupsDAO.up(usersGroups);
        } finally {
            adminsGroupsDAO.close();
        }
    }

    public static void storeGroup(Group group, Context context) {
        GroupDAO groupDAO = new GroupDAO(context);
        try {
            groupDAO.open();
            Group existingGroup = groupDAO.getGroupById(group.get_id());
            if (existingGroup == null) groupDAO.storeGroup(group);
            else groupDAO.updateGroup(group);

            for (UsersGroups usersGroups : group.getMembers()) {
                LocalStorageUser.storeUser(usersGroups.getMember(), context, false);
                storeUsersGroups(usersGroups, context);
            }

            for (AdminsGroups adminsGroups : group.getAdmins()) {
                storeAdminsGroups(adminsGroups, context);
            }

            for (Message message : group.getMessages()) {
                LocalStorageMessages.storeMessages(message, context);
            }

        } finally {
            groupDAO.close();
        }
    }
}
