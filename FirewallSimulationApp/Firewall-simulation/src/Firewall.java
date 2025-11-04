import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Firewall {
    private ArrayList<Rule> rules = new ArrayList<>();
    public int allowed = 0, denied = 0, checked = 0;
    public Map<String, Integer> protocolAllowed = new HashMap<>();
    public Map<String, Integer> protocolDenied = new HashMap<>();
    private ArrayList<String> logEntries = new ArrayList<>();

    public void addRule(Rule r) {
        rules.add(r);
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public String evaluate(Packet p) {
        checked++;
        String actionTaken = "DENY (default)";
        Rule matchedRule = null;

        for (Rule r : rules) {
            if (r.matches(p)) {
                matchedRule = r;
                actionTaken = r.action.toUpperCase();
                break;
            }
        }

        if (actionTaken.equalsIgnoreCase("ALLOW")) allowed++;
        else denied++;

        // Update protocol-based stats
        String protoKey = p.proto.toUpperCase();
        if (actionTaken.equalsIgnoreCase("ALLOW")) {
            protocolAllowed.put(protoKey, protocolAllowed.getOrDefault(protoKey, 0) + 1);
        } else {
            protocolDenied.put(protoKey, protocolDenied.getOrDefault(protoKey, 0) + 1);
        }

        // Log the packet
        logPacket(p, matchedRule, actionTaken);
        return actionTaken;
    }

    private void logPacket(Packet p, Rule r, String action) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String ruleComment = (r != null && r.comment != null) ? r.comment : (r != null ? "matched rule" : "none");
        String logEntry = String.format("[%s] %s: %s via %s", timestamp, action, p, ruleComment);
        System.out.println(logEntry);
        logEntries.add(logEntry);
    }

    public void printStats() {
        System.out.println("\n===== Firewall Statistics =====");
        System.out.printf("Total Packets: %d | Allowed: %d | Denied: %d%n", checked, allowed, denied);
        System.out.println("Allowed per Protocol: " + protocolAllowed);
        System.out.println("Denied per Protocol: " + protocolDenied);
        System.out.println("===== End of Statistics =====\n");
    }

    public ArrayList<String> getLogs() {
        return logEntries;
    }
} // <-- Make sure this closing brace is present
