package com.poniarcade.core.structs;

import java.time.LocalDateTime;

/**
 * Created by appledash on 7/24/17.
 * Blackjack is best pony.
 */
public record MailMessage(String senderName, String receiverName, LocalDateTime time, String message) {

}
