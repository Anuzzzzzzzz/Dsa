package question7;

import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.util.HashMap;
    import java.util.Map;
    import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

    class User {
        String name;
        String profilePicture;
        int followers;
        Map<String, Integer> connections;
        int x;
        int y;

        public User(String name, String profilePicture, int followers) {
            this.name = name;
            this.profilePicture = profilePicture;
            this.followers = followers;
            this.connections = new HashMap<>();

            // Calculate random positions for demonstration (replace with actual layout algorithm)
            this.x = (int) (Math.random() * 600); // Assuming canvas width is 800
            this.y = (int) (Math.random() * 400); // Assuming canvas height is 600
        }
    }

    public class gui extends JFrame {

        private Map<String, User> users = new HashMap<>();
        private String selectedNode = null;
        private String selectedEdgeStart = null;
        private String selectedEdgeEnd = null;
        private JPanel canvas;

        public gui() {
            setTitle("Social Network Graph");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            canvas = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawGraph((Graphics2D) g);
                }
            };
            canvas.setBackground(Color.WHITE);

            JButton addNodeButton = new JButton("Add Node");
            JButton addEdgeButton = new JButton("Add Edge");
            JButton deleteButton = new JButton("Delete Selected");
            JTextField searchField = new JTextField(20);

            JPanel toolsPanel = new JPanel();
            toolsPanel.add(addNodeButton);
            toolsPanel.add(addEdgeButton);
            toolsPanel.add(deleteButton);
            toolsPanel.add(new JLabel("Search User:"));
            toolsPanel.add(searchField);

            setLayout(new BorderLayout());
            add(toolsPanel, BorderLayout.NORTH);
            add(canvas, BorderLayout.CENTER);

            JButton readFileButton = new JButton("Read File");
        toolsPanel.add(readFileButton); // Add the button to the tools panel

        // Add action listener for the "Read File" button
        readFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Read user data from file and update the users map
                readUserDataFromFile("userdata.txt");
                repaint(); // Repaint the canvas to reflect the changes
            }
        });

            canvas.addMouseListener(new MouseAdapter() {
                private int offsetX;
                private int offsetY;
            
                @Override
                public void mouseClicked(MouseEvent e) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
            
                    // Check if the click is inside any node's area
                    for (User user : users.values()) {
                        int nodeX = user.x;
                        int nodeY = user.y;
            
                        if (mouseX >= nodeX && mouseX <= nodeX + 30 && mouseY >= nodeY && mouseY <= nodeY + 30) {
                            selectedNode = user.name;
                            selectedEdgeStart = null;
                            selectedEdgeEnd = null;
                            offsetX = mouseX - nodeX;
                            offsetY = mouseY - nodeY;
                            repaint();
                            break; // No need to check other nodes
                        }
                    }
                }
            
                @Override
                public void mousePressed(MouseEvent e) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
            
                    // Check if the click is inside any node's area
                    for (User user : users.values()) {
                        int nodeX = user.x;
                        int nodeY = user.y;
            
                        if (mouseX >= nodeX && mouseX <= nodeX + 30 && mouseY >= nodeY && mouseY <= nodeY + 30) {
                            selectedNode = user.name;
                            selectedEdgeStart = null;
                            selectedEdgeEnd = null;
                            offsetX = mouseX - nodeX;
                            offsetY = mouseY - nodeY;
                            repaint();
                            break; // No need to check other nodes
                        }
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (selectedNode != null) {
                        int newX = e.getX() - offsetX;
                        int newY = e.getY() - offsetY;
            
                        // Update the node's position
                        User selectedUser = users.get(selectedNode);
                        selectedUser.x = newX;
                        selectedUser.y = newY;
            
                        repaint();
                    }
                }
            });
            
        
            
            
            addNodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String userName = JOptionPane.showInputDialog("Enter user name:");
                    if (userName != null && !userName.isEmpty()) {
                        users.put(userName, new User(userName, "default_profile.png", 0));
                        repaint();
                    }
                     writeUserDataToFile("graph.txt");
                }
            });

            addEdgeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sourceUserName = JOptionPane.showInputDialog("Enter source user name:");
                    String targetUserName = JOptionPane.showInputDialog("Enter target user name:");
                    if (sourceUserName != null && targetUserName != null &&
                        users.containsKey(sourceUserName) && users.containsKey(targetUserName)) {
                        int strength = Integer.parseInt(JOptionPane.showInputDialog("Enter connection strength:"));
                        users.get(sourceUserName).connections.put(targetUserName, strength);
                        repaint();
                    }
                     writeUserDataToFile("graph.txt");
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedNode != null) {
                        users.remove(selectedNode);
                        selectedNode = null;
                        repaint();
                    }
                    if (selectedEdgeStart != null && selectedEdgeEnd != null) {
                        users.get(selectedEdgeStart).connections.remove(selectedEdgeEnd);
                        selectedEdgeStart = null;
                        selectedEdgeEnd = null;
                        repaint();
                    }
                   writeUserDataToFile("graph.txt");
                }
            });

            searchField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String searchUserName = searchField.getText();
                    if (users.containsKey(searchUserName)) {
                        selectedNode = searchUserName;
                        selectedEdgeStart = null;
                        selectedEdgeEnd = null;
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(gui.this, "User not found.");
                    }
                }
            });
        }

        private void drawGraph(Graphics2D g2) {
            for (User user : users.values()) {
                int x = user.x;
                int y = user.y;

                Ellipse2D.Double circle = new Ellipse2D.Double(x, y, 30, 30);
                g2.setColor(Color.BLUE);
                g2.fill(circle);

                g2.setColor(Color.BLACK);
                g2.drawString(user.name, x - 5, y - 5);

                g2.setColor(Color.GRAY);
                for (String connection : user.connections.keySet()) {
                    User targetUser = users.get(connection);
                    int targetX = targetUser.x;
                    int targetY = targetUser.y;

                    g2.drawLine(x + 15, y + 15, targetX + 15, targetY + 15);
                }
            }

            if (selectedNode != null) {
                g2.setColor(Color.RED);
                User selectedUser = users.get(selectedNode);
                int x = selectedUser.x;
                int y = selectedUser.y;
                g2.drawOval(x - 5, y - 5, 40, 40);
            }

            if (selectedEdgeStart != null && selectedEdgeEnd != null) {
                g2.setColor(Color.RED);
                User startUser = users.get(selectedEdgeStart);
                User endUser = users.get(selectedEdgeEnd);
                int startX = (startUser.x + endUser.x) / 2;
                int startY = (startUser.y + endUser.y) / 2;
                int endX = startX; // Replace with appropriate logic
                int endY = startY; // Replace with appropriate logic
                g2.drawLine(startX, startY, endX, endY);
            }
        }
         private void readUserDataFromFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                String userName = data[0];
                String profilePicture = data[1];
                int followers = Integer.parseInt(data[2]);
                User user = new User(userName, profilePicture, followers);

                // Parse connections and strengths
                String[] connections = data[3].split(",");
                String[] strengths = data[4].split(",");
                for (int i = 0; i < connections.length; i++) {
                    user.connections.put(connections[i], Integer.parseInt(strengths[i]));
                }

                users.put(userName, user);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       private void writeUserDataToFile(String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            for (User user : users.values()) {
                StringBuilder connections = new StringBuilder();
                StringBuilder strengths = new StringBuilder();
                for (Map.Entry<String, Integer> entry : user.connections.entrySet()) {
                    if (connections.length() > 0) {
                        connections.append(",");
                        strengths.append(",");
                    }
                    connections.append(entry.getKey());
                    strengths.append(entry.getValue());
                }
                
                String line = String.format("%s;%s;%d;%s;%s",
                    user.name, user.profilePicture, user.followers, connections, strengths);
                printWriter.println(line);
            }
            
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new gui().setVisible(true);
                }
            });
        }
    }

