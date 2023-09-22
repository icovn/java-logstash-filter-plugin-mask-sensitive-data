package org.logstashplugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SecurityUtil {
    private static final Logger logger = LogManager.getLogger(SecurityUtil.class);

    /**
     * Mask sensitive data from the input with "***"
     * e.g: "Customer phone:0123456789 has phone:0123456789" should return "Customer phone:*** has phone:***"
     * @param input The input string
     * @param pattern The sensitive pattern
     * @return Masked data
     */
    public static String maskSensitive(String input, String pattern) {
        logger.debug("(maskSensitive)input: {}, pattern: {}", input, pattern);
        if (input == null || pattern == null || input.isEmpty() || pattern.isEmpty() || !input.contains(pattern)) {
            return input;
        }

        String result = input;
        int markIndex = result.indexOf(pattern);
        while (true) {
            logger.debug("(maskSensitive)markIndex: {}", markIndex);

            // get the first part
            String firstPart = result.substring(0, markIndex);
            logger.debug("(maskSensitive)firstPart: {}", firstPart);

            // get the second part
            String secondPath = "";
            int nextMarkIndex = result.indexOf(",", markIndex + pattern.length());
            if (nextMarkIndex > -1) {
                secondPath = result.substring(nextMarkIndex);
            }
            logger.debug("(maskSensitive)secondPath: {}", secondPath);

            // combine parts
            result = firstPart + pattern + "***" + secondPath;
            logger.debug("(maskSensitive)result: {}", result);

            if (nextMarkIndex < 0) {
                break;
            }

            markIndex = result.indexOf(pattern, markIndex + pattern.length());
            if (markIndex < 0) {
                break;
            }
        }
        return result;
    }

    public static String maskSensitiveMultiple(String input, String pattern) {
        logger.debug("(maskSensitiveMultiple)input: {}, pattern: {}", input, pattern);
        if (input == null || pattern == null || input.isEmpty() || pattern.isEmpty()) {
            return input;
        }

        String result = input;
        for(String _pattern: pattern.split(",")) {
            result = maskSensitive(result, _pattern);
        }
        return result;
    }
}
