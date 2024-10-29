import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StackAssembler {
    private static final HashMap<String, Integer> opcodeMap = new HashMap<>();
    private static final ArrayList<Integer> stack = new ArrayList<>();

    public static void main(String[] args) {
        initializeOpcodeMap();
        
        JFrame frame = new JFrame("Stack-Based Assembler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JButton loadButton = new JButton("Load Instructions");

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = JOptionPane.showInputDialog("Enter the file path:");
                try {
                    readInstructionsFromFile(filePath);
                    outputArea.setText("Stack: " + stack.toString());
                } catch (Exception ex) {
                    outputArea.setText("Error: " + ex.getMessage());
                }
            }
        });

        frame.add(loadButton, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void initializeOpcodeMap() {
        opcodeMap.put("PUSH", 0x01);
        opcodeMap.put("POP", 0x02);
        opcodeMap.put("ADD", 0x03);
        opcodeMap.put("SUB", 0x04);
    }

    private static void readInstructionsFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            processInstruction(line.trim());
        }
        reader.close();
    }

    private static void processInstruction(String instruction) {
        if (instruction.startsWith("PUSH")) {
            String[] parts = instruction.split(" ");
            int value = Integer.parseInt(parts[1]);
            stack.add(value);
        } else if (instruction.equals("POP")) {
            if (!stack.isEmpty()) {
                stack.remove(stack.size() - 1);
            } else {
                throw new RuntimeException("Stack underflow");
            }
        } else if (instruction.equals("ADD")) {
            if (stack.size() < 2) throw new RuntimeException("Not enough values to add");
            int a = stack.remove(stack.size() - 1);
            int b = stack.remove(stack.size() - 1);
            stack.add(a + b);
        } else {
            throw new RuntimeException("Unknown instruction: " + instruction);
        }
    }
}
