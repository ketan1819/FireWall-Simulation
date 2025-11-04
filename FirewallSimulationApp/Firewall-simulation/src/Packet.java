
public class Packet {
    public String srcIp, dstIp, proto;
    public Integer srcPort, dstPort;
    public String flags;

    public Packet(String s, String d, String p, Integer sp, Integer dp, String f) {
        this.srcIp = s;
        this.dstIp = d;
        this.proto = p;
        this.srcPort = sp;
        this.dstPort = dp;
        this.flags = f;
    }

    @Override
    public String toString() {
        return String.format("%s:%d -> %s:%d (%s)", srcIp, srcPort, dstIp, dstPort, proto);
    }
}
