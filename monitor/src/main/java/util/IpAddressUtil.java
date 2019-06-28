package util;

public class IpAddressUtil {

    public static final String IP_PREFIX = "192.168.";

    /**
     *
     * @param maxPods ip address generation count ie from 0 to 9
     * @param maxServerPerPod ip address generation count ie from 0 to 99
     * @return array of ip addresses
     * @throws InvalidInputException if the range of IP address is not valid
     */
    public String[] generateIPForServers(int maxPods, int maxServerPerPod) throws InvalidInputException {
        if (maxPods <= 0 || maxPods > 256 || (maxServerPerPod > 256)) {
            InvalidInputException exception = new InvalidInputException("Invalid server pods or server count");
            throw exception;
        }

        final String[] ipAddresses = new String[maxPods * maxServerPerPod];
        int k = 0;
        for (int i = 0; i < maxPods; i++) {
            for (int j = 0; j < maxServerPerPod; j++) {
                ipAddresses[k] = IP_PREFIX + i + "." + j;
                k++;
            }
        }

        return ipAddresses;
    }

    public static class InvalidInputException extends Exception {
        public InvalidInputException(String s) {
            super(s);
        }
    }

    public boolean isIPValid(String ipAddress) {
        if (ipAddress == null || ipAddress.length() == 0) return false;
        String[] tokens = ipAddress.split("\\.");
        if (tokens.length < 4) return false;
        for (String token : tokens) {
            int number = Integer.valueOf(token);
            if (number < 0 || number > 255) return false;
        }

        return true;
    }
}
