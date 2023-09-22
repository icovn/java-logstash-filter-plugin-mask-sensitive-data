package org.logstashplugins;

public class SecurityUtil {

    /**
     * Mask sensitive data from the input with "***"
     * e.g: "Customer phone:0123456789 has phone:0123456789" should return "Customer phone:*** has phone:***"
     * @param input The input string
     * @param pattern The sensitive pattern
     * @return Masked data
     */
    public static String maskSensitive(String input, String pattern) {
        if (input == null || pattern == null || pattern.isEmpty() || !input.contains(pattern)) {
            return input;
        }

        int markedIndex = input.indexOf(pattern);
        String result = input;
        while (true) {
            String firstPart = result.substring(0, markedIndex);

            String secondPath = "";
            int nextMarkIndex = result.indexOf(",", markedIndex + pattern.length());
            if (nextMarkIndex > -1) {
                secondPath = result.substring(nextMarkIndex, result.length() - 1);
            }
            result = firstPart + pattern + "***" + secondPath;
            if (nextMarkIndex < 0) {
                break;
            }

            markedIndex = input.indexOf(pattern, markedIndex);
            if (markedIndex < 0) {
                break;
            }
        }
        return result;
    }
}
