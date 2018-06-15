package ensp.reseau.wiatalk.localstorage;

import android.content.Context;

import java.util.ArrayList;

import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.IPCall;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.MessageFile;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.tmodels.Call;

public class LocalStorageMessages {
    public static User getUserById(String userId, Context context){
        UserDAO userDAO = new UserDAO(context);
        userDAO.open();
        User sender = null;
        try{
            sender = userDAO.getUserById(userId);
        } finally {
            userDAO.close();
        }
        return sender;
    }

    public static Message getMessageById(String messageId, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        messageDAO.open();
        Message message = null;
        try{
            message = messageDAO.getMessagesById(messageId);
        } finally {
            messageDAO.close();
        }
        return message;
    }

    public static MessageFile getMessageFileById(String messageFileId, Context context){
        MessageFileDAO messageFileDAO = new MessageFileDAO(context);
        messageFileDAO.open();
        MessageFile messageFile = null;
        try{
            messageFile = messageFileDAO.getMessageFileById(messageFileId);
        } finally {
            messageFileDAO.close();
        }
        return messageFile;
    }

    public static ArrayList<Message> getGroupsMessages(Group group, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        messageDAO.open();
        ArrayList<Message> messages = null;
        try{
            messages = messageDAO.getGroupsMessages(group.get_id());
            if (messages!=null){
                for (Message message: messages){
                    message.setGroup(group);
                    message.setSender(getUserById(message.getSenderId(), context));
                    message.setReply(getMessageById(message.getReplyId(), context));
                    message.setFeedbacks();
                    if (message.getReceivedIds()!=null) for (String received: message.getReceivedIds()) message.addReceived(LocalStorageUser.getUserById(received, context));
                    if (message.getReadIds()!=null) for (String read: message.getReadIds()) message.addRead(LocalStorageUser.getUserById(read, context));
                    if (message.getFileId()!=null && !message.getFileId().isEmpty()) message.setFile(getMessageFileById(message.getFileId(), context));
                }
            }
        } finally {
            messageDAO.close();
        }
        return messages;
    }

    public static Message lastGroupMessage(Group group, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        messageDAO.open();
        Message lastMessage = null;
        try{
            lastMessage = messageDAO.getLastGroupMessage(group.get_id());
            if (lastMessage==null) return null;
            lastMessage.setGroup(group);
            lastMessage.setSender(getUserById(lastMessage.getSenderId(), context));
            lastMessage.setReply(getMessageById(lastMessage.getReplyId(), context));
            lastMessage.setFeedbacks();
            if(lastMessage.getReceivedIds()!=null) for (String received: lastMessage.getReceivedIds()) lastMessage.addReceived(LocalStorageUser.getUserById(received, context));
            if(lastMessage.getReadIds()!=null) for (String read: lastMessage.getReadIds()) lastMessage.addRead(LocalStorageUser.getUserById(read, context));
            if (lastMessage.getFileId()!=null && !lastMessage.getFileId().isEmpty()) lastMessage.setFile(getMessageFileById(lastMessage.getFileId(), context));
        } finally {
            messageDAO.close();
        }
        return lastMessage;
    }

    public static void storeMessages(Message message, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        try{
            messageDAO.open();
            messageDAO.storeMessage(message);
            if (message.getFile()!=null) storeMessageFile(message.getFile(), context);
        } finally {
            messageDAO.close();
        }
    }

    public static void deleteMessageByMyTimestamp(long timestamp, Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        try{
            messageDAO.open();
            messageDAO.deleteMessageByTimestamp(timestamp);
        } finally {
            messageDAO.close();
        }
    }

    public static void deleteMessageFileById(String id, Context context){
        MessageFileDAO messageFileDAO = new MessageFileDAO(context);
        messageFileDAO.open();
        try{
            messageFileDAO.deleteMessageFile(id);
        } finally {
            messageFileDAO.close();
        }
    }

    public static void storeIPCalls(IPCall ipCall, Context context){
        IPCallsDAO ipCallsDAO = new IPCallsDAO(context);
        try{
            ipCallsDAO.open();
            ipCallsDAO.storeIPCall(ipCall);
        } finally {
            ipCallsDAO.close();
        }
    }

    public static void storeMessageFile(MessageFile messageFile, Context context){
        MessageFileDAO messageFileDAO = new MessageFileDAO(context);
        try{
            messageFileDAO.open();
            messageFileDAO.storeMessageFile(messageFile);
        } finally {
            messageFileDAO.close();
        }
    }

    public static void updateMessageFile(MessageFile messageFile, Context context){
        MessageFileDAO messageFileDAO = new MessageFileDAO(context);
        try{
            messageFileDAO.open();
            messageFileDAO.updateMessageFile(messageFile);
        } finally {
            messageFileDAO.close();
        }
    }

    public static ArrayList<Message> getPendingMessages(Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        messageDAO.open();
        try{
            return messageDAO.getPendingMessages();
        } finally {
            messageDAO.close();
        }
    }

    public static void deletePendingMessage(Context context){
        MessageDAO messageDAO = new MessageDAO(context);
        messageDAO.open();
        try{
            messageDAO.deletePendingMessage();
        } finally {
            messageDAO.close();
        }
    }
}
