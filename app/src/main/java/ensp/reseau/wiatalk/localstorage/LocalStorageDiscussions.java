package ensp.reseau.wiatalk.localstorage;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import ensp.reseau.wiatalk.model.AdminsGroups;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.MessageFile;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.model.UsersGroups;
import ensp.reseau.wiatalk.ui.activities.GroupInfosActivity;

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

    public static Group getIB(String userId, Context context){
        GroupDAO groupDAO = new GroupDAO(context);
        groupDAO.open();
        try{
            User me = LocalStorageUser.getMe(context);
            if (me==null) return null;
            String ibId = groupDAO.getIb(me.get_Id(), userId);
            if (ibId==null) return null;
            return getGroupById(ibId, context);
        } finally {
            groupDAO.close();
        }
    }

    public static ArrayList<Group> getAllDiscussions(Context context){
        GroupDAO groupDAO = new GroupDAO(context);
        ArrayList<Group> groups = new ArrayList<>();
        groupDAO.open();
        try{
            groups = groupDAO.getAllGroups();
            if (groups==null) return null;
            for (Group group: groups){
                group.setLastMessage(LocalStorageMessages.lastGroupMessage(group, context));
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

    public static ArrayList<Message> getAllNewMessages(Context context) {
        MessageDAO messageDAO = new MessageDAO(context);
        ArrayList<Message> newMessages = null;
        try{
            messageDAO.open();
            newMessages = messageDAO.getAllNewMessages();
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
        if (admins!=null){
            for (AdminsGroups admin: admins){
                admin.setAdmin(LocalStorageUser.getUserById(admin.getMemberId(), context));
            }
        }

        ArrayList<UsersGroups> members = getGroupMembersIds(group.get_id(), context);
        if (members!=null){
            for (UsersGroups member: members){
                member.setMember(LocalStorageUser.getUserById(member.getMemberId(), context));
            }
        }

        group.setAdmins(admins);
        group.setMembers(members);
        group.setMessages(LocalStorageMessages.getGroupsMessages(group, context));
        group.setCreator(LocalStorageUser.getUserById(group.getCreatorId(), context));
    }

    public static void reloadGroupMessages(Group group, Context context){
        group.setMessages(LocalStorageMessages.getGroupsMessages(group, context));
    }

    public static void populateGroupInfos(Group group, Context context){
        ArrayList<AdminsGroups> admins = getGroupAdminsIds(group.get_id(), context);
        if (admins!=null){
            for (AdminsGroups admin: admins){
                admin.setAdmin(LocalStorageUser.getUserById(admin.getMemberId(), context));
            }
        }

        ArrayList<UsersGroups> members = getGroupMembersIds(group.get_id(), context);
        if (members!=null){
            for (UsersGroups member: members){
                member.setMember(LocalStorageUser.getUserById(member.getMemberId(), context));
            }
        }

        group.setAdmins(admins);
        group.setMembers(members);
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

    public static void deleteAllAdminsGroupForGroup(String groupId, Context context){
        AdminsGroupsDAO adminsGroupsDAO = new AdminsGroupsDAO(context);
        try{
            adminsGroupsDAO.open();
            adminsGroupsDAO.deleteAllForGroup(groupId);
        } finally {
            adminsGroupsDAO.close();
        }
    }

    public static void deleteAllUsersGroupForGroup(String groupId, Context context){
        UsersGroupsDAO usersGroupsDAO = new UsersGroupsDAO(context);
        try{
            usersGroupsDAO.open();
            usersGroupsDAO.deleteAllForGroup(groupId);
        } finally {
            usersGroupsDAO.close();
        }
    }

    public static void updateGroup(Group group, Context context){
        GroupDAO groupDAO = new GroupDAO(context);
        groupDAO.open();
        try{
            groupDAO.updateGroup(group);
        } finally {
            groupDAO.close();
        }
    }

    public static void storeGroup(Group group, Context context) {
        GroupDAO groupDAO = new GroupDAO(context);
        try {
            groupDAO.open();
            group.setCreatorId(group.getCreator()==null?null:group.getCreator().get_Id());
            Group existingGroup = groupDAO.getGroupById(group.get_id());
            if (existingGroup == null) groupDAO.storeGroup(group);
            else groupDAO.updateGroup(group);

            deleteAllUsersGroupForGroup(group.get_id(), context);
            for (UsersGroups usersGroups : group.getMembers()) {
                LocalStorageUser.storeUser(usersGroups.getMember(), context, false);
                usersGroups.setGroupId(group.get_id());
                usersGroups.setMemberId();
                storeUsersGroups(usersGroups, context);
            }

            deleteAllAdminsGroupForGroup(group.get_id(), context);
            for (AdminsGroups adminsGroups : group.getAdmins()) {
                adminsGroups.setGroupId(group.get_id());
                adminsGroups.setAdminId();
                storeAdminsGroups(adminsGroups, context);
            }

            /*for (Message message : group.getMessages()) {
                LocalStorageMessages.storeMessages(message, context);
            }*/
            System.out.println("FINI");

        } finally {
            groupDAO.close();
        }
    }

    public static void storeGroupInfos(Group group, Context context) {
        GroupDAO groupDAO = new GroupDAO(context);
        try {
            groupDAO.open();
            group.setCreatorId(group.getCreator()==null?null:group.getCreator().get_Id());
            Group existingGroup = groupDAO.getGroupById(group.get_id());
            if (existingGroup == null) groupDAO.storeGroup(group);
            else groupDAO.updateGroup(group);

            deleteAllUsersGroupForGroup(group.get_id(), context);
            for (UsersGroups usersGroups : group.getMembers()) {
                LocalStorageUser.storeUser(usersGroups.getMember(), context, false);
                usersGroups.setGroupId(group.get_id());
                usersGroups.setMemberId();
                storeUsersGroups(usersGroups, context);
            }

            deleteAllAdminsGroupForGroup(group.get_id(), context);
            for (AdminsGroups adminsGroups : group.getAdmins()) {
                adminsGroups.setGroupId(group.get_id());
                adminsGroups.setAdminId();
                storeAdminsGroups(adminsGroups, context);
            }

        } finally {
            groupDAO.close();
        }
    }

    public static void storeGroupMessages(Group group, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        MessageFileDAO messageFileDAO = new MessageFileDAO(context);
        try{
            messageDAO.open();
            messageFileDAO.open();
            for (Message message: group.getMessages()){
                message.arrangeForLocalStorage();
                if (messageDAO.getMessagesById(message.get_id())!=null) messageDAO.updateMessage(message);
                else messageDAO.storeMessage(message);
                if (message.getFile()!=null) messageFileDAO.storeMessageFile(message.getFile());
            }
        } finally {
            messageDAO.close();
            messageFileDAO.close();
        }
    }

    public static ArrayList<Group> getCommonGroups(String memberId, Context context){
        GroupDAO groupDAO = new GroupDAO(context);
        groupDAO.open();
        try{
            ArrayList<String> groupsIds = groupDAO.getCommonGroups(memberId);
            if (groupsIds==null) return null;
            ArrayList<Group> commonGroups = new ArrayList<>();
            groupDAO.close();
            for (String s: groupsIds) {
                Group group = getGroupById(s, context);
                if (group.getType()==Group.TYPE_GROUP){
                    populateGroupInfos(group, context);
                    commonGroups.add(group);
                }
            }
            return commonGroups;
        } finally {
            groupDAO.close();
        }
    }
}
