import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnhancedFirewallSimulation extends Application {

    private ObservableList<FirewallRule> rulesList = FXCollections.observableArrayList();
    private ObservableList<PacketLog> packetLogs = FXCollections.observableArrayList();
    private int packetsProcessed = 0;
    private int packetsAllowed = 0;
    private int packetsDenied = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Advanced Firewall Simulation System");

        // Create TabPane for organized interface
        TabPane tabPane = new TabPane();
        
        // Tab 1: Packet Testing
        Tab packetTab = new Tab("Packet Testing");
        packetTab.setClosable(false);
        packetTab.setContent(createPacketTestingPane());
        
        // Tab 2: Firewall Rules
        Tab rulesTab = new Tab("Firewall Rules");
        rulesTab.setClosable(false);
        rulesTab.setContent(createFirewallRulesPane());
        
        // Tab 3: Simulation & Logs
        Tab simTab = new Tab("Simulation & Logs");
        simTab.setClosable(false);
        simTab.setContent(createSimulationPane());
        
        // Tab 4: Statistics
        Tab statsTab = new Tab("Statistics");
        statsTab.setClosable(false);
        statsTab.setContent(createStatisticsPane());
        
        tabPane.getTabs().addAll(packetTab, rulesTab, simTab, statsTab);

        Scene scene = new Scene(tabPane, 900, 700);
        scene.getStylesheets().add("data:text/css," + getCustomCSS());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Add default rules
        addDefaultRules();
    }

    private VBox createPacketTestingPane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Test Individual Packet");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(15);

        // Packet input fields
        TextField sourceIP = new TextField("192.168.1.100");
        TextField destIP = new TextField("10.0.0.50");
        TextField sourcePort = new TextField("54321");
        TextField destPort = new TextField("80");
        
        ComboBox<String> protocol = new ComboBox<>();
        protocol.getItems().addAll("TCP", "UDP", "ICMP", "HTTP", "HTTPS");
        protocol.setValue("TCP");
        
        TextField packetSize = new TextField("1024");

        grid.add(new Label("Source IP:"), 0, 0);
        grid.add(sourceIP, 1, 0);
        grid.add(new Label("Destination IP:"), 2, 0);
        grid.add(destIP, 3, 0);
        
        grid.add(new Label("Source Port:"), 0, 1);
        grid.add(sourcePort, 1, 1);
        grid.add(new Label("Destination Port:"), 2, 1);
        grid.add(destPort, 3, 1);
        
        grid.add(new Label("Protocol:"), 0, 2);
        grid.add(protocol, 1, 2);
        grid.add(new Label("Packet Size (bytes):"), 2, 2);
        grid.add(packetSize, 3, 2);

        // Result area
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(250);
        resultArea.setStyle("-fx-font-family: 'Courier New';");

        // Test button
        Button testButton = new Button("Test Packet");
        testButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        
        testButton.setOnAction(e -> {
            if (validatePacketInputs(sourceIP, destIP, sourcePort, destPort, packetSize, resultArea)) {
                Packet packet = new Packet(
                    sourceIP.getText(),
                    destIP.getText(),
                    sourcePort.getText(),
                    destPort.getText(),
                    protocol.getValue(),
                    Integer.parseInt(packetSize.getText())
                );
                
                String result = processPacket(packet);
                resultArea.setText(result);
            }
        });

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> resultArea.clear());

        HBox buttonBox = new HBox(10, testButton, clearButton);

        vbox.getChildren().addAll(title, new Separator(), grid, buttonBox, new Label("Test Results:"), resultArea);
        return vbox;
    }

    private VBox createFirewallRulesPane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Firewall Rules Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Rule input form
        GridPane ruleGrid = new GridPane();
        ruleGrid.setVgap(10);
        ruleGrid.setHgap(10);

        TextField ruleName = new TextField();
        ComboBox<String> action = new ComboBox<>();
        action.getItems().addAll("ALLOW", "DENY");
        action.setValue("ALLOW");
        
        TextField ipPattern = new TextField("*");
        TextField portPattern = new TextField("*");
        ComboBox<String> protocolRule = new ComboBox<>();
        protocolRule.getItems().addAll("ANY", "TCP", "UDP", "ICMP", "HTTP", "HTTPS");
        protocolRule.setValue("ANY");
        
        TextField priority = new TextField("100");

        ruleGrid.add(new Label("Rule Name:"), 0, 0);
        ruleGrid.add(ruleName, 1, 0);
        ruleGrid.add(new Label("Action:"), 0, 1);
        ruleGrid.add(action, 1, 1);
        ruleGrid.add(new Label("IP Pattern:"), 0, 2);
        ruleGrid.add(ipPattern, 1, 2);
        ruleGrid.add(new Label("Port Pattern:"), 0, 3);
        ruleGrid.add(portPattern, 1, 3);
        ruleGrid.add(new Label("Protocol:"), 0, 4);
        ruleGrid.add(protocolRule, 1, 4);
        ruleGrid.add(new Label("Priority:"), 0, 5);
        ruleGrid.add(priority, 1, 5);

        Button addRuleButton = new Button("Add Rule");
        addRuleButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        // Rules table
        TableView<FirewallRule> rulesTable = new TableView<>();
        rulesTable.setItems(rulesList);

        TableColumn<FirewallRule, String> nameCol = new TableColumn<>("Rule Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<FirewallRule, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setPrefWidth(80);

        TableColumn<FirewallRule, String> ipCol = new TableColumn<>("IP Pattern");
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ipPattern"));
        ipCol.setPrefWidth(120);

        TableColumn<FirewallRule, String> portCol = new TableColumn<>("Port");
        portCol.setCellValueFactory(new PropertyValueFactory<>("portPattern"));
        portCol.setPrefWidth(80);

        TableColumn<FirewallRule, String> protoCol = new TableColumn<>("Protocol");
        protoCol.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        protoCol.setPrefWidth(100);

        TableColumn<FirewallRule, Integer> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(80);

        rulesTable.getColumns().addAll(nameCol, actionCol, ipCol, portCol, protoCol, priorityCol);

        addRuleButton.setOnAction(e -> {
            try {
                FirewallRule rule = new FirewallRule(
                    ruleName.getText(),
                    action.getValue(),
                    ipPattern.getText(),
                    portPattern.getText(),
                    protocolRule.getValue(),
                    Integer.parseInt(priority.getText())
                );
                rulesList.add(rule);
                sortRulesByPriority();
                ruleName.clear();
            } catch (Exception ex) {
                showAlert("Error", "Invalid rule configuration");
            }
        });

        Button deleteRuleButton = new Button("Delete Selected");
        deleteRuleButton.setOnAction(e -> {
            FirewallRule selected = rulesTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                rulesList.remove(selected);
            }
        });

        HBox buttonBox = new HBox(10, addRuleButton, deleteRuleButton);

        vbox.getChildren().addAll(title, new Separator(), ruleGrid, buttonBox, 
                                  new Label("Active Rules:"), rulesTable);
        return vbox;
    }

    private VBox createSimulationPane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Bulk Simulation & Traffic Logs");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane simGrid = new GridPane();
        simGrid.setVgap(10);
        simGrid.setHgap(10);

        TextField packetCount = new TextField("50");
        CheckBox logTraffic = new CheckBox("Enable Detailed Logging");
        logTraffic.setSelected(true);
        CheckBox randomize = new CheckBox("Randomize Packet Data");
        randomize.setSelected(true);

        simGrid.add(new Label("Number of Packets:"), 0, 0);
        simGrid.add(packetCount, 1, 0);
        simGrid.add(logTraffic, 0, 1);
        simGrid.add(randomize, 1, 1);

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(300);
        logArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11px;");

        Button runSimButton = new Button("Run Simulation");
        runSimButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        
        runSimButton.setOnAction(e -> {
            try {
                int count = Integer.parseInt(packetCount.getText());
                runBulkSimulation(count, logTraffic.isSelected(), randomize.isSelected(), logArea);
            } catch (Exception ex) {
                showAlert("Error", "Invalid packet count");
            }
        });

        Button clearLogButton = new Button("Clear Logs");
        clearLogButton.setOnAction(e -> {
            logArea.clear();
            packetLogs.clear();
        });

        HBox buttonBox = new HBox(10, runSimButton, clearLogButton);

        vbox.getChildren().addAll(title, new Separator(), simGrid, buttonBox, 
                                  new Label("Simulation Logs:"), logArea);
        return vbox;
    }

    private VBox createStatisticsPane() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label title = new Label("Traffic Statistics");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane statsGrid = new GridPane();
        statsGrid.setVgap(15);
        statsGrid.setHgap(20);
        statsGrid.setStyle("-fx-font-size: 14px;");

        Label processedLabel = new Label("Total Packets Processed: 0");
        Label allowedLabel = new Label("Packets Allowed: 0");
        Label deniedLabel = new Label("Packets Denied: 0");
        Label rateLabel = new Label("Allow Rate: 0%");

        statsGrid.add(processedLabel, 0, 0);
        statsGrid.add(allowedLabel, 0, 1);
        statsGrid.add(deniedLabel, 0, 2);
        statsGrid.add(rateLabel, 0, 3);

        Button refreshButton = new Button("Refresh Statistics");
        refreshButton.setOnAction(e -> {
            processedLabel.setText("Total Packets Processed: " + packetsProcessed);
            allowedLabel.setText("Packets Allowed: " + packetsAllowed);
            deniedLabel.setText("Packets Denied: " + packetsDenied);
            double rate = packetsProcessed > 0 ? (packetsAllowed * 100.0 / packetsProcessed) : 0;
            rateLabel.setText(String.format("Allow Rate: %.1f%%", rate));
        });

        Button resetButton = new Button("Reset Statistics");
        resetButton.setOnAction(e -> {
            packetsProcessed = 0;
            packetsAllowed = 0;
            packetsDenied = 0;
            refreshButton.fire();
        });

        HBox buttonBox = new HBox(10, refreshButton, resetButton);

        vbox.getChildren().addAll(title, new Separator(), statsGrid, buttonBox);
        return vbox;
    }

    private boolean validatePacketInputs(TextField sourceIP, TextField destIP, 
                                        TextField sourcePort, TextField destPort,
                                        TextField packetSize, TextArea resultArea) {
        if (sourceIP.getText().isEmpty() || destIP.getText().isEmpty()) {
            resultArea.setText("ERROR: IP addresses cannot be empty");
            return false;
        }
        try {
            int sport = Integer.parseInt(sourcePort.getText());
            int dport = Integer.parseInt(destPort.getText());
            int size = Integer.parseInt(packetSize.getText());
            if (sport < 0 || sport > 65535 || dport < 0 || dport > 65535) {
                resultArea.setText("ERROR: Ports must be between 0 and 65535");
                return false;
            }
            if (size < 0) {
                resultArea.setText("ERROR: Packet size must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            resultArea.setText("ERROR: Invalid number format");
            return false;
        }
        return true;
    }

    private String processPacket(Packet packet) {
        StringBuilder result = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        result.append("=== PACKET ANALYSIS ===\n");
        result.append("Timestamp: ").append(LocalDateTime.now().format(formatter)).append("\n\n");
        result.append("Source: ").append(packet.sourceIP).append(":").append(packet.sourcePort).append("\n");
        result.append("Destination: ").append(packet.destIP).append(":").append(packet.destPort).append("\n");
        result.append("Protocol: ").append(packet.protocol).append("\n");
        result.append("Size: ").append(packet.size).append(" bytes\n\n");
        
        result.append("=== FIREWALL EVALUATION ===\n");
        
        FirewallRule matchedRule = null;
        for (FirewallRule rule : rulesList) {
            if (ruleMatches(rule, packet)) {
                matchedRule = rule;
                break;
            }
        }
        
        if (matchedRule != null) {
            result.append("Matched Rule: ").append(matchedRule.name).append("\n");
            result.append("Action: ").append(matchedRule.action).append("\n");
            result.append("Priority: ").append(matchedRule.priority).append("\n");
            result.append("\n>>> VERDICT: ").append(matchedRule.action).append(" <<<\n");
            
            if (matchedRule.action.equals("ALLOW")) {
                packetsAllowed++;
            } else {
                packetsDenied++;
            }
        } else {
            result.append("No matching rule found\n");
            result.append("\n>>> VERDICT: DENY (Default Policy) <<<\n");
            packetsDenied++;
        }
        
        packetsProcessed++;
        return result.toString();
    }

    private void runBulkSimulation(int count, boolean detailed, boolean randomize, TextArea logArea) {
        logArea.clear();
        Random rand = new Random();
        
        logArea.appendText("Starting bulk simulation with " + count + " packets...\n");
        logArea.appendText("==============================================\n\n");
        
        for (int i = 0; i < count; i++) {
            Packet packet;
            if (randomize) {
                packet = generateRandomPacket(rand);
            } else {
                packet = new Packet("192.168.1." + (i % 255), "10.0.0." + (i % 255),
                                  String.valueOf(1024 + i), "80", "TCP", 1024);
            }
            
            FirewallRule matchedRule = null;
            for (FirewallRule rule : rulesList) {
                if (ruleMatches(rule, packet)) {
                    matchedRule = rule;
                    break;
                }
            }
            
            String verdict = matchedRule != null ? matchedRule.action : "DENY";
            if (verdict.equals("ALLOW")) {
                packetsAllowed++;
            } else {
                packetsDenied++;
            }
            packetsProcessed++;
            
            if (detailed) {
                logArea.appendText(String.format("[%03d] %s:%s -> %s:%s [%s] = %s%s\n",
                    i + 1, packet.sourceIP, packet.sourcePort, packet.destIP, packet.destPort,
                    packet.protocol, verdict,
                    matchedRule != null ? " (Rule: " + matchedRule.name + ")" : " (Default)"));
            }
        }
        
        logArea.appendText("\n==============================================\n");
        logArea.appendText("Simulation Complete!\n");
        logArea.appendText(String.format("Processed: %d | Allowed: %d | Denied: %d\n",
                          packetsProcessed, packetsAllowed, packetsDenied));
    }

    private Packet generateRandomPacket(Random rand) {
        String[] protocols = {"TCP", "UDP", "ICMP", "HTTP", "HTTPS"};
        return new Packet(
            "192.168." + rand.nextInt(256) + "." + rand.nextInt(256),
            "10.0." + rand.nextInt(256) + "." + rand.nextInt(256),
            String.valueOf(1024 + rand.nextInt(64000)),
            String.valueOf(rand.nextInt(1000) < 500 ? 80 : 443),
            protocols[rand.nextInt(protocols.length)],
            64 + rand.nextInt(1472)
        );
    }

    private boolean ruleMatches(FirewallRule rule, Packet packet) {
        if (!rule.protocol.equals("ANY") && !rule.protocol.equals(packet.protocol)) {
            return false;
        }
        if (!rule.ipPattern.equals("*") && !packet.destIP.contains(rule.ipPattern)) {
            return false;
        }
        if (!rule.portPattern.equals("*") && !packet.destPort.equals(rule.portPattern)) {
            return false;
        }
        return true;
    }

    private void sortRulesByPriority() {
        rulesList.sort((r1, r2) -> Integer.compare(r1.priority, r2.priority));
    }

    private void addDefaultRules() {
        rulesList.add(new FirewallRule("Allow HTTP", "ALLOW", "*", "80", "TCP", 10));
        rulesList.add(new FirewallRule("Allow HTTPS", "ALLOW", "*", "443", "TCP", 10));
        rulesList.add(new FirewallRule("Block SSH", "DENY", "*", "22", "TCP", 5));
        rulesList.add(new FirewallRule("Allow DNS", "ALLOW", "*", "53", "UDP", 15));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getCustomCSS() {
        return ".tab-pane { -fx-background-color: #f5f5f5; }";
    }

    // Inner classes
    public static class Packet {
        String sourceIP, destIP, sourcePort, destPort, protocol;
        int size;

        public Packet(String sourceIP, String destIP, String sourcePort, 
                     String destPort, String protocol, int size) {
            this.sourceIP = sourceIP;
            this.destIP = destIP;
            this.sourcePort = sourcePort;
            this.destPort = destPort;
            this.protocol = protocol;
            this.size = size;
        }
    }

    public static class FirewallRule {
        private String name, action, ipPattern, portPattern, protocol;
        private int priority;

        public FirewallRule(String name, String action, String ipPattern,
                          String portPattern, String protocol, int priority) {
            this.name = name;
            this.action = action;
            this.ipPattern = ipPattern;
            this.portPattern = portPattern;
            this.protocol = protocol;
            this.priority = priority;
        }

        public String getName() { return name; }
        public String getAction() { return action; }
        public String getIpPattern() { return ipPattern; }
        public String getPortPattern() { return portPattern; }
        public String getProtocol() { return protocol; }
        public int getPriority() { return priority; }
    }

    public static class PacketLog {
        private String timestamp, source, destination, protocol, verdict;

        public PacketLog(String timestamp, String source, String destination,
                        String protocol, String verdict) {
            this.timestamp = timestamp;
            this.source = source;
            this.destination = destination;
            this.protocol = protocol;
            this.verdict = verdict;
        }

        public String getTimestamp() { return timestamp; }
        public String getSource() { return source; }
        public String getDestination() { return destination; }
        public String getProtocol() { return protocol; }
        public String getVerdict() { return verdict; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}