package com.anastasia.notifications.datasource;

import com.anastasia.notifications.entity.TelegramChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramChatRepository extends JpaRepository<TelegramChat, Long> {
}
