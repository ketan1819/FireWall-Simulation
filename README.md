Enhanced Firewall Simulation System - Major Project Documentation

# Enhanced Firewall Simulation System

**Project Type:** Mini Project
**Technology:** Java 17, JavaFX 20
**Project Duration:** 4–6 Weeks
**Developed By:** Ketan Sonawane

---

## Abstract

The Enhanced Firewall Simulation System is a GUI-based application designed to simulate firewall operations, analyze network packets, and manage firewall rules. The system provides a dynamic environment to test packets individually, run bulk simulations, and visualize firewall performance statistics. This project helps in understanding packet filtering, rule prioritization, and network security mechanisms in a controlled, simulated environment.

---

## Introduction

Firewalls are an essential part of network security. They monitor incoming and outgoing traffic, allowing or denying packets based on defined rules. The Enhanced Firewall Simulation System provides a visual interface to simulate firewall operations and analyze the effect of various rules on network packets. This system is implemented in Java 17 using JavaFX 20, enabling an interactive GUI with tabs for packet testing, firewall rule management, simulation, and statistics.

---

## Objectives

1. Develop a GUI-based firewall simulation system.
2. Implement dynamic firewall rule creation, deletion, and prioritization.
3. Simulate individual and bulk network packets.
4. Generate detailed logs and traffic statistics.
5. Provide insights into network security and firewall operations.

---

## Tools and Technologies

* Programming Language: Java 17
* GUI Framework: JavaFX 20
* IDE: IntelliJ IDEA / Eclipse / VS Code
* Operating System: Windows 10/11, Linux, macOS
* Libraries/Modules:

  * javafx.controls
  * javafx.fxml
  * Java standard libraries (java.time, java.util)

---

## System Requirements

* Minimum 4GB RAM, 2GHz Processor
* JDK 17 installed and configured (JAVA_HOME)
* JavaFX SDK 20 or lower
* IDE (Optional) for editing and compiling

---

## Modules

### 1. Packet Testing

* Input: Source IP, Destination IP, Source Port, Destination Port, Protocol, Packet Size
* Function: Validates the packet and checks against firewall rules.
* Output: Displays detailed packet analysis and verdict (ALLOW/DENY).

### 2. Firewall Rules

* Input: Rule Name, Action (ALLOW/DENY), IP Pattern, Port Pattern, Protocol, Priority
* Function: Adds, deletes, and manages firewall rules dynamically.
* Feature: Rules evaluated based on priority, wildcards supported.

### 3. Simulation & Logs

* Input: Number of packets, randomization option, detailed logging toggle
* Function: Runs bulk simulations, processes multiple packets, and generates logs.
* Output: Detailed logs showing each packet, matched rule, and verdict.

### 4. Statistics

* Function: Tracks total packets processed, allowed, denied, and allow rate.
* Feature: Refresh or reset statistics dynamically.

---

## Methodology

1. Packet Input & Validation: Ensures IP addresses, ports, and packet size are valid.
2. Rule Matching: Firewall rules are evaluated in priority order; supports wildcards.
3. Verdict Generation: Each packet is either ALLOWED or DENIED based on rules or default policy.
4. Logging & Visualization: Logs provide timestamped packet analysis; GUI displays statistics.
5. Simulation: Randomized packets or user-defined packets simulate real-time network traffic.

**Layer of Operation:**

* Network Layer: IP filtering
* Transport Layer: TCP/UDP port filtering
* Application Layer: Protocol-specific filtering (HTTP, HTTPS)

---

## Installation and Setup

1. Install Java 17 and set environment variable:

```cmd
setx JAVA_HOME "C:\Program Files\Java\jdk-17.0.12"
setx PATH "%JAVA_HOME%\bin;%PATH%"
```

2. Download JavaFX 20 SDK and extract to a folder:

```
C:\Users\<YourUser>\Downloads\javafx-sdk-20
```

3. Compile JavaFX project:

```cmd
javac --module-path "C:\Users\<YourUser>\Downloads\javafx-sdk-20\lib" --add-modules javafx.controls,javafx.fxml *.java
```

4. Run the application:

```cmd
java --module-path "C:\Users\<YourUser>\Downloads\javafx-sdk-20\lib" --add-modules javafx.controls,javafx.fxml EnhancedFirewallSimulation
```

---

## Project Structure

```
FirewallSimulationApp/
│
├─ src/
│   ├─ EnhancedFirewallSimulation.java
│
├─ lib/ (JavaFX SDK path)
├─ README.md
```

---

## Dependencies

### 1. Java Development Kit (JDK)
- **Version:** Java 17 (LTS recommended)  
- **Requirement:** Ensure `JAVA_HOME` is set correctly.

### 2. JavaFX SDK
- **Version:** JavaFX 20 (compatible with JDK 17)  
- **Download:** [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)  
- **Required Modules:** `javafx.controls`, `javafx.fxml`  

### 3. IDE / Build Tool
- Recommended: **VS Code**, **IntelliJ IDEA**, or **Eclipse**  

### 4. Operating System
- Windows 10/11, Linux, or macOS  

---

## Rules Configuration

* IP Pattern: Specific IP or wildcard (e.g., 192.168.1.*)
* Port Pattern: Specific port or wildcard (*)
* Protocol: TCP, UDP, HTTP, HTTPS, ICMP, ANY
* Priority: Integer; lower number = higher priority
* Action: ALLOW or DENY

---

## Example Rules

| Rule Name  | Action | IP Pattern | Port Pattern | Protocol | Priority |
| ---------- | ------ | ---------- | ------------ | -------- | -------- |
| Allow HTTP | ALLOW  | *          | 80           | TCP      | 10       |
| Block SSH  | DENY   | *          | 22           | TCP      | 5        |
| Allow DNS  | ALLOW  | *          | 53           | UDP      | 15       |

---

## Example Packet Simulation

* Packet: 10.10.10.255:54321 -> 10.10.10.5:443 (HTTPS)
* Matching Rule: Block all 10.10.10.* on HTTPS
* Verdict: DENY
* Log displays timestamp, matched rule, and action.

---

## Results

* GUI shows real-time packet processing results.
* Logs capture all packets with detailed evaluation.
* Statistics update dynamically as packets are processed.

---

## Conclusion

This project demonstrates firewall packet filtering with a dynamic GUI interface. Users can test rules, simulate traffic, and analyze results. It is a valuable learning tool for understanding network security, rule prioritization, and packet processing.

---

## Future Enhancements

1. Stateful firewall simulation
2. Support for more protocols (FTP, SMTP)
3. Integration with real network capture
4. Advanced statistics and charts
5. User authentication and role-based access


---

## References

1. [JavaFX Documentation](https://openjfx.io/)
2. OSI Model and Firewall Filtering Concepts
3. Java java.util and java.time documentation

## Author

- **Name:** Ketan Sonawane  
- **Project Type:** Major Project / Simulation  
- **Technology Stack:** Java, JavaFX



