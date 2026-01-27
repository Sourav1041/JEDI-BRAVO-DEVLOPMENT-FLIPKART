package com.flipfit.dao;

import com.flipfit.bean.GymWaitList;
import java.util.List;

public interface WaitListDAO {
    boolean insertWaitList(GymWaitList waitList);
    List<GymWaitList> getWaitListBySlot(String slotId);
    boolean updateWaitListStatus(String waitListId, String status);
    boolean deleteWaitList(String waitListId);
    GymWaitList getFirstWaitingCustomer(String slotId);
}
