package com.project.smart.model;

import com.project.smart.Smart;
import java.util.concurrent.ExecutorService;

public interface Drone {

    void run(ExecutorService executorService);

    void addSubscriber(Smart.Account account);

    void removeSubscriber(Smart.Account account);

    boolean isEmpty();

    void stop(boolean forcibly);
}
