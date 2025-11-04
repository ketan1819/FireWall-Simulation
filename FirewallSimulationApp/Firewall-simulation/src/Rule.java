import java.net.InetAddress;

public class Rule {
    public String action;   // ALLOW or DENY
    public String proto;    // may be null or ANY
    public String srcNet;   // e.g. "192.168.1.0/24" or range "192.168.1.1-192.168.1.255"
    public String dstNet;
    public String srcPort;  // e.g. "80" or range "1000-2000" or ANY
    public String dstPort;
    public String comment;

    public Rule(String action) {
        this.action = action.toUpperCase();
    }

    // Helper: Check if IP is in CIDR or range or ANY
    private boolean ipMatches(String ip, String ruleIp) {
        if (ruleIp == null || ruleIp.equalsIgnoreCase("ANY") || ruleIp.equals("*")) return true;
        try {
            if (ruleIp.contains("-")) { // IP range
                String[] parts = ruleIp.split("-");
                long ipVal = ipToLong(ip);
                return ipVal >= ipToLong(parts[0].trim()) && ipVal <= ipToLong(parts[1].trim());
            } else if (ruleIp.contains("/")) { // CIDR
                return ipInCidr(ip, ruleIp);
            } else { // Single IP
                return ip.equals(ruleIp);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private long ipToLong(String ip) throws Exception {
        byte[] octets = InetAddress.getByName(ip).getAddress();
        long result = 0;
        for (byte octet : octets) {
            result = (result << 8) | (octet & 0xFF);
        }
        return result;
    }

    private boolean ipInCidr(String ip, String cidr) throws Exception {
        String[] parts = cidr.split("/");
        int prefix = Integer.parseInt(parts[1]);
        byte[] addr = InetAddress.getByName(ip).getAddress();
        byte[] net = InetAddress.getByName(parts[0]).getAddress();
        int bits = prefix;

        for (int i = 0; i < addr.length; i++) {
            int mask = 0;
            if (bits >= 8) mask = 0xFF;
            else if (bits > 0) mask = ~((1 << (8 - bits)) - 1) & 0xFF;

            if ((addr[i] & mask) != (net[i] & mask)) return false;
            bits -= 8;
            if (bits < 0) bits = 0;
        }
        return true;
    }

    // Helper: Check if port matches (single, range, ANY)
    private boolean portMatches(int port, String rulePort) {
        if (rulePort == null || rulePort.equalsIgnoreCase("ANY") || rulePort.equals("*")) return true;
        if (rulePort.contains("-")) {
            String[] parts = rulePort.split("-");
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());
            return port >= start && port <= end;
        } else {
            return port == Integer.parseInt(rulePort.trim());
        }
    }

    public boolean matches(Packet p) {
        if (proto != null && !proto.equalsIgnoreCase("ANY") && !proto.equalsIgnoreCase(p.proto)) return false;
        if (!ipMatches(p.srcIp, srcNet)) return false;
        if (!ipMatches(p.dstIp, dstNet)) return false;
        if (!portMatches(p.srcPort, srcPort)) return false;
        if (!portMatches(p.dstPort, dstPort)) return false;
        return true;
    }
}
