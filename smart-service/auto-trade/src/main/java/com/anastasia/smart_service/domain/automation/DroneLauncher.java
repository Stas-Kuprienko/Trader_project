package com.anastasia.smart_service.domain.automation;

import com.anastasia.smart_service.Smart;

public interface DroneLauncher {

    void run(Smart.SubscribeRequest request, Smart.SubscribeResponse response);
}
