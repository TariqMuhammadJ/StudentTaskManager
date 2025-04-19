package com.studytask.observers;

import com.studytask.models.Group;
import com.studytask.models.User;

public interface GroupObserver {
    void onGroupCreated(Group group);
    void onGroupUpdated(Group group);
    void onGroupDeleted(int groupId);
    void onMemberJoined(Group group, User user);
    void onMemberLeft(Group group, User user);
}