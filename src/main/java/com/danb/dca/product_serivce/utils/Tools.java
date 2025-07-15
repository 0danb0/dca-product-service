package com.danb.dca.product_serivce.utils;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

import static com.danb.dca.product_serivce.utils.ConstantStrings.APPENDIX_PK;
import static com.danb.dca.product_serivce.utils.ConstantStrings.ROOT_PK;

@Slf4j
@Component
public class Tools {

    public String getInstant() {
        DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTime();
        DateTime dt = DateTime.now(DateTimeZone.UTC);
        return dt.toString(dateFormatter);
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    public String createPk(String applicationId) {
        return String.join("#", ROOT_PK, applicationId, APPENDIX_PK);
    }

    public byte[] downloadFileFromUrl(String fileUrl) throws IOException {
        try (InputStream in = new URL(fileUrl).openStream()) {
            return in.readAllBytes();
        }
    }

    public String extractFilenameFromUrl(String fileUrl) {
        try {
            return Paths.get(new URI(fileUrl).getPath()).getFileName().toString();
        } catch (Exception e) {
            log.warn("Failed to extract filename from URL: {}", fileUrl);
            return UUID.randomUUID() + ".dat"; // fallback
        }
    }
}
